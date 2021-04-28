-- ----------------------------
-- Table structure for tb_log_monitor_result
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_result`;
create table tb_log_monitor_result(
  id varchar(32) not null COMMENT '主建',
  name text not null comment '服务名称',
  path text not null comment '测试地址',
  tags varchar(258) default null COMMENT '分组',
  url text default null comment '请求接口',
  method varchar(10) default null comment '请求方式',
  error varchar(289) default null comment '请求错误',
  respose text default null comment '响应数据',
  respose_code int(10) default null comment '响应编码',
  restful_response longtext default null comment '接口返回数据',
  header text default null comment 'header参数',
  body text default null comment 'body参数',
  is_success int(1) default null comment '是否成功 0否 1是',
  response_time int(10) default null comment '执行时长 单位毫秒',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) DEFAULT NULL comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口监控详情';

-- ----------------------------
-- Table structure for tb_log_monitor_setting
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_setting`;
create table tb_log_monitor_setting(
  id varchar(32) not null COMMENT '主建',
  server varchar(50) not null comment '服务名称',
  path varchar(30) default null comment '监控地址',
  tag varchar(258) default null COMMENT '分组',
  method varchar(10) default null comment '请求方式',
  is_monitor int(2) default 3 comment '是否监控 0:否 1:是 3:未设置',
  code varchar(258) default '200' comment 'respose正确code,多个逗号分割',
  exec_js varchar(258) default null comment 'js代码',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) DEFAULT NULL comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口监控设置表';

-- ----------------------------
-- Table structure for tb_log_monitor_setting_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_param`;
create table tb_log_monitor_param(
  id varchar(32) not null COMMENT '主建',
  server varchar(50) not null comment '服务名称',
  path varchar(30) default null comment '监控地址',
  method varchar(10) default null comment '请求方式',
  type varchar(10) default null comment '参数类型 字典 PARAM_TYPE（header、path、body、query）',
  name varchar(20) default null comment '参数名称',
  value varchar(258) default null comment '参数值',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) DEFAULT NULL comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口监控参数表';


-- ----------------------------
-- add data for tb_core_dict_type
-- ----------------------------
INSERT INTO `tb_core_dict_type`(`id`, `dict_type_code`, `dict_type_name`, `note`, `del_enabled`, `sort_num`, `parent_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `tenant_code`, `is_tree`, `create_org`) VALUES ('a5c94db5490032b8e0f39e64b637e408', 'RESTFUL_MONITOR', '基础-接口监控字典', '', 'Y', 1, '-1', '1', '2021-04-27 17:14:46', '1', '2021-04-27 17:14:46', 1, 0, '000000', 0, '');
INSERT INTO `tb_core_dict_type`(`id`, `dict_type_code`, `dict_type_name`, `note`, `del_enabled`, `sort_num`, `parent_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `tenant_code`, `is_tree`, `create_org`) VALUES ('52cd701b216e592806acbefe0d75bb05', 'PARAM_TYPE', '参数类型', '', 'Y', 1, 'a5c94db5490032b8e0f39e64b637e408', '1', '2021-04-27 17:15:15', '1', '2021-04-27 17:15:15', 1, 0, '000000', 0, '');

-- ----------------------------
-- add data for tb_core_dict
-- ----------------------------
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`) VALUES ('17062df17d9da95103a8de9a4347fd5b', 'PARAM_TYPE', 'header', 'header', '-1', 'zh_cn', '', 0, '1', '2021-04-27 17:15:36', '1', '2021-04-27 17:15:36', 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`) VALUES ('3a54a00ea3f1b721a88fd40dfd4050d1', 'PARAM_TYPE', 'path', 'path', '-1', 'zh_cn', '', 1, '1', '2021-04-27 17:15:46', '1', '2021-04-27 17:15:46', 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`) VALUES ('abc302d0db1b1d1569e9992be60695e8', 'PARAM_TYPE', 'body', 'body', '-1', 'zh_cn', '', 3, '1', '2021-04-27 17:15:58', '1', '2021-04-27 17:15:58', 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`) VALUES ('cdd0bf652c365ff8b54eaed2763fa6de', 'PARAM_TYPE', 'query', 'query', '-1', 'zh_cn', '', 4, '1', '2021-04-27 17:16:08', '1', '2021-04-27 17:16:08', 1, 0, NULL, '-1', '000000', '');

-- ----------------------------
-- add data for tb_core_function
-- ----------------------------
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('c339f73d1d78378afe7441471a70f4a9', '接口监控', '接口监控', 'MONITOR_RESTFUL', '30b4baffc04b62602bdb79a25f307462', '/monitor/restful', 1, 'iconfont iconicon_task', 0, '', '', '', NULL, '1', '2021-04-25 02:32:27', '1', '2021-04-25 22:53:09', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('2cc59e0ef237772faa8879101e6a09d0', '监控设置', '监控设置', 'MONITOR_SETTING', 'c339f73d1d78378afe7441471a70f4a9', '/monitor/restful/setting', 1, 'iconfont icon-canshu', 1, '', '主要用来设置接口监控时得参数', '', NULL, '1', '2021-04-25 02:38:03', '1', '2021-04-25 22:52:56', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('6c3dd5dc10fd974c1674c5b23728c6ec', '监控结果', '监控结果', 'MONITOR_RESULT', 'c339f73d1d78378afe7441471a70f4a9', '/monitor/restful/result', 1, 'iconfont icon-debug', 0, '', '用来查询接口监控得结果，包括第三方服务得监控结果', '', NULL, '1', '2021-04-25 02:35:22', '1', '2021-04-25 22:52:22', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('08ee84e22f62f64751c814724733d036', '保存参数', '保存接口参数', 'MONITOR_SAVE_PARAMS', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/save-param', 0, '', 6, '', '', '', NULL, '1', '2021-04-25 23:21:39', '1', '2021-04-28 11:34:12', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('369d276d9a5596e121575b4a43354e42', '获取参数', '获取接口参数', 'MONITOR_PARAMS', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/param', 0, '', 5, '', '', '', NULL, '1', '2021-04-25 23:20:31', '1', '2021-04-25 23:20:31', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('57695c1201b3ab34dc9dd7fc36a85f9e', '树形列表', '树形列表', 'MONITOR_TREE', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/monitors', 0, '', 1, '', '', '', NULL, '1', '2021-04-25 23:13:19', '1', '2021-04-25 23:13:19', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('5dcf4877b534939299853912dbee23f2', '删除设置', '删除接口设置', 'MONITOR_DELETE_SETUP', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/delete-setup', 0, '', 4, '', '', '', NULL, '1', '2021-04-25 23:18:16', '1', '2021-04-25 23:18:16', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('b2511ed8cd3aea90554ce7540a8cd489', '获取设置', '获取接口设置', 'MONITOR_SETUP', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/setup', 0, '', 2, '', '', '', NULL, '1', '2021-04-25 23:14:35', '1', '2021-04-25 23:14:35', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('b837798c7836577f84dad0c6d3a7f84f', '保存设置', '保存接口设置', 'MONITOR_SAVE_SETUP', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/save-setup', 0, '', 3, '', '', '', NULL, '1', '2021-04-25 23:16:59', '1', '2021-04-25 23:16:59', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('3858a8f4ec9c70e0b92f00106b388041', '服务列表', '服务列表', 'MONITOR_SERVERS', '6c3dd5dc10fd974c1674c5b23728c6ec', '/monitor/setting/servers', 0, '', 3, '', '', '', NULL, '1', '2021-04-25 23:11:20', '1', '2021-04-25 23:11:20', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('7ca3b1ed7fd373a04f1aad228db7ab9f', '分组列表', '分组列表', 'MONITOR_TAGS', '6c3dd5dc10fd974c1674c5b23728c6ec', '/monitor/setting/tags', 0, '', 4, '', '', '', NULL, '1', '2021-04-25 23:12:04', '1', '2021-04-25 23:12:04', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('de3fe5ad0490d0245bbe38057eff114a', '列表', '列表', 'MONITOR_RESULTS_LIST', '6c3dd5dc10fd974c1674c5b23728c6ec', '/monitor/log/list', 0, '', 1, '', '', '', NULL, '1', '2021-04-25 23:07:56', '1', '2021-04-25 23:07:56', 1, 0, '', '_self');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`, `target`) VALUES ('e04922d67ebf2fbb44ac0e4e1b6c8774', '导出', '导出', 'MONITOR_RESULTS_EXPORT', '6c3dd5dc10fd974c1674c5b23728c6ec', '/monitor/log/export', 0, '', 1, '', '', '', NULL, '1', '2021-04-25 23:09:50', '1', '2021-04-25 23:09:50', 1, 0, '', '_self');

-- ----------------------------
-- add data for tb_core_role_function
-- ----------------------------
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('02c9fc97fc9b69a99772de3133fb9782', '1', '5dcf4877b534939299853912dbee23f2', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('132940913d76d14a5542bad020b6744e', '1', '08ee84e22f62f64751c814724733d036', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('151e45d595a0cb245172db90545de2c3', '1', '3858a8f4ec9c70e0b92f00106b388041', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('24d2bcb22059136309ee6f3d2ceb780d', '1', '2cc59e0ef237772faa8879101e6a09d0', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('3889ee4266e82509e5db6b197d5ef6c5', '1', 'b2511ed8cd3aea90554ce7540a8cd489', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('40efb7441b6ad2fc833351a0ce5cce79', '1', '7ca3b1ed7fd373a04f1aad228db7ab9f', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('5a38b7d90e2256d76ba7761dee8bb419', '1', 'e04922d67ebf2fbb44ac0e4e1b6c8774', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('5eb1cf712775740a693e128ce0c8a9b6', '1', 'b837798c7836577f84dad0c6d3a7f84f', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('b4251d4e6998b898f15138b418c1a9c3', '1', '57695c1201b3ab34dc9dd7fc36a85f9e', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('c11290eccb56da347548404761ff8cc7', '1', '369d276d9a5596e121575b4a43354e42', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('ce2167a1f7712e367f391bf3bf7ea8bd', '1', '6c3dd5dc10fd974c1674c5b23728c6ec', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('d7dad5e783eef08cafb49515da7ba848', '1', 'de3fe5ad0490d0245bbe38057eff114a', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`) VALUES ('ea32404a2aff83fb8787c2ec8c83acaf', '1', 'c339f73d1d78378afe7441471a70f4a9', '1', '2021-04-25 23:23:10', '1', '2021-04-25 23:23:10', 1, 0, '');

