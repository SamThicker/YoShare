package com.yo.fileservice.vo;

import com.yo.yoshare.mbg.model.CmsFileTransInfo;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VOFileTransInfo extends CmsFileTransInfo {

    private MultipartFile file;


}
