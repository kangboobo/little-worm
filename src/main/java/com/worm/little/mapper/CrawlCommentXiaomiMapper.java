package com.worm.little.mapper;

import com.worm.little.entity.CrawlCommentXiaomi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CrawlCommentXiaomiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CrawlCommentXiaomi record);

    CrawlCommentXiaomi selectByPrimaryKey(Integer id);

    List<CrawlCommentXiaomi> selectAll();

    int updateByPrimaryKey(CrawlCommentXiaomi record);

    /**
     * 批量新增评论数据
     *
     * @param recordList
     */
    void batchInsert(List<CrawlCommentXiaomi> recordList);

    /**
     * 获取评论
     *
     * @param param
     * @return
     */
    List<CrawlCommentXiaomi> getCommentList(Map param);

    /**
     * 获取存储的最新评论
     *
     * @param param
     * @return
     */
    CrawlCommentXiaomi getLastComment(Map param);

}