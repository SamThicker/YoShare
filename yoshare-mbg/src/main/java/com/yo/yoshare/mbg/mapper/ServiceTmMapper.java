package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.ServiceTm;
import com.yo.yoshare.mbg.model.ServiceTmExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceTmMapper {
    int countByExample(ServiceTmExample example);

    int deleteByExample(ServiceTmExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceTm record);

    int insertSelective(ServiceTm record);

    List<ServiceTm> selectByExample(ServiceTmExample example);

    ServiceTm selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceTm record, @Param("example") ServiceTmExample example);

    int updateByExample(@Param("record") ServiceTm record, @Param("example") ServiceTmExample example);

    int updateByPrimaryKeySelective(ServiceTm record);

    int updateByPrimaryKey(ServiceTm record);
}