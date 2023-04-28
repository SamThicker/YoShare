package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.GmsGroup;
import com.yo.yoshare.mbg.model.GmsGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GmsGroupMapper {
    int countByExample(GmsGroupExample example);

    int deleteByExample(GmsGroupExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GmsGroup record);

    int insertSelective(GmsGroup record);

    List<GmsGroup> selectByExample(GmsGroupExample example);

    GmsGroup selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GmsGroup record, @Param("example") GmsGroupExample example);

    int updateByExample(@Param("record") GmsGroup record, @Param("example") GmsGroupExample example);

    int updateByPrimaryKeySelective(GmsGroup record);

    int updateByPrimaryKey(GmsGroup record);
}