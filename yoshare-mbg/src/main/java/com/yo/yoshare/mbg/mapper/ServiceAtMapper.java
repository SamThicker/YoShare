package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.ServiceAt;
import com.yo.yoshare.mbg.model.ServiceAtExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceAtMapper {
    int countByExample(ServiceAtExample example);

    int deleteByExample(ServiceAtExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceAt record);

    int insertSelective(ServiceAt record);

    List<ServiceAt> selectByExample(ServiceAtExample example);

    ServiceAt selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceAt record, @Param("example") ServiceAtExample example);

    int updateByExample(@Param("record") ServiceAt record, @Param("example") ServiceAtExample example);

    int updateByPrimaryKeySelective(ServiceAt record);

    int updateByPrimaryKey(ServiceAt record);
}