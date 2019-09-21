package com.worm.little.mapper;

import com.worm.little.entity.UserCrawlRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCrawlRecordMapper {
    int insert(UserCrawlRecord record);

    List<UserCrawlRecord> selectAll();

    List<UserCrawlRecord> selectGameInfoByCode(Integer gameCode);
}