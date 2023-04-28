package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsMemberResource;
import com.yo.yoshare.mbg.model.CmsMemberResourceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsMemberResourceMapper {
    int countByExample(CmsMemberResourceExample example);

    int deleteByExample(CmsMemberResourceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsMemberResource record);

    int insertSelective(CmsMemberResource record);

    List<CmsMemberResource> selectByExample(CmsMemberResourceExample example);

    CmsMemberResource selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsMemberResource record, @Param("example") CmsMemberResourceExample example);

    int updateByExample(@Param("record") CmsMemberResource record, @Param("example") CmsMemberResourceExample example);

    int updateByPrimaryKeySelective(CmsMemberResource record);

    int updateByPrimaryKey(CmsMemberResource record);
}