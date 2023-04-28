package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsMemberResourceClassfication;
import com.yo.yoshare.mbg.model.CmsMemberResourceClassficationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsMemberResourceClassficationMapper {
    int countByExample(CmsMemberResourceClassficationExample example);

    int deleteByExample(CmsMemberResourceClassficationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsMemberResourceClassfication record);

    int insertSelective(CmsMemberResourceClassfication record);

    List<CmsMemberResourceClassfication> selectByExample(CmsMemberResourceClassficationExample example);

    CmsMemberResourceClassfication selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsMemberResourceClassfication record, @Param("example") CmsMemberResourceClassficationExample example);

    int updateByExample(@Param("record") CmsMemberResourceClassfication record, @Param("example") CmsMemberResourceClassficationExample example);

    int updateByPrimaryKeySelective(CmsMemberResourceClassfication record);

    int updateByPrimaryKey(CmsMemberResourceClassfication record);
}