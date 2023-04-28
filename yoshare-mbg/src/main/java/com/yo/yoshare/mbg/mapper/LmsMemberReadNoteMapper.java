package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.LmsMemberReadNote;
import com.yo.yoshare.mbg.model.LmsMemberReadNoteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LmsMemberReadNoteMapper {
    int countByExample(LmsMemberReadNoteExample example);

    int deleteByExample(LmsMemberReadNoteExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LmsMemberReadNote record);

    int insertSelective(LmsMemberReadNote record);

    List<LmsMemberReadNote> selectByExample(LmsMemberReadNoteExample example);

    LmsMemberReadNote selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LmsMemberReadNote record, @Param("example") LmsMemberReadNoteExample example);

    int updateByExample(@Param("record") LmsMemberReadNote record, @Param("example") LmsMemberReadNoteExample example);

    int updateByPrimaryKeySelective(LmsMemberReadNote record);

    int updateByPrimaryKey(LmsMemberReadNote record);
}