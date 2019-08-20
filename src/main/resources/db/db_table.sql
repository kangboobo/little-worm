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
  `user_id` int(20) NOT NULL COMMENT '用户id',
  `sort` int(10) NOT NULL COMMENT '序号',
  PRIMARY KEY (`id`),
  UNION KEY uk_comment_xiaomi(`user_id`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='小米游戏论坛爬取评论表';