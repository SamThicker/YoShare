package com.yo.fileservice.service.ServiceImpl;

import com.yo.fileservice.components.MinioTemplate;
import com.yo.fileservice.dto.SimpleFileInfo;
import com.yo.fileservice.service.GroupFileService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.CmsGroupFileMapper;
import com.yo.yoshare.mbg.mapper.CmsGroupResourceMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberFileMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceMapper;
import com.yo.yoshare.mbg.model.*;
import io.minio.errors.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupFileServiceImpl implements GroupFileService {


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


    @Override
    public CommonResult uploadFile(Long id, Long groupId, String hash, MultipartFile file, String title, String description, Optional<Long> classis) throws Exception {
        id = Long.parseLong(request.getHeader("userId"));
        String hashString = "";
        MessageDigest md5;
        String name = file.getOriginalFilename();
        String type = name.substring(name.lastIndexOf(".")+1);
        //文件操作
        md5 = MessageDigest.getInstance("MD5");
        byte[] uploadBytes = file.getBytes();
        byte[] digest = md5.digest(uploadBytes);
        hashString = new BigInteger(1, digest).toString(16);
        if (!hashString.equals(hash)) {
            return CommonResult.failed("文件出错");
        }
        String fileName = fileDirTrans(hashString, file.getOriginalFilename());
        minioTemplate.putObject("file", fileName, file.getInputStream(), file.getSize(), "file.getContentType()");
        //数据库操作数据库操作
        CmsGroupFile groupFile = new CmsGroupFile();
        groupFile.setById(id);
        groupFile.setMd5(hashString);
        groupFile.setType(type);
        groupFile.setSize(file.getSize());
        groupFile.setName(name);
        groupFile.setGroupId(groupId);
        groupFileMapper.insertSelective(groupFile);
        CmsGroupResource resource = new CmsGroupResource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setDateTime(new Date());
        resource.setType("FILE");
        resource.setResourceRef(groupFile.getId().toString());
        resource.setClassification(classis.orElse(null));
        resource.setByUserId(id);
        resource.setGroupId(groupId);
        groupResourceMapper.insertSelective(resource);
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult uploadExistFile(Long id, Long groupId,String name, String hash, String title, String description, Optional<Long> classis) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        id = Long.valueOf(request.getDateHeader("uesrId"));
        CmsGroupFileExample example = new CmsGroupFileExample();
        example.createCriteria().andMd5EqualTo(hash);
        List<CmsGroupFile> files = groupFileMapper.selectByExample(example);
        if (files.size() == 0){
            return CommonResult.failed("文件不存在");
        }
        CmsGroupFile file = files.get(0);
        String srcFileName = fileDirTrans(file);
        String targetFileName = fileDirTrans(hash, name);
        minioTemplate.copyObject("file", targetFileName,"file", srcFileName);
        //数据库操作
        file.setId(null);
        file.setById(id);
        file.setName(name);
        file.setGroupId(groupId);
        groupFileMapper.insertSelective(file);
        CmsGroupResource resource = new CmsGroupResource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setDateTime(new Date());
        resource.setType("FILE");
        resource.setResourceRef(file.getId().toString());
        resource.setClassification(classis.orElse(-1L));
        resource.setByUserId(id);
        resource.setGroupId(groupId);
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

}
