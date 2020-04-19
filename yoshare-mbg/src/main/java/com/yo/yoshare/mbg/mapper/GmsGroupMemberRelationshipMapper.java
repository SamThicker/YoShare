package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.GmsGroupMemberRelationship;
import com.yo.yoshare.mbg.model.GmsGroupMemberRelationshipExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GmsGroupMemberRelationshipMapper {
    int countByExample(GmsGroupMemberRelationshipExample example);

    int deleteByExample(GmsGroupMemberRelationshipExample example);

    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("memberId") Long memberId);

    int insert(GmsGroupMemberRelationship record);

    int insertSelective(GmsGroupMemberRelationship record);

    List<GmsGroupMemberRelationship> selectByExample(GmsGroupMemberRelationshipExample example);

    GmsGroupMemberRelationship selectByPrimaryKey(@Param("groupId") Long groupId, @Param("memberId") Long memberId);

    int updateByExampleSelective(@Param("record") GmsGroupMemberRelationship record, @Param("example") GmsGroupMemberRelationshipExample example);

    int updateByExample(@Param("record") GmsGroupMemberRelationship record, @Param("example") GmsGroupMemberRelationshipExample example);

    int updateByPrimaryKeySelective(GmsGroupMemberRelationship record);

    int updateByPrimaryKey(GmsGroupMemberRelationship record);
}