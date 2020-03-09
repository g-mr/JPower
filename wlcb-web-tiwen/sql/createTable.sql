
drop table if exists tbl_supplies;
CREATE TABLE `tbl_supplies` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `openid` varchar(30) DEFAULT NULL COMMENT 'openid',
  `bayonet_number` int(10) DEFAULT null COMMENT '卡口号',
  `bayonet_name` varchar(50) DEFAULT NULL COMMENT '卡口名称',
  `supplies` text DEFAULT NULL COMMENT '物资分配',
  `quxian` varchar(50) DEFAULT NULL COMMENT '区县',
  `jiedao` varchar(30) DEFAULT NULL COMMENT '街道',
  `shequ` varchar(25) DEFAULT NULL COMMENT '社区',
  `xiaoqu` varchar(20) DEFAULT NULL COMMENT '小区',
  `create_user` varchar(32) DEFAULT 'root' COMMENT '作成者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成时间',
  `update_user` varchar(32) DEFAULT 'root' COMMENT '修改者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int(5) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='物资表';