package com.yo.yoshare.logservice.service;

import com.yo.yoshare.common.api.CommonResult;

public interface MemberReadNoteLogService {
    CommonResult addMemberReadNoteLog(Long userId, String noteId);

    CommonResult queryMemberReadNote(Long userId, String option);
}
