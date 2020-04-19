package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsMemberFavoritePage;
import com.yo.yoshare.mbg.model.CmsMemberFavoritePageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsMemberFavoritePageMapper {
    int countByExample(CmsMemberFavoritePageExample example);

    int deleteByExample(CmsMemberFavoritePageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsMemberFavoritePage record);

    int insertSelective(CmsMemberFavoritePage record);

    List<CmsMemberFavoritePage> selectByExample(CmsMemberFavoritePageExample example);

    CmsMemberFavoritePage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsMemberFavoritePage record, @Param("example") CmsMemberFavoritePageExample example);

    int updateByExample(@Param("record") CmsMemberFavoritePage record, @Param("example") CmsMemberFavoritePageExample example);

    int updateByPrimaryKeySelective(CmsMemberFavoritePage record);

    int updateByPrimaryKey(CmsMemberFavoritePage record);
}