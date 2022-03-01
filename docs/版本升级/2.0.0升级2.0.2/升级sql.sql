-- ----------------------------
-- Table structure for tb_log_error
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_error`;
create table tb_log_error(
  id varchar(32) not null COMMENT '主建',
  server_name varchar(50) not null comment '服务名称',
  server_ip varchar(20) default null comment '服务器ip',
  server_host varchar(30) default null comment '服务器名',
  env varchar(8) default null comment '环境',
  url varchar(50) default null comment '请求接口',
  method varchar(8) default null comment '操作方式',
  method_class varchar(100) default null comment '方法类',
  method_name varchar(50) default null comment '方法名',
  param text default null comment '请求参数',
  oper_ip varchar(20) default null comment '操作IP地址',
  oper_name varchar(50) default null comment '操作人员',
  oper_user_type tinyint(1) default null comment '操作人员类型，是系统用户还是业务用户 0系统1业务2白名单',
  client_code varchar(20) default null comment '操作客户端',
  error text default null comment '错误信息',
  line_number int(4) default null comment '报错行号',
  exception_name varchar(258) default null comment '异常名称',
  message text default null comment '异常信息',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) DEFAULT NULL comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错误日志表';

-- ----------------------------
-- Table structure for tb_log_operate
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_operate`;
create table tb_log_operate(
  id varchar(32) not null COMMENT '主建',
  server_name varchar(50) not null comment '服务名称',
  server_ip varchar(20) default null comment '服务器ip',
  server_host varchar(30) default null comment '服务器名',
  env varchar(8) default null comment '环境',
  url varchar(50) default null comment '请求接口',
  method varchar(8) default null comment '操作方式',
  method_class varchar(100) default null comment '方法类',
  method_name varchar(50) default null comment '方法名',
  param text default null comment '请求参数',
  oper_ip varchar(20) default null comment '操作IP地址',
  oper_name varchar(50) default null comment '操作人员',
  oper_user_type tinyint(1) default null comment '操作人员类型，是系统用户还是业务用户 0系统1业务2白名单',
  client_code varchar(20) default null comment '操作客户端',
  title varchar(50) default null comment '操作标题',
  business_type varchar(10) default null comment '业务类型（OTHER=其它,INSERT=新增,UPDATE=修改,DELETE=删除,GRANT=授权,EXPORT=导出,IMPORT=导入,FORCE=强退,GENCODE=生成代码,CLEAN=清空数据,REVIEW=审核）',
  return_content text default null comment '返回内容',
  error_msg text default null comment '错误消息',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) DEFAULT NULL comment '创建部门',
  status   tinyint(1) default 1 comment '操作状态（0正常 1异常）',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';


-- ----------------------------
-- update data for tb_core_dict_type
-- ----------------------------
update tb_core_dict_type set dict_type_name = '基础-日志监控字典' where dict_type_code = 'RESTFUL_MONITOR';

-- ----------------------------
-- add data for tb_core_dict_type
-- ----------------------------
INSERT INTO `tb_core_dict_type`(`id`, `dict_type_code`, `dict_type_name`, `note`, `del_enabled`, `sort_num`, `parent_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `tenant_code`, `is_tree`, `create_org`)
VALUES ('a5c94db5490032brty39e64b637e408', 'BUSINESS_TYPE', '操作日志业务类型', NULL, 'N', 2, 'a5c94db5490032b8e0f39e64b637e408', '1', sysdate(), '1', sysdate(), 1, 0, '000000', 0, '');
INSERT INTO `tb_core_dict_type`(`id`, `dict_type_code`, `dict_type_name`, `note`, `del_enabled`, `sort_num`, `parent_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `tenant_code`, `is_tree`, `create_org`)
VALUES ('a5c94db5ghda32brty39e64b637e408', 'OPERATE_STATUS', '操作结果状态', NULL, 'N', 3, 'a5c94db5490032b8e0f39e64b637e408', '1', sysdate(), '1', sysdate(), 1, 0, '000000', 0, '');

-- ----------------------------
-- add data for tb_core_dict
-- ----------------------------
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('17062df17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'OTHER', '其它', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('17062rf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'INSERT', '新增', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('1706fgf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'UPDATE', '修改', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('1706yuf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'DELETE', '删除', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('1706vbf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'GRANT', '授权', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('1706asf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'EXPORT', '导出', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('1706zxf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'IMPORT', '导入', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('1706q3f17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'FORCE', '强退', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('17064ff17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'GENCODE', '生成代码', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('170626gf17d9jklx10a8de9a4347f5b', 'BUSINESS_TYPE', 'CLEAN', '清空数据', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('170626gf1747hklx10a8de9a4347f5b', 'BUSINESS_TYPE', 'REVIEW', '审核', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');

INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('170626gf17d9yulx10a8de9a4347f5b', 'OPERATE_STATUS', '0', '正常', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');
INSERT INTO `tb_core_dict`(`id`, `dict_type_code`, `code`, `name`, `parent_id`, `locale_id`, `note`, `sort_num`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `dict_level`, `pcode`, `tenant_code`, `create_org`)
VALUES ('170626gfu7d9jklx10a8de9a4347f5b', 'OPERATE_STATUS', '1', '异常', '-1', 'zh_cn', '', 0, '1', sysdate(), '1', sysdate(), 1, 0, NULL, '-1', '000000', '');


-- ----------------------------
-- add data for tb_core_function
-- ----------------------------
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `target`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('30b4baffc04b6260fgtb79a25f307462', '系统日志', '日志', 'LOG', '-1', '/log', 1, 'iconfont icon-caidanguanli', '_self', 5, '', '', '', NULL, '1', sysdate(), '1', sysdate(), 1, 0, '');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `target`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('30b4baffc04b4850fgtb79a25f307462', '错误日志', '错误日志', 'ERROR_LOG', '30b4baffc04b6260fgtb79a25f307462', '/log/error', 1, 'iconfont iconicon_doc', '_self', 5, '', '', '', NULL, '1', sysdate(), '1', sysdate(), 1, 0, '');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `target`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('30b4baffc04b6260fgtb79a26f307462', '操作日志', '操作日志', 'OPERATE_LOG', '30b4baffc04b6260fgtb79a25f307462', '/log/operate', 1, 'iconfont iconicon_compile', '_self', 5, '', '', '', NULL, '1', sysdate(), '1', sysdate(), 1, 0, '');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `target`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('30b4baffc0487960fgtb79a26f307462', '列表', '列表', 'OPERATE_LOG_LIST', '30b4baffc04b6260fgtb79a26f307462', '/log/operate/list', 0, null, '_self', 1, '', '', '', NULL, '1', sysdate(), '1', sysdate(), 1, 0, '');
INSERT INTO `tb_core_function`(`id`, `function_name`, `alias`, `code`, `parent_id`, `url`, `is_menu`, `icon`, `target`, `sort`, `remark`, `moude_summary`, `operate_instruction`, `function_level`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('30b4baffc0yjm960fgtb79a26f307462', '列表', '列表', 'ERROR_LOG_LIST', '30b4baffc04b4850fgtb79a25f307462', '/log/error/list', 0, null, '_self', 1, '', '', '', NULL, '1', sysdate(), '1', sysdate(), 1, 0, '');

-- ----------------------------
-- update data for tb_core_function
-- ----------------------------
update tb_core_function set url = '/log/monitor/result' where code = 'MONITOR_RESULT';
update tb_core_function set url = '/log/monitor/setting' where code = 'MONITOR_SETTING';

-- ----------------------------
-- add data for tb_core_role_function
-- ----------------------------
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('0477329c9f6c46a593985th8cf4bbb53', '1', '30b4baffc04b6260fgtb79a25f307462', '1', sysdate(), '1', sysdate(), 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('0477329c9f6c46a593985tujmf4bbb53', '1', '30b4baffc04b4850fgtb79a25f307462', '1', sysdate(), '1', sysdate(), 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('0477329c9f6c5tf593985th8cf4bbb53', '1', '30b4baffc04b6260fgtb79a26f307462', '1', sysdate(), '1', sysdate(), 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('0477329c9f6c5tdfv3985th8cf4bbb53', '1', '30b4baffc0487960fgtb79a26f307462', '1', sysdate(), '1', sysdate(), 1, 0, '');
INSERT INTO `tb_core_role_function`(`id`, `role_id`, `function_id`, `create_user`, `create_time`, `update_user`, `update_time`, `status`, `is_deleted`, `create_org`)
VALUES ('0477329c9f6c5tghn3985th8cf4bbb53', '1', '30b4baffc0yjm960fgtb79a26f307462', '1', sysdate(), '1', sysdate(), 1, 0, '');
