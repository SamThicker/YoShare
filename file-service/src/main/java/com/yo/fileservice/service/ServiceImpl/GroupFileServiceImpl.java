package com.yo.fileservice.service.ServiceImpl;

import com.yo.fileservice.components.MinioTemplate;
import com.yo.fileservice.util.CallBack;
import com.yo.fileservice.util.FileResInfoAdapter;
import com.yo.fileservice.util.FileUtil;
import com.yo.fileservice.vo.SimpleFileInfo;
import com.yo.fileservice.service.GroupFileService;
import com.yo.fileservice.vo.VOFileResourceInfo;
import com.yo.fileservice.vo.VOFileTransInfo;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.CmsFileTransInfoMapper;
import com.yo.yoshare.mbg.mapper.CmsGroupFileMapper;
import com.yo.yoshare.mbg.mapper.CmsGroupResourceMapper;
import com.yo.yoshare.mbg.model.*;
import io.minio.errors.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.xmlpull.v1.XmlPullParserException;

import javax.security.auth.callback.Callback;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupFileServiceImpl implements GroupFileService, CallBack {


    @Value("${file.rootDir.groupFile}")
    private  String GROUP_FILE_DIR;
    @Autowired
    private CmsGroupFileMapper groupFileMapper;
    @Autowired
    private CmsGroupResourceMapper groupResourceMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MinioTemplate minioTemplate;
    @Autowired
    private CmsFileTransInfoMapper fileTransInfoMapper;
    @Autowired
    private FileUtil fileUtil;
    @Value("${file.rootDir.partFile}")
    private String PART_FILE_DIR;


    @Override
    public CommonResult uploadFile(VOFileResourceInfo info, Long groupId) throws Exception {
        String userId = request.getHeader("userId");
        String userName = request.getHeader("userName");
        //设置文件信息
        CmsGroupFile file = FileResInfoAdapter.toGroupFile(info);
        file.setByName(userName);
        file.setById(Long.parseLong(userId));
        file.setGroupId(groupId);
        //插入数据
        groupFileMapper.insertSelective(file);
        CmsGroupResource resource = FileResInfoAdapter.toGroupResource(info);
        resource.setGroupId(groupId);
        resource.setResourceRef(file.getId().toString());
        resource.setByUserId(Long.parseLong(userId));
        groupResourceMapper.insertSelective(resource);
        CmsFileTransInfo transInfo = new CmsFileTransInfo();
        CmsFileTransInfoExample example = new CmsFileTransInfoExample();
        example.createCriteria().andMd5EqualTo(info.getFileMd5()).andFinishEqualTo("0");
        List<CmsFileTransInfo> list = fileTransInfoMapper.selectByExample(example);
        int result = list.size()>0 ? list.get(0).getCursor() : 0;
        return CommonResult.success(result);
    }

    @Override
    public CommonResult uploadExistFile(Long groupId, VOFileResourceInfo info) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        String userId = request.getHeader("uesrId");
        String userName = request.getHeader("userName");
        CmsGroupFileExample example = new CmsGroupFileExample();
        example.createCriteria().andMd5EqualTo(info.getFileMd5());
        List<CmsGroupFile> files = groupFileMapper.selectByExample(example);
        if (files.size() == 0){
            return CommonResult.failed("文件不存在");
        }
        CmsGroupFile file = files.get(0);
        String srcFileName = fileDirTrans(file);
        String targetFileName = fileDirTrans(info.getFileMd5(), info.getFileName());
        minioTemplate.copyObject("file", targetFileName,"file", srcFileName);
        //数据库操作
        file.setId(null);
        file.setById(Long.parseLong(userId));
        file.setName(userName);
        file.setGroupId(groupId);
        groupFileMapper.insertSelective(file);
        //复制数据
        CmsGroupResource resource = FileResInfoAdapter.toGroupResource(info);
        resource.setByUserId(Long.parseLong(userId));
        resource.setGroupId(groupId);
        resource.setResourceRef(file.getId().toString());
        groupResourceMapper.insertSelective(resource);
        return CommonResult.success("成功");
    }


    @Override
    public CommonResult getFileInfo(String fileId, String groupId) {
        String userId = request.getHeader("userId");
        CmsGroupFile file = getExistFile(groupId, fileId);
        if (null == file) {
            return CommonResult.failed("找不到相关文件");
        }
        SimpleFileInfo fileInfo = new SimpleFileInfo();
        BeanUtils.copyProperties(file, fileInfo);
        //得到要访问的文件的URL
        String objectName = fileDirTrans(file.getMd5(), file.getName());
        String url = minioTemplate.getObjectURL("file", objectName, 60 * 60 *24);
        String reg = "^(https|http)(:\\/\\/)[a-zA-Z0-9.]+:[0-9]{1,}";
        url = url.replaceAll(reg, "");
        fileInfo.setUrl(url);
        return CommonResult.success(fileInfo, "操作成功");
    }

    @Override
    public CommonResult uploadMultipartFile(VOFileTransInfo info, Long groupId) throws Exception {
        String hashString;
        MessageDigest md5;
        CmsFileTransInfo realInfo = fileUtil.getTransInfo(info);
        if (realInfo.getCursor()>=realInfo.getTotalNum()){
            return CommonResult.failed("文件出错");
        }
        fileUtil.saveFile(info);
        realInfo.setCursor(realInfo.getCursor()+1);
        fileTransInfoMapper.updateByPrimaryKeySelective(realInfo);
        System.out.println("start:" +System.currentTimeMillis());
        if (realInfo.getCursor().equals(realInfo.getTotalNum())){
            fileUtil.callBack(this, info);
        }
        System.out.println("end:" +System.currentTimeMillis());
        return CommonResult.success("操作成功");
    }

    @Override
    public CommonResult deleteFile(String fileId, String groupId) throws Exception {
        String userId = request.getHeader("userId");
        CmsGroupFile file = getExistFile(groupId, fileId);
        if (null == file) {
            return CommonResult.failed("找不到相关文件");
        }
        groupFileMapper.deleteByPrimaryKey(file.getId());
        String objectName = fileDirTrans(file);
        minioTemplate.removeObject("file", objectName);
        return CommonResult.success("操作成功");
    }


    /**文件信息转换为minio文件名*/
    String fileDirTrans(CmsGroupFile file){
        return file.getMd5() + "/group/" + file.getName();
    }

    /**文件信息转换为minio文件名*/
    String fileDirTrans(String hash, String name){
        return hash + "/group/" + name;
    }

    /**检查文件是否存在，若存在，则获取文件信息，否则返回null*/
    CmsGroupFile getExistFile(String groupId, String fileId){
        CmsGroupFileExample example = new CmsGroupFileExample();
        example.createCriteria().andIdEqualTo(Long.parseLong(fileId)).andGroupIdEqualTo(Long.parseLong(groupId));
        List<CmsGroupFile> fileInfoList = groupFileMapper.selectByExample(example);
        if (fileInfoList.size() <= 0){
            return null;
        } else {
            return fileInfoList.get(0);
        }
    }

    @Override
    public void doCallBack(CmsFileTransInfo info) throws Exception {
        mergeFile(info);
    }

    /**合并文件*/
    void mergeFile(CmsFileTransInfo info) throws Exception {
        String parentDirStr = PART_FILE_DIR + info.getMd5();
        File parentDir = new File(parentDirStr);
        String outFile = parentDirStr + "/" + info.getName();
        String[] list =  parentDir.list();
        Arrays.sort(list);
        //合并文件
        FileUtil.doMergeFiles(outFile, list, parentDirStr);
        String hashString = "";
        //校验文件MD5
        hashString = DigestUtils.md5Hex(new FileInputStream(outFile));
        if (!hashString.equals(info.getMd5())) {
            CmsFileTransInfo info1 = new CmsFileTransInfo();
            info1.setMessage("文件出错");
            CmsFileTransInfoExample example = new CmsFileTransInfoExample();
            fileTransInfoMapper.updateByExampleSelective(info1,example);
            return;
        }
        String fileName = fileDirTrans(hashString, info.getName());
        File file = new File(outFile);
        //把文件存到Minio
        minioTemplate.putObject("file", fileName, new FileInputStream(file), file.length(), "file.getContentType()");
        //数据库操作
        CmsGroupFile groupFile = new CmsGroupFile();
        groupFile.setStatus("1");
        groupFile.setSize((long) outFile.length());
        CmsGroupFileExample example = new CmsGroupFileExample();
        example.createCriteria().andMd5EqualTo(info.getMd5()).andStatusEqualTo("0");
        groupFileMapper.updateByExampleSelective(groupFile, example);
        CmsFileTransInfo info1 = new CmsFileTransInfo();
        info1.setFinish("1");
        info1.setStatus("0");
        CmsFileTransInfoExample example1 = new CmsFileTransInfoExample();
        example1.createCriteria().andFinishEqualTo("0").andMd5EqualTo(info.getMd5());
        fileTransInfoMapper.updateByExampleSelective(info1, example1);
        FileUtil.deleteTempFiles(parentDirStr);
    }


//    @Override
//    public CommonResult uploadFile(Long id, Long groupId, String hash, MultipartFile file, String title, String description, Optional<Long> classis) throws Exception {
//        id = Long.parseLong(request.getHeader("userId"));
//        String hashString = "";
//        MessageDigest md5;
//        String name = file.getOriginalFilename();
//        String type = name.substring(name.lastIndexOf(".")+1);
//        //文件操作
//        md5 = MessageDigest.getInstance("MD5");
//        byte[] uploadBytes = file.getBytes();
//        byte[] digest = md5.digest(uploadBytes);
//        hashString = new BigInteger(1, digest).toString(16);
//        if (!hashString.equals(hash)) {
//            return CommonResult.failed("文件出错");
//        }
//        String fileName = fileDirTrans(hashString, file.getOriginalFilename());
//        minioTemplate.putObject("file", fileName, file.getInputStream(), file.getSize(), "file.getContentType()");
//        //数据库操作数据库操作
//        CmsGroupFile groupFile = new CmsGroupFile();
//        groupFile.setById(id);
//        groupFile.setMd5(hashString);
//        groupFile.setType(type);
//        groupFile.setSize(file.getSize());
//        groupFile.setName(name);
//        groupFile.setGroupId(groupId);
//        groupFileMapper.insertSelective(groupFile);
//        CmsGroupResource resource = new CmsGroupResource();
//        resource.setTitle(title);
//        resource.setDescription(description);
//        resource.setDateTime(new Date());
//        resource.setType("FILE");
//        resource.setResourceRef(groupFile.getId().toString());
//        resource.setClassification(classis.orElse(null));
//        resource.setByUserId(id);
//        resource.setGroupId(groupId);
//        groupResourceMapper.insertSelective(resource);
//        return CommonResult.success("成功");
//    }

}
