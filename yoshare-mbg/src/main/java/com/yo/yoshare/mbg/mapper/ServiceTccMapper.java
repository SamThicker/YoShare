package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.ServiceTcc;
import com.yo.yoshare.mbg.model.ServiceTccExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceTccMapper {
    int countByExample(ServiceTccExample example);

    int deleteByExample(ServiceTccExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceTcc record);

    int insertSelective(ServiceTcc record);

    List<ServiceTcc> selectByExample(ServiceTccExample example);

    ServiceTcc selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceTcc record, @Param("example") ServiceTccExample example);

    int updateByExample(@Param("record") ServiceTcc record, @Param("example") ServiceTccExample example);

    int updateByPrimaryKeySelective(ServiceTcc record);

    int updateByPrimaryKey(ServiceTcc record);
}