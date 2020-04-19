package com.yo.yoshare.logservice.dao;

import com.yo.yoshare.common.api.CommonResult;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface MemberReadNoteDao {

    int addMemberReadRecord(@Param(value = "memberId") Long memberId,
                            @Param(value = "date") Date date,
                            @Param(value = "noteId") String noteId);


    List<Map<Date, Integer>> selectMemberReadByWeek(@Param(value = "memberId") Long memberId);

    List<Map<Date, Integer>> selectMemberReadByMonth(@Param(value = "memberId") Long memberId);

    int selectMemberReadByDay(@Param(value = "memberId") Long memberId);
}
