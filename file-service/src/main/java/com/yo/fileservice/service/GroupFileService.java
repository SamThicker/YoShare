package com.yo.fileservice.service;

import com.yo.yoshare.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface GroupFileService {

    CommonResult uploadFile(Long id, Long groupId, String hash, MultipartFile file, String title, String description, Optional<Long> classis) throws NoSuchAlgorithmException, IOException;

    CommonResult uploadExistFile(Long id, Long groupId, String name, String hash, String title, String description, Optional<Long> classis);
}
