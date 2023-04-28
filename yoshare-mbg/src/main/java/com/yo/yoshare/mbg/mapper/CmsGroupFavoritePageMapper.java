package com.yo.yoshare.mbg.mapper;

import com.yo.yoshare.mbg.model.CmsGroupFavoritePage;
import com.yo.yoshare.mbg.model.CmsGroupFavoritePageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsGroupFavoritePageMapper {
    int countByExample(CmsGroupFavoritePageExample example);

    int deleteByExample(CmsGroupFavoritePageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsGroupFavoritePage record);

    int insertSelective(CmsGroupFavoritePage record);

    List<CmsGroupFavoritePage> selectByExample(CmsGroupFavoritePageExample example);

    CmsGroupFavoritePage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsGroupFavoritePage record, @Param("example") CmsGroupFavoritePageExample example);

    int updateByExample(@Param("record") CmsGroupFavoritePage record, @Param("example") CmsGroupFavoritePageExample example);

    int updateByPrimaryKeySelective(CmsGroupFavoritePage record);

    int updateByPrimaryKey(CmsGroupFavoritePage record);
}