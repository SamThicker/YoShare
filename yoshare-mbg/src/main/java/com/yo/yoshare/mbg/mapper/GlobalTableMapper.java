package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.GlobalTable;
import com.yo.yoshare.mbg.model.GlobalTableExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GlobalTableMapper {
    int countByExample(GlobalTableExample example);

    int deleteByExample(GlobalTableExample example);

    int deleteByPrimaryKey(String xid);

    int insert(GlobalTable record);

    int insertSelective(GlobalTable record);

    List<GlobalTable> selectByExample(GlobalTableExample example);

    GlobalTable selectByPrimaryKey(String xid);

    int updateByExampleSelective(@Param("record") GlobalTable record, @Param("example") GlobalTableExample example);

    int updateByExample(@Param("record") GlobalTable record, @Param("example") GlobalTableExample example);

    int updateByPrimaryKeySelective(GlobalTable record);

    int updateByPrimaryKey(GlobalTable record);
}