package com.yo.fileservice.service.ServiceImpl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
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
    public CommonResult uploadFile(Long id, String hash, MultipartFile file, String title, String description, Optional<String> classis) throws NoSuchAlgorithmException, IOException {
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
        File dir = new File(MEMBER_FILE_DIR + hashString + "/");
        dir.mkdir();
        File dest = new File(MEMBER_FILE_DIR + hashString + "/" + name);
        file.transferTo(dest);
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
    public CommonResult uploadExistFile(Long id, String name, String hash, String title, String description, Optional<String> classis) {
        CmsMemberFileExample example = new CmsMemberFileExample();
        example.createCriteria().andMd5EqualTo(hash);
        List<CmsMemberFile> files = memberFileMapper.selectByExample(example);
        if (files.size() == 0){
            return CommonResult.failed("文件不存在");
        }
        //数据库操作
        CmsMemberFile file = files.get(0);
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

}
