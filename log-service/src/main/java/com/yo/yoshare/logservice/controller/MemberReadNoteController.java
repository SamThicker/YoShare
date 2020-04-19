package com.yo.yoshare.logservice.controller;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.logservice.service.MemberReadNoteLogService;
import com.yo.yoshare.mbg.mapper.LmsMemberReadNoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
public class MemberReadNoteController {

    @Autowired
    private MemberReadNoteLogService memberReadNoteLogService;

    @PostMapping(value = "/readNoteLog/{id}")
    private CommonResult addReadNoteLog(@PathVariable(value = "id") Long userId,
                                     @RequestParam String noteId){
        return memberReadNoteLogService.addMemberReadNoteLog(userId, noteId);
    }

    @GetMapping(value = "/readNoteLog/{id}/{option}")
    private CommonResult getReadNoteLog(@PathVariable(value = "id") Long userId,
                                        @PathVariable(value = "option") String option){
        return memberReadNoteLogService.queryMemberReadNote(userId, option);
    }





}
