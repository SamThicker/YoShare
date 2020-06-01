package com.yo.fileservice.util;

import com.yo.fileservice.vo.VOFileResourceInfo;
import com.yo.yoshare.mbg.model.CmsGroupFile;
import com.yo.yoshare.mbg.model.CmsGroupResource;

import java.util.Date;

public class FileResInfoAdapter {
    public static CmsGroupResource toGroupResource(VOFileResourceInfo info){
        CmsGroupResource resource = new CmsGroupResource();
        resource.setTitle(info.getResTitle());
        resource.setDescription(info.getResDescription());
        resource.setDateTime(new Date());
        resource.setType("FILE");
        resource.setClassification(Long.parseLong(info.getResClassification()));
        return resource;
    }


    public static CmsGroupFile toGroupFile(VOFileResourceInfo info){
        String name = info.getFileName();
        String type = name.substring(name.lastIndexOf(".")+1);
        CmsGroupFile file = new CmsGroupFile();
        file.setStatus("0");
        file.setName(name);
        file.setType(type);
        file.setMd5(info.getFileMd5());
        return file;
    }
}
