package com.yo.yoshare.logservice.service.impl;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.logservice.dao.MemberReadNoteDao;
import com.yo.yoshare.logservice.service.MemberReadNoteLogService;
import com.yo.yoshare.mbg.mapper.LmsMemberReadNoteMapper;
import com.yo.yoshare.mbg.model.LmsMemberReadNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MemberReadNoteLogServiceImpl implements MemberReadNoteLogService {

    @Autowired
    private LmsMemberReadNoteMapper readNoteMapper;
    @Autowired
    private MemberReadNoteDao memberReadNoteDao;

    @Override
    public CommonResult addMemberReadNoteLog(Long userId, String noteId) {
        java.sql.Date transDate = new java.sql.Date(System.currentTimeMillis());
        memberReadNoteDao.addMemberReadRecord(userId, transDate, noteId);
        return CommonResult.success("已读");
    }

    @Override
    public CommonResult queryMemberReadNote(Long userId, String option) {
        Object result = null;
        switch (option){
            case "DAY": {
                result = memberReadNoteDao.selectMemberReadByDay(userId);
                break;
            }
            case "WEEK": {
                result = memberReadNoteDao.selectMemberReadByWeek(userId);
                break;
            }
            case "MONTH": {
                result = memberReadNoteDao.selectMemberReadByMonth(userId);
                break;
            }
            default: {
                //TODO
                break;
            }
        }
        return CommonResult.success(result,"成功");
    }

}
