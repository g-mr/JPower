/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Schema         : jpower

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 05/03/2021 16:13:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_core_city
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_city`;
CREATE TABLE `tb_core_city` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(100) NOT NULL COMMENT '城市编码',
  `pcode` varchar(100) NOT NULL COMMENT '上级编码',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `fullname` varchar(150) DEFAULT NULL COMMENT '全称',
  `rankd` int(6) DEFAULT NULL COMMENT '级别 1：省份/直辖市 2：地市 3：区县 4：乡镇/街道 5：村委',
  `lng` double(10,6) DEFAULT NULL COMMENT '经度',
  `lat` double(10,6) DEFAULT NULL COMMENT '维度',
  `country_code` varchar(100) DEFAULT NULL COMMENT '国家编码',
  `city_type` varchar(10) DEFAULT NULL COMMENT '城市类型1：首都、2：直辖市、3：地级市、4县级市、9：其他',
  `note` varchar(100) DEFAULT NULL COMMENT '备注',
  `sort_num` int(6) DEFAULT '0' COMMENT '排序',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='城市地区表';

-- ----------------------------
-- Table structure for tb_core_client
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_client`;
CREATE TABLE `tb_core_client` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(128) DEFAULT NULL COMMENT '客户端名称',
  `client_code` varchar(31) NOT NULL COMMENT '客户端CODE，唯一约束',
  `client_secret` varchar(31) NOT NULL COMMENT '客户端密钥',
  `access_token_validity` int(10) NOT NULL DEFAULT '0' COMMENT 'token过期时间，单位秒',
  `refresh_token_validity` int(10) NOT NULL DEFAULT '0' COMMENT '刷新token时间，单位秒，时间应该比token过期时间更长',
  `sort_num` int(6) DEFAULT NULL COMMENT '排序',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_client_code` (`client_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户端系统表';

-- ----------------------------
-- Table structure for tb_core_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_data_scope`;
CREATE TABLE `tb_core_data_scope` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `menu_id` varchar(32) DEFAULT NULL COMMENT '菜单主键',
  `scope_code` varchar(255) DEFAULT NULL COMMENT '编号',
  `scope_name` varchar(255) DEFAULT NULL COMMENT '数据权限名称',
  `scope_field` varchar(255) DEFAULT NULL COMMENT '数据权限字段',
  `scope_class` varchar(500) DEFAULT NULL COMMENT '数据权限类名',
  `scope_column` varchar(255) DEFAULT '*' COMMENT '数据可见字段',
  `scope_type` tinyint(1) DEFAULT NULL COMMENT '数据权限类型 字典：DATA_SCOPE_TYPE',
  `scope_value` varchar(2000) DEFAULT NULL COMMENT '数据权限值域',
  `all_role` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否所有角色都执行',
  `note` varchar(255) DEFAULT NULL COMMENT '数据权限备注',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_scope_code` (`scope_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据权限表';

-- ----------------------------
-- Table structure for tb_core_dict
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_dict`;
CREATE TABLE `tb_core_dict` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `dict_type_code` varchar(50) NOT NULL COMMENT '字典类型代码',
  `code` varchar(100) NOT NULL COMMENT '字典代码',
  `name` varchar(250) NOT NULL COMMENT '字典名称',
  `parent_id` varchar(32) DEFAULT '-1' COMMENT '上级ID',
  `locale_id` varchar(20) NOT NULL DEFAULT 'zh_cn' COMMENT '语言 zh_cn en_us',
  `note` varchar(100) DEFAULT NULL COMMENT '备注',
  `sort_num` int(6) DEFAULT '0' COMMENT '排序',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `dict_level` int(6) DEFAULT NULL COMMENT '树形字典结构的级别',
  `pcode` varchar(100) DEFAULT '-1' COMMENT '上级代码',
  `tenant_code` varchar(6) NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- ----------------------------
-- Table structure for tb_core_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_dict_type`;
CREATE TABLE `tb_core_dict_type` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `dict_type_code` varchar(50) NOT NULL COMMENT '字典类型代码',
  `dict_type_name` varchar(100) NOT NULL COMMENT '字典类型名称',
  `note` varchar(100) DEFAULT NULL COMMENT '描述',
  `del_enabled` char(1) NOT NULL DEFAULT 'N' COMMENT '是否允许删除 N:不允许 Y允许',
  `sort_num` int(6) DEFAULT '0' COMMENT '排序',
  `parent_id` varchar(32) DEFAULT '-1' COMMENT '父字典类型ID',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `is_tree` tinyint(1) DEFAULT NULL COMMENT '是否树形结构 字典YN01',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`dict_type_code`,`tenant_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ----------------------------
-- Table structure for tb_core_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_file`;
CREATE TABLE `tb_core_file` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(127) DEFAULT NULL COMMENT '文件名称',
  `file_size` int(11) DEFAULT NULL COMMENT '文件大小 单位：字节',
  `file_type` varchar(31) DEFAULT NULL COMMENT '文件类型',
  `path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `content` longblob COMMENT '文件内容',
  `mark` varchar(156) DEFAULT NULL COMMENT '文件标识',
  `storage_type` varchar(20) DEFAULT 'SERVER' COMMENT '存储类型 字典FILE_STORAGE_TYPE',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- ----------------------------
-- Table structure for tb_core_function
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_function`;
CREATE TABLE `tb_core_function` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `function_name` varchar(50) NOT NULL COMMENT '名称',
  `alias` varchar(50) DEFAULT NULL COMMENT '别名',
  `code` varchar(64) NOT NULL COMMENT '功能编码',
  `parent_id` varchar(32) DEFAULT '-1' COMMENT '父级ID',
  `url` varchar(100) DEFAULT NULL COMMENT '资源URL',
  `is_menu` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否菜单 0：否 1：是',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `target` varchar(10) DEFAULT '_self' COMMENT '打开方式',
  `sort` int(6) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `moude_summary` text COMMENT '模块概述',
  `operate_instruction` text COMMENT '操作说明',
  `function_level` int(2) DEFAULT NULL COMMENT '菜单级别',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='功能菜单表';

-- ----------------------------
-- Table structure for tb_core_org
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_org`;
CREATE TABLE `tb_core_org` (
  `id` varchar(32) NOT NULL,
  `code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  `name` varchar(100) NOT NULL COMMENT '组织机构名称',
  `parent_id` varchar(32) DEFAULT '-1' COMMENT '父级ID',
  `ancestor_id` text COMMENT '祖级ID',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int(6) DEFAULT '0' COMMENT '排序',
  `head_name` varchar(30) DEFAULT NULL COMMENT '负责人姓名',
  `head_phone` varchar(11) DEFAULT NULL COMMENT '负责人电话',
  `head_email` varchar(30) DEFAULT NULL COMMENT '负责人邮箱',
  `contact_name` varchar(30) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(11) DEFAULT NULL COMMENT '联系人电话',
  `contact_email` varchar(30) DEFAULT NULL COMMENT '联系人邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `is_virtual` tinyint(1) DEFAULT '0' COMMENT '是否虚拟机构 0:否 1：是',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- Table structure for tb_core_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_param`;
CREATE TABLE `tb_core_param` (
  `id` varchar(32) NOT NULL COMMENT '主建',
  `code` varchar(100) NOT NULL COMMENT '参数code',
  `name` varchar(100) DEFAULT NULL COMMENT '参数名称',
  `value` text NOT NULL COMMENT '参数值',
  `note` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `is_effect` tinyint(1) DEFAULT '1' COMMENT '是否支持立即生效 0否 1是',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';

-- ----------------------------
-- Table structure for tb_core_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role`;
CREATE TABLE `tb_core_role` (
  `id` varchar(32) NOT NULL COMMENT '主建',
  `alias` varchar(30) DEFAULT NULL COMMENT '角色别名',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '上级ID',
  `icon_url` varchar(100) DEFAULT NULL COMMENT '图标地址',
  `is_sys_role` tinyint(1) DEFAULT '1' COMMENT '是否系统角色 0:否 1:是',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Table structure for tb_core_role_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_data`;
CREATE TABLE `tb_core_role_data` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `role_id` varchar(50) NOT NULL COMMENT '角色ID',
  `data_id` varchar(100) NOT NULL COMMENT '数据权限ID',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`data_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色数据权限关联表';

-- ----------------------------
-- Table structure for tb_core_role_function
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_function`;
CREATE TABLE `tb_core_role_function` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `role_id` varchar(50) NOT NULL COMMENT '角色ID',
  `function_id` varchar(100) NOT NULL COMMENT '菜单ID',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`function_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单表';

-- ----------------------------
-- Table structure for tb_core_tenant
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_tenant`;
CREATE TABLE `tb_core_tenant` (
  `id` varchar(32) NOT NULL COMMENT '主建',
  `tenant_code` varchar(6) NOT NULL COMMENT '租户编码',
  `tenant_name` varchar(50) NOT NULL COMMENT '租户名称',
  `domain` varchar(40) DEFAULT NULL COMMENT '域名',
  `logo` varchar(100) DEFAULT NULL COMMENT '租户Logo',
  `contact_name` varchar(20) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(11) DEFAULT NULL COMMENT '联系人电话',
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `account_number` int(8) DEFAULT '-1' COMMENT '账号额度',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `license_key` varchar(512) DEFAULT NULL COMMENT '授权码',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表';

-- ----------------------------
-- Table structure for tb_core_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_user`;
CREATE TABLE `tb_core_user` (
  `id` varchar(32) NOT NULL COMMENT '主建',
  `org_id` varchar(32) DEFAULT NULL COMMENT '组织机构主键',
  `login_id` varchar(50) NOT NULL COMMENT '登录用户名',
  `password` varchar(50) NOT NULL COMMENT '登录密码',
  `user_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `id_type` tinyint(1) DEFAULT '1' COMMENT '证件类型 1:身份证 2:中国护照 3:台胞证 4:外国护照 5:外国人永居证',
  `id_no` varchar(40) DEFAULT NULL COMMENT '证件编码',
  `user_type` tinyint(1) DEFAULT '0' COMMENT '用户类型 0:系统用户 1：普通用户 2：单位用户 3:会员 9：匿名用户',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `email` varchar(30) DEFAULT NULL COMMENT '邮箱地址',
  `telephone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) DEFAULT NULL COMMENT '通讯地址',
  `post_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `login_count` int(6) DEFAULT '0' COMMENT '登录次数',
  `nick_name` varchar(256) DEFAULT NULL COMMENT '昵称',
  `other_code` varchar(64) DEFAULT NULL COMMENT '第三方平台标识',
  `activation_status` tinyint(1) DEFAULT '1' COMMENT '激活状态 1：激活 0：未激活',
  `activation_code` varchar(10) DEFAULT NULL COMMENT '激活码',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) DEFAULT '000000' COMMENT '租户编码',
  `create_org` varchar(32) DEFAULT NULL,
  `avatar` varchar(512) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统登录用户表';

-- ----------------------------
-- Table structure for tb_core_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_user_role`;
CREATE TABLE `tb_core_user_role` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `role_id` varchar(50) NOT NULL COMMENT '角色ID',
  `user_id` varchar(100) NOT NULL COMMENT '用户ID',
  `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

SET FOREIGN_KEY_CHECKS = 1;


-- start todo 接口监控相关表

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
-- Table structure for tb_log_monitor_param
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

-- end todo 接口监控相关表

-- start todo 日志相关表

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

-- end todo 日志相关表