package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsGroupResource;
import com.yo.yoshare.mbg.model.CmsGroupResourceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsGroupResourceMapper {
    int countByExample(CmsGroupResourceExample example);

    int deleteByExample(CmsGroupResourceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsGroupResource record);

    int insertSelective(CmsGroupResource record);

    List<CmsGroupResource> selectByExample(CmsGroupResourceExample example);

    CmsGroupResource selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsGroupResource record, @Param("example") CmsGroupResourceExample example);

    int updateByExample(@Param("record") CmsGroupResource record, @Param("example") CmsGroupResourceExample example);

    int updateByPrimaryKeySelective(CmsGroupResource record);

    int updateByPrimaryKey(CmsGroupResource record);
}