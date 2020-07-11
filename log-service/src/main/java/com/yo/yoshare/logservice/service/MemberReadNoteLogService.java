package com.yo.yoshare.logservice.service;

import com.yo.yoshare.common.api.CommonResult;

import java.util.Date;

public interface MemberReadNoteLogService {
    CommonResult addMemberReadNoteLog(Long userId, String id, String noteType, Long groupId, String title);

    CommonResult queryMemberReadNote(Long userId, String option);

    CommonResult queryMemberReadNoteDetail(Long userId, Date date);
}
