
drop table if exists tbl_wechar_red;
CREATE TABLE `tbl_wechar_red` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `order_num` varchar(32) NOT NULL COMMENT '订单号',
  `openid` varchar(64) NOT NULL COMMENT '收款人openid',
  `re_user_name` varchar(20) NOT NULL COMMENT '收款人姓名',
  `price` int(10) NOT NULL COMMENT '金额 单位：分',
  `note` varchar(100) DEFAULT NULL COMMENT '付款备注',
  `ip` varchar(32) NOT NULL COMMENT '付款时ip',
  `return_code` varchar(16) NOT NULL COMMENT '付款状态码',
  `return_msg` varchar(128) NOT NULL COMMENT '返回信息',
  `result_code` varchar(16) DEFAULT NULL COMMENT '付款结果',
  `err_code` varchar(32) DEFAULT NULL COMMENT '错误代码',
  `err_code_des` varchar(128) DEFAULT NULL COMMENT '错误代码描述',
  `wx_payment_no` varchar(64) DEFAULT NULL COMMENT '微信付款单号',
  `wx_payment_time` datetime DEFAULT NULL COMMENT '微信付款成功时间',
  `pay_type` int(1) DEFAULT NULL COMMENT '支付类型  1：企业付款到零钱 2：现金红包',
  `create_user` varchar(32) DEFAULT 'root' COMMENT '作成者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成时间',
  `update_user` varchar(32) DEFAULT 'root' COMMENT '修改者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `order_num_index` (`order_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='微信红包记录表';