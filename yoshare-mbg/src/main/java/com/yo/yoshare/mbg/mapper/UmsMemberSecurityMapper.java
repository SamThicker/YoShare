package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.UmsMemberSecurity;
import com.yo.yoshare.mbg.model.UmsMemberSecurityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberSecurityMapper {
    int countByExample(UmsMemberSecurityExample example);

    int deleteByExample(UmsMemberSecurityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberSecurity record);

    int insertSelective(UmsMemberSecurity record);

    List<UmsMemberSecurity> selectByExample(UmsMemberSecurityExample example);

    UmsMemberSecurity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsMemberSecurity record, @Param("example") UmsMemberSecurityExample example);

    int updateByExample(@Param("record") UmsMemberSecurity record, @Param("example") UmsMemberSecurityExample example);

    int updateByPrimaryKeySelective(UmsMemberSecurity record);

    int updateByPrimaryKey(UmsMemberSecurity record);
}