package com.yo.fileservice.service.ServiceImpl;

import com.yo.fileservice.service.GroupFileService;
import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.mbg.mapper.CmsGroupFileMapper;
import com.yo.yoshare.mbg.mapper.CmsGroupResourceMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberFileMapper;
import com.yo.yoshare.mbg.mapper.CmsMemberResourceMapper;
import com.yo.yoshare.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
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


    @Override
    public CommonResult uploadFile(Long id, Long groupId, String hash, MultipartFile file, String title, String description, Optional<Long> classis) throws NoSuchAlgorithmException, IOException {
        id = Long.valueOf(request.getDateHeader("uesrId"));
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
        File dir = new File(GROUP_FILE_DIR + hashString + "/");
        dir.mkdir();
        File dest = new File(GROUP_FILE_DIR + hashString + "/" + name);
        file.transferTo(dest);
        //数据库操作
        CmsGroupFile groupFile = new CmsGroupFile();
        groupFile.setById(id);
        groupFile.setMd5(hashString);
        groupFile.setType(type);
        groupFile.setSize(file.getSize());
        groupFile.setName(name);
        groupFileMapper.insertSelective(groupFile);
        CmsGroupResource resource = new CmsGroupResource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setDateTime(new Date());
        resource.setType("FILE");
        resource.setResourceRef(groupFile.getId().toString());
        resource.setClassification(classis.orElse(null));
        resource.setByUserId(id);
        groupResourceMapper.insertSelective(resource);
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult uploadExistFile(Long id, Long groupId,String name, String hash, String title, String description, Optional<Long> classis) {
        id = Long.valueOf(request.getDateHeader("uesrId"));
        CmsGroupFileExample example = new CmsGroupFileExample();
        example.createCriteria().andMd5EqualTo(hash);
        List<CmsGroupFile> files = groupFileMapper.selectByExample(example);
        if (files.size() == 0){
            return CommonResult.failed("文件不存在");
        }
        //数据库操作
        CmsGroupFile file = files.get(0);
        file.setId(null);
        file.setById(id);
        file.setName(name);
        groupFileMapper.insertSelective(file);
        CmsGroupResource resource = new CmsGroupResource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setDateTime(new Date());
        resource.setType("FILE");
        resource.setResourceRef(file.getId().toString());
        resource.setClassification(classis.orElse(-1L));
        resource.setByUserId(id);
        resource.setGroupId(Long.valueOf(1));
        groupResourceMapper.insertSelective(resource);
        return CommonResult.success("成功");
    }
}
