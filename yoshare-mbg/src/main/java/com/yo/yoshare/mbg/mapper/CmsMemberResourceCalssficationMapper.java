package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsMemberResourceCalssfication;
import com.yo.yoshare.mbg.model.CmsMemberResourceCalssficationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsMemberResourceCalssficationMapper {
    int countByExample(CmsMemberResourceCalssficationExample example);

    int deleteByExample(CmsMemberResourceCalssficationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsMemberResourceCalssfication record);

    int insertSelective(CmsMemberResourceCalssfication record);

    List<CmsMemberResourceCalssfication> selectByExample(CmsMemberResourceCalssficationExample example);

    CmsMemberResourceCalssfication selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsMemberResourceCalssfication record, @Param("example") CmsMemberResourceCalssficationExample example);

    int updateByExample(@Param("record") CmsMemberResourceCalssfication record, @Param("example") CmsMemberResourceCalssficationExample example);

    int updateByPrimaryKeySelective(CmsMemberResourceCalssfication record);

    int updateByPrimaryKey(CmsMemberResourceCalssfication record);
}