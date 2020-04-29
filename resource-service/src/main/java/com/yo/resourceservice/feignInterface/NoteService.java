package com.yo.resourceservice.feignInterface;

import com.yo.yoshare.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(value = "service-note")
public interface NoteService {


    /**
     * 删除特定用户特定笔记
     * */
    @RequestMapping(value = "/note/ownNote/{userId}/{noteId}", method = RequestMethod.DELETE)
    @ResponseBody
    CommonResult delNote(@PathVariable(value = "userId") String userId,
                                @PathVariable(value = "noteId") String noteId);

}
