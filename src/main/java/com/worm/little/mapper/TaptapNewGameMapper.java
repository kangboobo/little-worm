package com.worm.little.mapper;

import com.worm.little.entity.TaptapNewGame;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaptapNewGameMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaptapNewGame record);

    TaptapNewGame selectByPrimaryKey(Long id);

    List<TaptapNewGame> selectAll();

    int updateByPrimaryKey(TaptapNewGame record);

    /**
     * 批量新增
     *
     * @param recordList
     */
    void batchInsert(@Param("list") List<TaptapNewGame> recordList);

    /**
     * 获取存储的最新游戏
     *
     * @param param
     * @return
     */
    TaptapNewGame getLastGame(Map param);

    /**
     * 获取游戏
     *
     * @param param
     * @return
     */
    List<TaptapNewGame> getGameList(Map param);
}