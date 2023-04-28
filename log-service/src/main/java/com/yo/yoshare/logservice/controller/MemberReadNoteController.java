package com.yo.yoshare.logservice.controller;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.logservice.service.MemberReadNoteLogService;
import com.yo.yoshare.mbg.mapper.LmsMemberReadNoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/log")
public class MemberReadNoteController {

    @Autowired
    private MemberReadNoteLogService memberReadNoteLogService;
    @Autowired
    private HttpServletRequest request;


    @PostMapping(value = "/readNoteLog/{id}")
    private CommonResult addReadNoteLog(@PathVariable(value = "id") Long userId,
                                     @RequestParam String noteId,
                                        @RequestParam String noteType,
                                        @RequestParam Long groupId,
                                        @RequestParam String title){
        return memberReadNoteLogService.addMemberReadNoteLog(userId, noteId, noteType, groupId, title);
    }

    @GetMapping(value = "/readNoteLog/{id}/{option}")
    private CommonResult getReadNoteLog(@PathVariable(value = "id") Long userId,
                                        @PathVariable(value = "option") String option){
        return memberReadNoteLogService.queryMemberReadNote(userId, option);
    }

    @PostMapping(value = "/readNoteLogDetails/{id}")
    private CommonResult getReadNoteLogDetails(@PathVariable(value = "id") Long userId, String dateStr) throws ParseException {
        //Date date = new Date(dateStr);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        return memberReadNoteLogService.queryMemberReadNoteDetail(userId, date);
    }





}
