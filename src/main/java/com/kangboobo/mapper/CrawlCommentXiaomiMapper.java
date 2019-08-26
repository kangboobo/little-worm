package com.kangboobo.mapper;

import com.kangboobo.entity.CrawlCommentXiaomi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrawlCommentXiaomiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CrawlCommentXiaomi record);

    CrawlCommentXiaomi selectByPrimaryKey(Integer id);

    List<CrawlCommentXiaomi> selectAll();

    int updateByPrimaryKey(CrawlCommentXiaomi record);

    /**
     * 获取存储的最新评论
     *
     * @param gameCode
     * @return
     */
    public CrawlCommentXiaomi getLastComment(Integer gameCode);
}