SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` varchar(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(100) NOT NULL COMMENT '用户姓名',
  `password` varchar(20) NOT NULL COMMENT '用户密码',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `role`   varchar(20) NOT NULL COMMENT '用户角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------

DROP TABLE IF EXISTS `user_crawl_record`;
CREATE TABLE `user_crawl_record` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `system_code` int(10) NOT NULL COMMENT '游戏id',
  `game_code` int(10) NOT NULL COMMENT '游戏id',
  `crawl_count` int(10) DEFAULT NULL COMMENT '爬取条数',
  `create_time` datetime DEFAULT NULL COMMENT '爬取时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_crawl_record`(`user_id`, `system_code`, `game_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户爬取记录表';


DROP TABLE IF EXISTS `crawl_comment_xiaomi`;
CREATE TABLE `crawl_comment_xiaomi` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `game_code` int(10) NOT NULL COMMENT '游戏id',
  `sort` int(10) NOT NULL COMMENT '序号',
  `viewpoint_id` varchar(30) DEFAULT NULL COMMENT '评论id',
  `uuid` int(30) DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(100) DEFAULT NULL COMMENT '用户名',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别:0-女；1-男；2-未知',
  `score` int(10) DEFAULT NULL COMMENT '评分',
  `content` text DEFAULT NULL COMMENT '评论内容',
  `like_count` int(10) DEFAULT NULL COMMENT '点赞数',
  `reply_count` int(10) DEFAULT NULL COMMENT '回复数',
  `view_count` int(10) DEFAULT NULL COMMENT '浏览数',
  `create_time` datetime DEFAULT NULL COMMENT '发表时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `top_reply` text DEFAULT NULL COMMENT '置顶回复',
  `play_duration` int(20) DEFAULT NULL COMMENT '游戏时长：单位毫秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_xiaomi`(`user_id`, `game_code`, `sort`),
  KEY `idx_comment_xiaomi`(`viewpoint_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='小米游戏中心评论数据表';
