package com.worm.little.mapper;

import com.worm.little.entity.UserCrawlRecord;
import java.util.List;

public interface UserCrawlRecordMapper {
    int insert(UserCrawlRecord record);

    List<UserCrawlRecord> selectAll();
}