SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(20) NOT NULL COMMENT '主键',
  `user_name` varchar(100) NOT NULL COMMENT '用户名称',
  `password` varchar(20) NOT NULL COMMENT '用户密码',
  `status` varchar(20) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------

DROP TABLE IF EXISTS `crawl_comment_xiaomi`;
CREATE TABLE `crawl_comment_xiaomi` (
  `id` int(20) NOT NULL COMMENT '主键',
  `game_code` int(20) NOT NULL COMMENT '游戏id',
  `sort` int(10) NOT NULL COMMENT '序号',
  `viewpoint_id` varchar(30) DEFAULT NULL COMMENT '评论id',
  `uuid` int(30) DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(100) DEFAULT NULL COMMENT '用户名',
  `sex` int(1) DEFAULT NULL COMMENT '性别:0-女；1-男；2-未知',
  `score` int(10) DEFAULT NULL COMMENT '评分',
  `content` text DEFAULT NULL COMMENT '评论内容',
  `like_count` int(10) DEFAULT NULL COMMENT '点赞数',
  `reply_count` int(10) DEFAULT NULL COMMENT '回复数',
  `view_count` int(10) DEFAULT NULL COMMENT '浏览数',
  `create_time` date DEFAULT NULL COMMENT '发表时间',
  `update_time` date DEFAULT NULL COMMENT '更新时间',
  `top_reply` text DEFAULT NULL COMMENT '置顶回复',
  `play_duration` int(20) DEFAULT NULL COMMENT '游戏时长：单位毫秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_xiaomi`(`game_code`, `sort`),
  KEY `idx_comment_xiaomi`(`viewpoint_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='小米游戏中心评论数据表';
