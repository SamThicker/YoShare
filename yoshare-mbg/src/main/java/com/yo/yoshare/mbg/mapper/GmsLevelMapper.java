package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.GmsLevel;
import com.yo.yoshare.mbg.model.GmsLevelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GmsLevelMapper {
    int countByExample(GmsLevelExample example);

    int deleteByExample(GmsLevelExample example);

    int deleteByPrimaryKey(Integer level);

    int insert(GmsLevel record);

    int insertSelective(GmsLevel record);

    List<GmsLevel> selectByExample(GmsLevelExample example);

    GmsLevel selectByPrimaryKey(Integer level);

    int updateByExampleSelective(@Param("record") GmsLevel record, @Param("example") GmsLevelExample example);

    int updateByExample(@Param("record") GmsLevel record, @Param("example") GmsLevelExample example);

    int updateByPrimaryKeySelective(GmsLevel record);

    int updateByPrimaryKey(GmsLevel record);
}