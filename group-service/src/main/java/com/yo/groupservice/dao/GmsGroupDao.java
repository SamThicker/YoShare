package com.yo.groupservice.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GmsGroupDao {
     List selectMemberGroup(@Param(value = "userId") Long userId);

     List listGroupMember(@Param(value = "groupId") Long groupId);
}
