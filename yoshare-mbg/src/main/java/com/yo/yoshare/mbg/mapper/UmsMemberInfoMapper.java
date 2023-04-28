package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.UmsMemberInfo;
import com.yo.yoshare.mbg.model.UmsMemberInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberInfoMapper {
    int countByExample(UmsMemberInfoExample example);

    int deleteByExample(UmsMemberInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberInfo record);

    int insertSelective(UmsMemberInfo record);

    List<UmsMemberInfo> selectByExample(UmsMemberInfoExample example);

    UmsMemberInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsMemberInfo record, @Param("example") UmsMemberInfoExample example);

    int updateByExample(@Param("record") UmsMemberInfo record, @Param("example") UmsMemberInfoExample example);

    int updateByPrimaryKeySelective(UmsMemberInfo record);

    int updateByPrimaryKey(UmsMemberInfo record);
}