package com.yo.fileservice.service.ServiceImpl;

import com.alibaba.druid.util.StringUtils;
import com.yo.fileservice.dto.SimpleFileInfo;
import com.yo.fileservice.service.FileService;
import com.yo.fileservice.components.MinioTemplate;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.CmsMemberFileMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceMapper;
import com.yo.yoshare.mbg.model.CmsMemberFile;
import com.yo.yoshare.mbg.model.CmsMemberFileExample;
import com.yo.yoshare.mbg.model.CmsMemberResource;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.tika.Tika;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accesskey;
    @Value("${minio.secretKey}")
    private String secretkey;
    @Value("${file.rootDir.memberFile}")
    private  String MEMBER_FILE_DIR;
    @Autowired
    private CmsMemberFileMapper memberFileMapper;
    @Autowired
    private CmsMemberResourceMapper memberResourceMapper;
    @Autowired
    private HttpServletRequest request;


    @Bean
    public MinioClient client() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(endpoint, accesskey, secretkey);
    }

    @Autowired
    private MinioTemplate minioTemplate;

    @Override
    public CommonResult uploadResource(String username, MultipartFile file) throws Exception {
        minioTemplate.putObject("static","icon/a",file.getInputStream(),file.getSize(),file.getContentType());
        return CommonResult.success("","");
    }


    @Override
    public String presignedPutUrl(String bucketName, String objectName, Integer expires) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidResponseException, XmlParserException {
        return minioTemplate.presignedPutObjectUrl(bucketName, objectName, null).replaceFirst("^.+:[0-9]+","");
    }

    @Override
    public String presignedBlogFilePutUrl(Long userId, String fileName, int expires) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, InvalidResponseException, XmlParserException {
        String objectName = "blogPic/"+ userId + "-" +UUID.randomUUID()+fileName.substring(fileName.lastIndexOf("."));
        return minioTemplate.presignedPutObjectUrl("resource", objectName, null).replaceFirst("^.+:[0-9]+","");
    }

    @Override
    public CommonResult uploadFile(Long id, String hash, MultipartFile file, String title, String description, Optional<String> classis) throws Exception {
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
        minioTemplate.putObject("file", fileName, file.getInputStream(), file.getSize(), file.getContentType());
        //数据库操作
        CmsMemberFile memberFile = new CmsMemberFile();
        memberFile.setById(id);
        memberFile.setMd5(hashString);
        memberFile.setType(type);
        memberFile.setSize(file.getSize());
        memberFile.setName(name);
        memberFileMapper.insertSelective(memberFile);
        CmsMemberResource resource = new CmsMemberResource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setDatetime(new Date());
        resource.setType("FILE");
        resource.setResourceRef(memberFile.getId().toString());
        resource.setClassification(classis.orElse(""));
        resource.setByUserId(id);
        memberResourceMapper.insertSelective(resource);
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult uploadExistFile(Long id, String name, String hash, String title, String description, Optional<String> classis) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        CmsMemberFileExample example = new CmsMemberFileExample();
        example.createCriteria().andMd5EqualTo(hash);
        List<CmsMemberFile> files = memberFileMapper.selectByExample(example);
        if (files.size() == 0){
            return CommonResult.failed("文件不存在");
        }
        CmsMemberFile file = files.get(0);
        String srcFileName = fileDirTrans(file);
        String targetFileName = fileDirTrans(hash, name);
        minioTemplate.copyObject("file", targetFileName,"file", srcFileName);
        //数据库操作
        file.setId(null);
        file.setById(id);
        file.setName(name);
        memberFileMapper.insertSelective(file);
        CmsMemberResource resource = new CmsMemberResource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setDatetime(new Date());
        resource.setType("FILE");
        resource.setResourceRef(file.getId().toString());
        resource.setClassification(classis.orElse(""));
        resource.setByUserId(id);
        memberResourceMapper.insertSelective(resource);
        return CommonResult.success("成功");
    }

    @Override
    public void downloadFile(HttpServletRequest req, HttpServletResponse resp, Long userId, String fileId) throws IOException, ServletException {
        CmsMemberFile fileInfo = memberFileMapper.selectByPrimaryKey(Long.valueOf(fileId));
        System.out.println("进入下载");
        String fileName = fileInfo.getName();
        String md5 = fileInfo.getMd5();
        //得到要下载的文件
        File file = new File(MEMBER_FILE_DIR + File.separator + md5 + File.separator + fileName);
        System.out.println(file);
        Tika tika = new Tika();
        //String mimeType = Files.probeContentType(file.toPath());
        String mimeType = tika.detect(file);
        if (!StringUtils.isEmpty(mimeType)) {
            resp.setContentType(mimeType);
        }
        //如果文件不存在
        if(!file.exists()){
            req.setAttribute("error", "您要下载的资源已被删除！！");
        }
        //设置缓存区
        byte[] bytes = new byte[1024];
        //设置响应头，控制浏览器下载该文件
        //读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(file);
        //创建输出流
        OutputStream os = resp.getOutputStream();
        while((in.read(bytes))>0){
            os.write(bytes);
        }
        //关闭输入流
        in.close();
        //关闭输出流
        os.close();
    }


    @Override
    public CommonResult getFileInfo(String fileId) {
        String userId = request.getHeader("userId");
        CmsMemberFile file = getExistFile(userId, fileId);
        if (null == file) {
            return CommonResult.failed("找不到相关文件");
        }
        SimpleFileInfo fileInfo = new SimpleFileInfo();
        BeanUtils.copyProperties(file, fileInfo);
        //得到要访问的文件的URL
        String objectName = file.getMd5() + "/member/" + file.getName();
        String url = minioTemplate.getObjectURL("file", objectName, 60 * 60 *24);
        String reg = "^(https|http)(:\\/\\/)[a-zA-Z0-9.]+:[0-9]{1,}";
        url = url.replaceAll(reg, "");
        fileInfo.setUrl(url);
        return CommonResult.success(fileInfo, "操作成功");
    }

    @Override
    public CommonResult deleteFile(String fileId) throws Exception {
        String userId = request.getHeader("userId");
        CmsMemberFile file = getExistFile(userId, fileId);
        if (null == file) {
            return CommonResult.failed("找不到相关文件");
        }
        memberFileMapper.deleteByPrimaryKey(file.getId());
        String objectName = fileDirTrans(file);
        minioTemplate.removeObject("file", objectName);
        return CommonResult.success("操作成功");
    }

    /**文件信息转换为minio文件名*/
    String fileDirTrans(CmsMemberFile file){
        return file.getMd5() + "/member/" + file.getName();
    }

    /**文件信息转换为minio文件名*/
    String fileDirTrans(String hash, String name){
       return hash + "/member/" + name;
    }

    /**检查文件是否存在，若存在，则获取文件信息，否则返回null*/
    CmsMemberFile getExistFile(String userId, String fileId){
        CmsMemberFileExample example = new CmsMemberFileExample();
        example.createCriteria().andIdEqualTo(Long.parseLong(fileId)).andByIdEqualTo(Long.parseLong(userId));
        List<CmsMemberFile> fileInfoList = memberFileMapper.selectByExample(example);
        if (fileInfoList.size() <= 0){
            return null;
        } else {
            return fileInfoList.get(0);
        }
    }
}
