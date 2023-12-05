package com.yo.resourceservice.feignInterface;

import com.yo.resourceservice.component.FeignConfiguration;
import com.yo.yoshare.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(value = "file-service", configuration = FeignConfiguration.class)
public interface FileService {

    /**删除个人文件*/
    @ResponseBody
    @RequestMapping(value = "/file/member/file/{fileId}", method = RequestMethod.DELETE)
    CommonResult deleteFile(@PathVariable(value = "fileId") String fileId) throws Exception;


    /**删除小组文件*/
    @ResponseBody
    @DeleteMapping(value = "/file/group/{groupId}/file/{fileId}")
    CommonResult deleteFileForGroup(@PathVariable(value = "fileId") String fileId,
                            @PathVariable(value = "groupId") String groupId) throws Exception;

}
