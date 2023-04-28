package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsMemberFile;
import com.yo.yoshare.mbg.model.CmsMemberFileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsMemberFileMapper {
    int countByExample(CmsMemberFileExample example);

    int deleteByExample(CmsMemberFileExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsMemberFile record);

    int insertSelective(CmsMemberFile record);

    List<CmsMemberFile> selectByExample(CmsMemberFileExample example);

    CmsMemberFile selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsMemberFile record, @Param("example") CmsMemberFileExample example);

    int updateByExample(@Param("record") CmsMemberFile record, @Param("example") CmsMemberFileExample example);

    int updateByPrimaryKeySelective(CmsMemberFile record);

    int updateByPrimaryKey(CmsMemberFile record);
}