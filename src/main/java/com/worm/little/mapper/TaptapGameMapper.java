package com.worm.little.mapper;

import com.worm.little.entity.TaptapGame;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaptapGameMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaptapGame record);

    TaptapGame selectByPrimaryKey(Long id);

    List<TaptapGame> selectAll();

    int updateByPrimaryKey(TaptapGame record);

    /**
     * 批量新增
     *
     * @param recordList
     */
    void batchInsert(@Param("list") List<TaptapGame> recordList);

    /**
     * 获取存储的最新游戏
     *
     * @param param
     * @return
     */
    TaptapGame getLastGame(Map param);

    /**
     * 获取游戏
     *
     * @param param
     * @return
     */
    List<TaptapGame> getGameList(Map param);
}