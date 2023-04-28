package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.BranchTable;
import com.yo.yoshare.mbg.model.BranchTableExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BranchTableMapper {
    int countByExample(BranchTableExample example);

    int deleteByExample(BranchTableExample example);

    int deleteByPrimaryKey(Long branchId);

    int insert(BranchTable record);

    int insertSelective(BranchTable record);

    List<BranchTable> selectByExample(BranchTableExample example);

    BranchTable selectByPrimaryKey(Long branchId);

    int updateByExampleSelective(@Param("record") BranchTable record, @Param("example") BranchTableExample example);

    int updateByExample(@Param("record") BranchTable record, @Param("example") BranchTableExample example);

    int updateByPrimaryKeySelective(BranchTable record);

    int updateByPrimaryKey(BranchTable record);
}