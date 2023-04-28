package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.LockTable;
import com.yo.yoshare.mbg.model.LockTableExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LockTableMapper {
    int countByExample(LockTableExample example);

    int deleteByExample(LockTableExample example);

    int deleteByPrimaryKey(String rowKey);

    int insert(LockTable record);

    int insertSelective(LockTable record);

    List<LockTable> selectByExample(LockTableExample example);

    LockTable selectByPrimaryKey(String rowKey);

    int updateByExampleSelective(@Param("record") LockTable record, @Param("example") LockTableExample example);

    int updateByExample(@Param("record") LockTable record, @Param("example") LockTableExample example);

    int updateByPrimaryKeySelective(LockTable record);

    int updateByPrimaryKey(LockTable record);
}