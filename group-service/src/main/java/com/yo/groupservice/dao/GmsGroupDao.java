package com.yo.groupservice.dao;

import com.yo.yoshare.mbg.model.GmsGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GmsGroupDao {
    List<GmsGroup> selectMemberGroup(@Param("userId") Long userId);
}
