package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.GmsAccessType;
import com.yo.yoshare.mbg.model.GmsAccessTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GmsAccessTypeMapper {
    int countByExample(GmsAccessTypeExample example);

    int deleteByExample(GmsAccessTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GmsAccessType record);

    int insertSelective(GmsAccessType record);

    List<GmsAccessType> selectByExample(GmsAccessTypeExample example);

    GmsAccessType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GmsAccessType record, @Param("example") GmsAccessTypeExample example);

    int updateByExample(@Param("record") GmsAccessType record, @Param("example") GmsAccessTypeExample example);

    int updateByPrimaryKeySelective(GmsAccessType record);

    int updateByPrimaryKey(GmsAccessType record);
}