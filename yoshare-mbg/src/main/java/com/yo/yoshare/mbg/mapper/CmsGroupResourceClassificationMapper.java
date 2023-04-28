package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsGroupResourceClassification;
import com.yo.yoshare.mbg.model.CmsGroupResourceClassificationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsGroupResourceClassificationMapper {
    int countByExample(CmsGroupResourceClassificationExample example);

    int deleteByExample(CmsGroupResourceClassificationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsGroupResourceClassification record);

    int insertSelective(CmsGroupResourceClassification record);

    List<CmsGroupResourceClassification> selectByExample(CmsGroupResourceClassificationExample example);

    CmsGroupResourceClassification selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsGroupResourceClassification record, @Param("example") CmsGroupResourceClassificationExample example);

    int updateByExample(@Param("record") CmsGroupResourceClassification record, @Param("example") CmsGroupResourceClassificationExample example);

    int updateByPrimaryKeySelective(CmsGroupResourceClassification record);

    int updateByPrimaryKey(CmsGroupResourceClassification record);
}