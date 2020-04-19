package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsFileSeq;
import com.yo.yoshare.mbg.model.CmsFileSeqExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsFileSeqMapper {
    int countByExample(CmsFileSeqExample example);

    int deleteByExample(CmsFileSeqExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsFileSeq record);

    int insertSelective(CmsFileSeq record);

    List<CmsFileSeq> selectByExample(CmsFileSeqExample example);

    CmsFileSeq selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsFileSeq record, @Param("example") CmsFileSeqExample example);

    int updateByExample(@Param("record") CmsFileSeq record, @Param("example") CmsFileSeqExample example);

    int updateByPrimaryKeySelective(CmsFileSeq record);

    int updateByPrimaryKey(CmsFileSeq record);
}