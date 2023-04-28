package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsGroupFile;
import com.yo.yoshare.mbg.model.CmsGroupFileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsGroupFileMapper {
    int countByExample(CmsGroupFileExample example);

    int deleteByExample(CmsGroupFileExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsGroupFile record);

    int insertSelective(CmsGroupFile record);

    List<CmsGroupFile> selectByExample(CmsGroupFileExample example);

    CmsGroupFile selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsGroupFile record, @Param("example") CmsGroupFileExample example);

    int updateByExample(@Param("record") CmsGroupFile record, @Param("example") CmsGroupFileExample example);

    int updateByPrimaryKeySelective(CmsGroupFile record);

    int updateByPrimaryKey(CmsGroupFile record);
}