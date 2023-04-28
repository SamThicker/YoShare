package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsFileTransInfo;
import com.yo.yoshare.mbg.model.CmsFileTransInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsFileTransInfoMapper {
    int countByExample(CmsFileTransInfoExample example);

    int deleteByExample(CmsFileTransInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsFileTransInfo record);

    int insertSelective(CmsFileTransInfo record);

    List<CmsFileTransInfo> selectByExample(CmsFileTransInfoExample example);

    CmsFileTransInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsFileTransInfo record, @Param("example") CmsFileTransInfoExample example);

    int updateByExample(@Param("record") CmsFileTransInfo record, @Param("example") CmsFileTransInfoExample example);

    int updateByPrimaryKeySelective(CmsFileTransInfo record);

    int updateByPrimaryKey(CmsFileTransInfo record);
}