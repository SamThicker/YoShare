package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.AreaData;
import com.yo.yoshare.mbg.model.AreaDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AreaDataMapper {
    int countByExample(AreaDataExample example);

    int deleteByExample(AreaDataExample example);

    int insert(AreaData record);

    int insertSelective(AreaData record);

    List<AreaData> selectByExample(AreaDataExample example);

    int updateByExampleSelective(@Param("record") AreaData record, @Param("example") AreaDataExample example);

    int updateByExample(@Param("record") AreaData record, @Param("example") AreaDataExample example);
}