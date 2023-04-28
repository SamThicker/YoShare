package com.yo.yoshare.logservice.service.impl;

import com.yo.yoshare.common.api.CommonResult;
import com.yo.yoshare.logservice.dao.MemberReadNoteDao;
import com.yo.yoshare.logservice.service.MemberReadNoteLogService;
import com.yo.yoshare.mbg.mapper.LmsMemberReadNoteMapper;
import com.yo.yoshare.mbg.model.LmsMemberReadNoteExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MemberReadNoteLogServiceImpl implements MemberReadNoteLogService {

    @Autowired
    private LmsMemberReadNoteMapper readNoteMapper;
    @Autowired
    private MemberReadNoteDao memberReadNoteDao;

    @Override
    public CommonResult addMemberReadNoteLog(Long userId, String id, String noteType, Long groupId, String title) {
        java.sql.Date transDate = new java.sql.Date(System.currentTimeMillis());
        memberReadNoteDao.addMemberReadRecord(userId, transDate, id, noteType, groupId, title);
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

    @Override
    public CommonResult queryMemberReadNoteDetail(Long userId, Date date) {
        LmsMemberReadNoteExample example = new LmsMemberReadNoteExample();
        example.createCriteria().andDateEqualTo(date).andMemberIdEqualTo(userId);
        return  CommonResult.success(readNoteMapper.selectByExample(example));
    }

}
