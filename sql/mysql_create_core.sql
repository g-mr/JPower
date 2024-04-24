/*
 Navicat Premium Data Transfer

 Source Server         : localhost_mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : jpower

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 24/04/2024 16:43:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_core_client
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_client`;
CREATE TABLE `tb_core_client` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端名称',
  `client_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端CODE，唯一约束',
  `client_secret` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端密钥',
  `access_token_validity` int(11) NOT NULL DEFAULT '0' COMMENT 'token过期时间，单位秒',
  `refresh_token_validity` int(11) NOT NULL DEFAULT '0' COMMENT '刷新token时间，单位秒，时间应该比token过期时间更长',
  `login_limit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录限制',
  `sort_num` int(11) DEFAULT NULL COMMENT '排序',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_client_code` (`client_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='客户端系统表';

-- ----------------------------
-- Records of tb_core_client
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_client` VALUES (1728752347702018048, '后台管理平台', 'admin', 'SCewmm', 1800, 2400, 'NONE', 1, NULL, 1, '2021-03-03 22:33:10', 1, '2024-02-26 08:56:51', 0, 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_data_scope`;
CREATE TABLE `tb_core_data_scope` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单主键',
  `scope_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编号',
  `scope_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限名称',
  `scope_field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限字段',
  `scope_class` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限类名',
  `scope_column` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '*' COMMENT '数据可见字段',
  `scope_type` tinyint(1) DEFAULT NULL COMMENT '数据权限类型 字典：DATA_SCOPE_TYPE',
  `scope_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限值域',
  `all_role` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否所有角色都执行',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限备注',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_scope_code` (`scope_code`) USING BTREE,
  KEY `menu_id_pk` (`menu_id`),
  CONSTRAINT `menu_id_pk` FOREIGN KEY (`menu_id`) REFERENCES `tb_core_function` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='数据权限表';

-- ----------------------------
-- Records of tb_core_data_scope
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_data_scope` VALUES (1728752350004690944, 1728752670466293760, 'FILE_ALL', '文件管理 [全部可见]', '*', 'top.jpower.dbs.dao.mapper.selectPage', '-', 1, '', 0, NULL, 1, '2021-03-03 22:33:17', 1, '2023-11-26 12:29:34', 1728752845331021824, 0);
INSERT INTO `tb_core_data_scope` VALUES (1728752351065849856, 1728752670466293760, 'FILE_ORG', '文件管理 [所在机构可见]', '*', 'top.jpower.dbs.dao.mapper.selectPage', 'create_org', 3, 'create_org = {orgId}', 0, NULL, 1, '2021-03-03 22:33:17', 1, '2023-11-26 12:29:34', 1728752845331021824, 0);
INSERT INTO `tb_core_data_scope` VALUES (1728752352655491072, 1728752670466293760, 'FILE_ORG_CHILD', '文件管理 [所在机构可见及子级可见]', '*', 'top.jpower.dbs.dao.mapper.selectPage', 'create_org', 4, 'create_org in {orgId}', 0, NULL, 1, '2021-03-03 22:33:17', 1, '2023-11-26 12:29:34', 1728752845331021824, 0);
INSERT INTO `tb_core_data_scope` VALUES (1728752354085748736, 1728752670466293760, 'FILE_USER', '文件管理 [本人可见]', '*', 'top.jpower.dbs.dao.mapper.selectPage', 'create_user', 2, 'create_user = {userId}', 0, NULL, 1, '2021-03-03 22:33:17', 1, '2023-11-26 12:29:34', 1728752845331021824, 0);
INSERT INTO `tb_core_data_scope` VALUES (1728752355893493760, 1728752697632800768, 'ORG_CHILD', '部门管理 [所在机构可见及子级可见]', NULL, 'top.jpower.dbs.dao.org.mapper.TbCoreOrgMapper.listLazyByParent', 'id', 4, 'id in {orgId}', 0, NULL, 1, '2022-09-15 16:36:12', 1, '2023-11-26 12:29:34', 1728752845331021824, 0);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_dict
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_dict`;
CREATE TABLE `tb_core_dict` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `dict_type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型代码',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典代码',
  `name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `is_stop` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '是否停用',
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '上级ID',
  `locale` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'zh' COMMENT '语言 zh en',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `sort_num` int(11) DEFAULT '0' COMMENT '排序',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `dict_level` int(11) DEFAULT NULL COMMENT '树形字典结构的级别',
  `pcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '-1' COMMENT '上级代码',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='字典表';

-- ----------------------------
-- Records of tb_core_dict
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_dict` VALUES (1728752358502350848, 'DKFS', '_parent', '_parent', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752359534149632, 'DATA_SCOPE_TYPE', '1', '全部可见', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752360570142720, 'DKFS', '_top', '_top', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752361606135808, 'BUSINESS_TYPE', 'REVIEW', '审核', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:37', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752362671489024, 'BUSINESS_TYPE', 'CLEAN', '清空数据', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:37', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752363954946048, 'OPERATE_STATUS', '0', '正常', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:37', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752365154516992, 'OPERATE_STATUS', '1', '异常', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:37', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752367213920256, 'PARAM_TYPE', 'header', 'header', 'N', -1, 'zh', '', 0, 1, '2021-04-27 17:15:36', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752368367353856, 'BUSINESS_TYPE', 'OTHER', '其它', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:35', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752369399152640, 'BUSINESS_TYPE', 'INSERT', '新增', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:35', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752370430951424, 'BUSINESS_TYPE', 'GENCODE', '生成代码', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:37', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752371462750208, 'BUSINESS_TYPE', 'EXPORT', '导出', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:36', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752372268056576, 'BUSINESS_TYPE', 'UPDATE', '修改', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:36', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752373165637632, 'BUSINESS_TYPE', 'FORCE', '强退', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:36', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752374285516800, 'BUSINESS_TYPE', 'GRANT', '授权', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:36', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752375678025728, 'BUSINESS_TYPE', 'DELETE', '删除', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:36', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752376512692224, 'BUSINESS_TYPE', 'IMPORT', '导入', 'N', -1, 'zh', '', 0, 1, '2022-05-10 23:30:36', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752377347358720, 'FILE_STORAGE_TYPE', 'SERVER', '服务器', 'N', -1, 'zh', NULL, 1, 1, '2021-03-05 16:58:37', 1728753047802658816, '2023-11-26 12:30:20', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752378412711936, 'FILE_STORAGE_TYPE', 'DATABASE', '数据库', 'N', -1, 'zh', NULL, 1, 1, '2021-03-05 16:58:37', 1728753047802658816, '2023-11-26 12:30:20', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752379759083520, 'FILE_STORAGE_TYPE', 'FASTDFS', 'fastdfs', 'N', -1, 'zh', NULL, 1, 1, '2021-03-05 16:58:37', 1728753047802658816, '2023-11-26 12:30:20', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752381273227264, 'FUNCTION_TYPE', '0', '按钮', 'N', -1, 'zh', NULL, 0, 1, '2022-11-16 01:09:30', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752382518935552, 'DKFS', '_blank', '_blank', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752383928221696, 'FUNCTION_TYPE', '2', '接口', 'N', -1, 'zh', NULL, 0, 1, '2022-11-16 01:10:45', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752385052295168, 'YYZL', 'ja', '日文', 'N', -1, 'zh', NULL, 0, 1, '2022-10-09 19:44:55', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752386109259776, 'PARAM_TYPE', 'path', 'path', 'N', -1, 'zh', '', 1, 1, '2021-04-27 17:15:46', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752388613259264, 'CITY_TYPE', '1', '首都', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752390035128320, 'CITY_TYPE', '2', '直辖市', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752391054344192, 'CITY_TYPE', '3', '地级市', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752392375549952, 'CITY_TYPE', '4', '县级市', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752393524789248, 'CITY_TYPE', '9', '其他', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752394611113984, 'CITY_LEVEL', '1', '省份/直辖市', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752395642912768, 'CITY_LEVEL', '2', '地市', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752397605847040, 'CITY_LEVEL', '3', '区县', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752398818000896, 'CITY_LEVEL', '4', '乡镇/街道', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752400088875008, 'CITY_LEVEL', '5', '村委', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752401393303552, 'YYZL', 'zh', '中文', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752402227970048, 'YYZL', 'en', '英文', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752403163299840, 'YN', 'Y', '是', 'N', -1, 'zh', NULL, 2, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752404169932800, 'YN', 'N', '否', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752405449195520, 'YN01', '1', '是', 'N', -1, 'zh', NULL, 2, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752406673932288, 'YN01', '0', '否', 'N', -1, 'zh', 'null', 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752408087412736, 'ID_TYPE', '1', '身份证', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752409748357120, 'ID_TYPE', '2', '中国护照', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752410780155904, 'ID_TYPE', '3', '台胞证', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752411921006592, 'ID_TYPE', '4', '外国护照', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752412873113600, 'ID_TYPE', '5', '外国人永居证', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752414110433280, 'USER_TYPE', '0', '系统用户', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752415213535232, 'USER_TYPE', '1', '普通用户', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752416358580224, 'USER_TYPE', '2', '单位用户', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752417642037248, 'USER_TYPE', '3', '会员', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752418686418944, 'USER_TYPE', '9', '匿名用户', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752419932127232, 'USER_TYPE', '4', '客服用户', 'Y', -1, 'zh', NULL, 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752421093949440, 'XBIE', 'FEMALE', '女', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752422054445056, 'XBIE', 'MAN', '男', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752422054445057, 'XBIE', 'UNKNOWN', '未知', 'N', -1, 'zh', NULL, 1, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752423056883712, 'POST_TYPE', '1', '领导', 'N', -1, 'zh', NULL, 0, 1, '2022-09-17 15:37:34', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752424206123008, 'DKFS', '_self', '_self', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752425456025600, 'LOGIN_LIMIT', 'NONE', '不限制', 'N', -1, 'zh', NULL, 0, 1, '2022-11-11 19:06:03', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752427075026944, 'FUNCTION_TYPE', '1', '菜单', 'N', -1, 'zh', NULL, 0, 1, '2022-11-16 01:10:17', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752428148768768, 'LOGIN_LIMIT', 'ONE', '单模式', 'N', -1, 'zh', NULL, 0, 1, '2022-11-11 19:06:14', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752429260259328, 'LOGIN_LIMIT', 'SQUEEZE', '挤掉模式', 'N', -1, 'zh', NULL, 0, 1, '2022-11-11 19:06:44', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752431168667648, 'DATA_SCOPE_TYPE', '2', '本人可见', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752432041082880, 'DATA_SCOPE_TYPE', '4', '所在机构及子级可见', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752433186127872, 'DATA_SCOPE_TYPE', '5', '自定义', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752434746408960, 'DATA_SCOPE_TYPE', '3', '所在机构可见', 'N', -1, 'zh', '', 0, 1, '2021-03-03 22:33:23', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752435941785600, 'PARAM_TYPE', 'body', 'body', 'N', -1, 'zh', '', 3, 1, '2021-04-27 17:15:58', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752436977778688, 'POST_TYPE', '2', '普工', 'N', -1, 'zh', NULL, 0, 1, '2022-09-17 15:37:49', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752438118629376, 'POST_TYPE', '3', '技工', 'N', -1, 'zh', NULL, 0, 1, '2022-09-17 15:38:00', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1728752439150428160, 'PARAM_TYPE', 'query', 'query', 'N', -1, 'zh', '', 4, 1, '2021-04-27 17:16:08', 1, '2023-11-26 12:29:32', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1764851365206581250, 'SMS_CATEGORY', 'ali', '阿里', 'N', -1, 'zh', NULL, 0, 1, '2024-03-05 11:12:09', 1, '2024-03-05 11:12:09', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1782694643767443457, 'OSS_CATEGORY', 'ali', '阿里云', 'N', -1, 'zh', NULL, 0, 1, '2024-04-23 16:54:59', 1, '2024-04-23 16:54:59', 0, NULL, '-1', '000000', 1728752845331021824);
INSERT INTO `tb_core_dict` VALUES (1782694777007898626, 'OSS_CATEGORY', 'qiniu', '七牛云', 'N', -1, 'zh', NULL, 1, 1, '2024-04-23 16:55:30', 1, '2024-04-23 16:55:30', 0, NULL, '-1', '000000', 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_dict_type`;
CREATE TABLE `tb_core_dict_type` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `dict_type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型代码',
  `dict_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型名称',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `del_enabled` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '是否允许删除 N:不允许 Y允许',
  `sort_num` int(11) DEFAULT '0' COMMENT '排序',
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '父字典类型ID',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `is_tree` tinyint(1) DEFAULT NULL COMMENT '是否树形结构 字典YN01',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`dict_type_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- ----------------------------
-- Records of tb_core_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_dict_type` VALUES (1728752440236752896, 'BUSINESS_TYPE', '操作日志业务类型', NULL, 'N', 2, 1728752454979731456, 1, '2022-05-10 23:30:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752441574735872, 'CITY_LEVEL', '行政区级别', NULL, 'N', 1, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752442744946688, 'CITY_TYPE', '城市类型', NULL, 'N', 1, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752443864825856, 'DATA_SCOPE_TYPE', '数据权限类型', NULL, 'N', 0, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752444816932864, 'DKFS', '页面打开方式', NULL, 'N', 3, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752445886480384, 'FILE_STORAGE_TYPE', '文件存储类型', NULL, 'N', 1, 1728752458242899968, 1, '2021-03-03 22:33:28', 1728753047802658816, '2023-11-26 12:30:20', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752447639699456, 'FUNCTION_TYPE', '功能类型', NULL, 'N', 10, 1728752458242899968, 1, '2022-11-16 01:07:58', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752448738607104, 'ID_TYPE', '证件类型', NULL, 'N', 4, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752450022064128, 'LOGIN_LIMIT', '登录限制', NULL, 'N', 10, 1728752458242899968, 1, '2022-11-11 19:05:48', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752451401990144, 'OPERATE_STATUS', '操作结果状态', NULL, 'N', 3, 1728752454979731456, 1, '2022-05-10 23:30:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752452387651584, 'PARAM_TYPE', '参数类型', '', 'Y', 1, 1728752454979731456, 1, '2021-04-27 17:15:15', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752453624971264, 'POST_TYPE', '岗位类型', NULL, 'Y', 10, 1728752458242899968, 1, '2022-09-17 15:37:08', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752454979731456, 'RESTFUL_MONITOR', '基础-日志监控字典', '', 'Y', 1, -1, 1, '2021-04-27 17:14:46', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752456913305600, 'TOP_BUSINESS_ZDIAN', '业务-基础字典', '', 'Y', 2, -1, 1, '2021-03-06 00:58:23', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752458242899968, 'TOP_SYSTEM_CORE_ZDIAN', '核心-基础字典', NULL, 'N', 1, -1, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752460486852608, 'USER_TYPE', '用户类型', NULL, 'N', 4, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752461275381760, 'XBIE', '性别', NULL, 'N', 4, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752462391066624, 'YN', '是否', NULL, 'N', 3, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752464215588864, 'YN01', '是否01', NULL, 'N', 3, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1728752465650040832, 'YYZL', '语言种类', NULL, 'N', 2, 1728752458242899968, 1, '2021-03-03 22:33:28', 1, '2023-11-26 12:29:35', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1764851320730181633, 'SMS_CATEGORY', '短信分类', NULL, 'Y', 0, 1728752458242899968, 1, '2024-03-05 11:11:59', 1, '2024-03-05 11:11:59', 0, 0, 1728752845331021824);
INSERT INTO `tb_core_dict_type` VALUES (1782694566495780866, 'OSS_CATEGORY', '对象存储类型', NULL, 'Y', 3, 1728752458242899968, 1, '2024-04-23 16:54:40', 1, '2024-04-23 16:54:40', 0, 0, 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_function
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_function`;
CREATE TABLE `tb_core_function` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `client_id` bigint(20) NOT NULL COMMENT '客户端ID',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `alias` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '别名',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能编码',
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '父级ID',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源URL',
  `function_type` int(11) NOT NULL DEFAULT '0' COMMENT '功能类型 0：按钮 1：菜单 2：接口',
  `is_hide` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `target` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '_self' COMMENT '打开方式',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `moude_summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '模块概述',
  `operate_instruction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '操作说明',
  `function_level` int(11) DEFAULT NULL COMMENT '菜单级别',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `ancestor_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code_index` (`code`) USING BTREE,
  KEY `client_id_fk` (`client_id`),
  CONSTRAINT `client_id_fk` FOREIGN KEY (`client_id`) REFERENCES `tb_core_client` (`id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='功能菜单表';

-- ----------------------------
-- Records of tb_core_function
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_function` VALUES (1728752466925109248, 1728752347702018048, '在线文档', '文档', '/kanyun', 1728752510478761984, 'https://www.kancloud.cn/guodingzhi/jpower/', 1, 0, 'iconfont iconicon_study', '_blank', 0, '', '', '', NULL, 1, '2021-03-04 00:56:46', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752468548304896, 1728752347702018048, '权限设置', '权限', 'AUTHORITY', -1, '/authority', 1, 0, 'iconfont iconicon_safety', '_self', 2, '', '', '', NULL, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752473178816512, 1728752347702018048, '菜单列表', '菜单列表', 'CHILD_FUNCTION', 1728752684378800128, '/core/function/listByParent', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752475166916608, 1728752347702018048, '列表', '列表', 'CITY_LIST', 1728752605429415936, '/core/city/lazyTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752477540892672, 1728752347702018048, '列表', '列表', 'CLIENT_LIST', 1728752614065487872, '/core/client/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752479638044672, 1728752347702018048, '客户端功能树', '客户端功能树', 'CLIENT_MENU_TREE', 1728752752674652160, '/core/function/clientMenuTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752481009582080, 1728752347702018048, '客户端下拉', '客户端下拉', 'DATASCOPE_CLIENT_SELECT', 1728752623653666816, '/core/client/selectList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752482834104320, 1728752347702018048, '顶级菜单选项', '顶级菜单选项', 'DATASCOPE_TOPMENU_SELECT', 1728752623653666816, '/core/menu/select', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752485220663296, 1728752347702018048, '字典查询', '字典查询', 'DICT_SELECT', -1, '/core/dict/getDictListByType', 2, 0, NULL, '_self', 100, NULL, NULL, NULL, NULL, 1, '2022-10-27 20:00:42', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752487330398208, 1728752347702018048, '接口文档', '接口', 'DOC', 1728752510478761984, 'http://doc.top', 1, 0, 'iconfont iconicon_compile', '_blank', 0, '', '', '', NULL, 1, '2021-03-04 00:54:13', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752488563523584, 1728752347702018048, '文件下载', '下载', 'DOWNLOAD', -1, '/core/file/download/{base}', 2, 0, '', '_self', 105, '', '', '', 0, 1, '2021-03-03 22:33:41', 1, '2024-04-24 11:44:31', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752490606149632, 1728752347702018048, 'ELK监控', 'ELK', 'ELK', 1728752516157849600, 'http://elk.top', 1, 0, 'iconfont icon-biaodan', '_blank', 0, '', '', '', NULL, 1, '2021-03-04 00:47:25', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752491759583232, 1728752347702018048, '错误日志', '错误日志', 'ERROR_LOG', 1728752512726908928, '/log/error', 1, 0, 'iconfont iconicon_doc', '_self', 5, '', '', '', NULL, 1, '2022-05-10 23:30:56', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752494343274496, 1728752347702018048, '错误日志', '错误日志', 'ERROR_LOG_LIST', 1728752491759583232, '/log/error/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752496788553728, 1728752347702018048, '客户端下拉', '客户端下拉', 'FUNCTION_CLIENT_SELECT', 1728752684378800128, '/core/client/selectList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752498541772800, 1728752347702018048, '菜单下按钮列表', '按钮列表', 'FUNCTION_LISTBUT', -1, '/core/function/listBut', 2, 0, NULL, '_self', 102, NULL, NULL, NULL, 3, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752501008023552, 1728752347702018048, '菜单列表', '菜单列表', 'FUNCTION_LISTMENUTREE', -1, '/core/function/listMenuTree', 2, 0, NULL, '_self', 101, NULL, NULL, NULL, 3, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752503692378112, 1728752347702018048, '顶级菜单选项', '顶级菜单选项', 'FUNCTION_TOPMENU_SELECT', 1728752684378800128, '/core/menu/select', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752506099908608, 1728752347702018048, '网关管理', '网关', 'GATEWAY', -1, '/gateway', 1, 0, 'iconfont iconicon_subordinate', '_blank', 7, '', '', '', NULL, 1, '2021-03-04 00:34:30', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752509442768896, 1728752347702018048, '获取源码', '源码', 'GITEE', 1728752510478761984, 'https://gitee.com/gdzWork', 1, 0, 'iconfont icongitee2', '_blank', 0, '', '', '', NULL, 1, '2021-03-04 00:58:14', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752510478761984, 1728752347702018048, '了解JPower', 'JPower', 'JPOWER', -1, '/jpower', 1, 0, 'iconfont iconicon_task', '_blank', 10, '', '', '', NULL, 1, '2021-03-04 00:33:04', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752512726908928, 1728752347702018048, '系统日志', '日志', 'LOG', -1, '/log', 1, 0, 'iconfont icon-caidanguanli', '_self', 5, '', '', '', NULL, 1, '2022-05-10 23:30:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752516157849600, 1728752347702018048, '系统监控', '监控', 'MONITOR', -1, '/monitor', 1, 0, 'iconfont icon-yanzhengma', '_blank', 5, '', '', '', NULL, 1, '2021-03-04 00:31:33', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752518888341504, 1728752347702018048, '删除接口设置', '删除接口设置', 'MONITOR_DELETE_SETUP', 1728752531517390848, '/monitor/setting/delete-setup', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752519840448512, 1728752347702018048, '获取接口参数', '获取接口参数', 'MONITOR_PARAMS', 1728752531517390848, '/monitor/setting/param', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752521165848576, 1728752347702018048, '接口监控', '接口监控', 'MONITOR_RESTFUL', 1728752516157849600, '/monitor/restful', 1, 0, 'iconfont iconicon_task', '_self', 0, '', '', '', NULL, 1, '2021-04-25 02:32:27', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752523451744256, 1728752347702018048, '监控结果', '监控结果', 'MONITOR_RESULT', 1728752521165848576, '/log/monitor/result', 1, 0, 'iconfont icon-debug', '_self', 0, '', '用来查询接口监控得结果，包括第三方服务得监控结果', '', NULL, 1, '2021-04-25 02:35:22', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752525334986752, 1728752347702018048, '导出监控结果', '导出监控结果', 'MONITOR_RESULTS_EXPORT', 1728752523451744256, '/monitor/log/export', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752526987542528, 1728752347702018048, '监控结果', '监控结果', 'MONITOR_RESULTS_LIST', 1728752523451744256, '/monitor/log/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752528140976128, 1728752347702018048, '保存接口参数', '保存接口参数', 'MONITOR_SAVE_PARAMS', 1728752531517390848, '/monitor/setting/save-param', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752529298604032, 1728752347702018048, '保存接口设置', '保存接口设置', 'MONITOR_SAVE_SETUP', 1728752531517390848, '/monitor/setting/save-setup', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752530439454720, 1728752347702018048, '服务列表', '服务列表', 'MONITOR_SERVERS', 1728752523451744256, '/monitor/setting/servers', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752531517390848, 1728752347702018048, '监控设置', '监控设置', 'MONITOR_SETTING', 1728752521165848576, '/log/monitor/setting', 1, 0, 'iconfont icon-canshu', '_self', 1, '', '主要用来设置接口监控时得参数', '', NULL, 1, '2021-04-25 02:38:03', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752533727789056, 1728752347702018048, '获取接口设置', '获取接口设置', 'MONITOR_SETUP', 1728752531517390848, '/monitor/setting/setup', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752535271292928, 1728752347702018048, '分组列表', '分组列表', 'MONITOR_TAGS', 1728752523451744256, '/monitor/setting/tags', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752536198234112, 1728752347702018048, '接口树形', '接口树形', 'MONITOR_TREE', 1728752531517390848, '/monitor/setting/monitors', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752537448136704, 1728752347702018048, '注册中心', 'NACOS', 'NACOS', 1728752506099908608, 'http://nacos.top', 1, 0, 'iconfont icon-iconset0265', '_blank', 0, '', '', '', NULL, 1, '2021-03-04 00:45:29', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752539486568448, 1728752347702018048, '操作日志', '操作日志', 'OPERATE_LOG', 1728752512726908928, '/log/operate', 1, 0, 'iconfont iconicon_compile', '_self', 5, '', '', '', NULL, 1, '2022-05-10 23:30:56', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752542833623040, 1728752347702018048, '操作日志', '操作日志', 'OPERATE_LOG_LIST', 1728752539486568448, '/log/operate/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752544486178816, 1728752347702018048, '组织管理', '组织', 'ORG', -1, '/org', 1, 0, 'iconfont iconicon_shakehands', '_self', 1, '', '', '', NULL, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752547980034048, 1728752347702018048, '新增', '新增', 'ORG_CHILD_ADD', 1728752697632800768, '/core/org/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752549510955008, 1728752347702018048, '列表', '列表', 'PARAM_LIST', 1728752712317059072, '/core/param/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752551624884224, 1728752347702018048, '岗位管理', '岗位', 'POST', 1728752544486178816, '/core/post', 1, 0, 'iconfont iconicon_group', '_self', 3, NULL, NULL, NULL, NULL, 1, '2022-09-17 15:14:36', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752553889808384, 1728752347702018048, '新增岗位', '新增岗位', 'POST_ADD', 1728752551624884224, '/core/post/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752556381224960, 1728752347702018048, '删除岗位', '删除岗位', 'POST_DELETE', 1728752551624884224, '/core/post/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752565197651968, 1728752347702018048, '岗位详情', '岗位详情', 'POST_DETAIL', 1728752551624884224, '/core/post/get', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752572306997248, 1728752347702018048, '岗位列表', '岗位列表', 'POST_PAGE', 1728752551624884224, '/core/post/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752577394688000, 1728752347702018048, '岗位下拉', '岗位下拉', 'POST_SELECT', 1728752767056920576, '/core/post/select', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752580817240064, 1728752347702018048, '编辑岗位', '编辑岗位', 'POST_UPDATE', 1728752551624884224, '/core/post/update', 0, 0, NULL, '_self', 100, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2024-03-22 10:00:49', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752583459651584, 1728752347702018048, '客户端下拉', '客户端下拉', 'ROLE_CLIENT_SELECT', 1728752728398020608, '/core/client/selectList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752585988816896, 1728752347702018048, '客户端顶部菜单树', '客户端顶部菜单树', 'ROLE_CLIENT_TOPMENU', 1728752728398020608, '/core/menu/listName', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752588639617024, 1728752347702018048, '菜单树形', '菜单树形', 'ROLE_MENU_TREE', 1728752728398020608, '/core/function/menuTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752591604989952, 1728752347702018048, '顶级菜单选项', '顶级菜单选项', 'ROLE_TOPMENU', 1728752728398020608, '/core/menu/select', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752593567924224, 1728752347702018048, '顶部菜单ID', '顶部菜单ID', 'ROLE_TOPMENU_ID', 1728752728398020608, '/core/role/topMenuId', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752595866402816, 1728752347702018048, '接口限流', '限流', 'SENTINEL', 1728752506099908608, 'http://sentinel.top', 1, 0, 'iconfont iconicon_exchange', '_blank', 0, '', '', '', NULL, 1, '2021-03-04 00:49:42', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752597271494656, 1728752347702018048, '链路监控', '链路', 'SKYWALKING', 1728752516157849600, 'http://skywalking.top', 1, 0, 'iconfont icon-iconset0216', '_blank', 0, '', '', '', NULL, 1, '2021-03-04 00:38:42', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752598496231424, 1728752347702018048, '应用监控', '应用', 'SPRING-BOOT-ADMIN', 1728752516157849600, 'http://admin.top', 1, 0, 'iconfont iconicon_safety', '_blank', 0, '', '', '', NULL, 1, '2021-03-04 00:43:27', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752599901323264, 1728752347702018048, '系统管理', '系统', 'SYSTEM', -1, '/system', 1, 0, 'iconfont iconicon_setting', '_self', 3, '', '', '', 1, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752605429415936, 1728752347702018048, '行政区划', '行政区列表', 'SYSTEM_CITY', 1728752599901323264, '/core/city', 1, 0, 'iconfont iconicon_GPS', '_self', 7, NULL, NULL, NULL, 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752607845335040, 1728752347702018048, '删除', '删除', 'SYSTEM_CITY_DELETE', 1728752605429415936, '/core/city/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752609959264256, 1728752347702018048, '详情', '详情', 'SYSTEM_CITY_DETAIL', 1728752605429415936, '/core/city/get', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752611938975744, 1728752347702018048, '保存', '保存', 'SYSTEM_CITY_SAVE', 1728752605429415936, '/core/city/save', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752614065487872, 1728752347702018048, '客户端管理', '客户端列表', 'SYSTEM_CLIENT', 1728752599901323264, '/core/client', 1, 0, 'iconfont iconicon_airplay', '_self', 1, NULL, NULL, NULL, 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752616913420288, 1728752347702018048, '保存', '保存', 'SYSTEM_CLIENT_ADD', 1728752614065487872, '/core/client/save', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752618784079872, 1728752347702018048, '删除', '删除', 'SYSTEM_CLIENT_DELETE', 1728752614065487872, '/core/client/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752620776374272, 1728752347702018048, '保存', '保存', 'SYSTEM_CLIENT_SAVE', 1728752614065487872, '/core/client/save', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752623653666816, 1728752347702018048, '数据权限', '数据权限', 'SYSTEM_DATASCOPE', 1728752468548304896, '/core/dataScope', 1, 0, 'iconfont icon-shujuzhanshi2', '_self', 2, '', '', '', 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752628695220224, 1728752347702018048, '新增', '新增', 'SYSTEM_DATASCOPE_ADD', 1728752623653666816, '/core/dataScope/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752630628794368, 1728752347702018048, '删除', '删除', 'SYSTEM_DATASCOPE_DELETE', 1728752623653666816, '/core/dataScope/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752632491065344, 1728752347702018048, '详情', '详情', 'SYSTEM_DATASCOPE_DETAIL', 1728752623653666816, '/core/dataScope/queryById', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752634797932544, 1728752347702018048, '数据权限', '数据权限', 'SYSTEM_DATASCOPE_LIST', 1728752728398020608, '/core/dataScope/listByMenuId', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752636916056064, 1728752347702018048, '数据权限ID', '数据权限ID', 'SYSTEM_DATASCOPE_LISTID', 1728752728398020608, '/core/dataScope/listIdByRoleId', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752639038373888, 1728752347702018048, '列表', '列表', 'SYSTEM_DATASCOPE_LISTPAGE', 1728752623653666816, '/core/dataScope/listPage', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752640732872704, 1728752347702018048, '菜单列表', '菜单列表', 'SYSTEM_DATASCOPE_MENU', 1728752623653666816, '/core/dataScope/listDataByParent', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752642410594304, 1728752347702018048, '数据赋权', '数据赋权', 'SYSTEM_DATASCOPE_ROLE', 1728752728398020608, '/core/dataScope/roleDataScope', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752644541300736, 1728752347702018048, '修改', '修改', 'SYSTEM_DATASCOPE_UPDATE', 1728752623653666816, '/core/dataScope/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752646332268544, 1728752347702018048, '字典管理', '字典列表', 'SYSTEM_DICT', 1728752599901323264, '/core/dict', 1, 0, 'iconfont iconicon_study', '_self', 8, NULL, NULL, NULL, 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752649532522496, 1728752347702018048, '删除字典', '删除字典', 'SYSTEM_DICT_DELETE', 1728752646332268544, '/core/dict/deleteDict', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752651470290944, 1728752347702018048, '字典详情', '字典详情', 'SYSTEM_DICT_DETAIL', 1728752646332268544, '/core/dict/getDict', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752653533888512, 1728752347702018048, '字典列表', '字典列表', 'SYSTEM_DICT_LIST', 1728752646332268544, '/core/dict/listByType', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752655182249984, 1728752347702018048, '字典子级', '字典子级', 'SYSTEM_DICT_LIST_BY_PARENT', 1728752646332268544, '/core/dict/listDictChildList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752657212293120, 1728752347702018048, '保存字典', '保存字典', 'SYSTEM_DICT_SAVE', 1728752646332268544, '/core/dict/saveDict', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752659615629312, 1728752347702018048, '停用字典', '停用字典', 'SYSTEM_DICT_STOP', 1728752646332268544, '/core/dict/stopDict', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752661452734464, 1728752347702018048, '字典类型树', '字典类型树', 'SYSTEM_DICT_TYPELIST', 1728752646332268544, '/core/dict/dictTypeTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752663252090880, 1728752347702018048, '新增字典类型', '新增字典类型', 'SYSTEM_DICT_TYPE_ADD', 1728752646332268544, '/core/dict/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752665244385280, 1728752347702018048, '删除字典类型', '删除字典类型', 'SYSTEM_DICT_TYPE_DELETE', 1728752646332268544, '/core/dict/deleteDictType', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752666892746752, 1728752347702018048, '字典类型详情', '字典类型详情', 'SYSTEM_DICT_TYPE_DETAIL', 1728752646332268544, '/core/dict/getDictType', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752668935372800, 1728752347702018048, '修改字典类型', '修改字典类型', 'SYSTEM_DICT_TYPE_UPDATE', 1728752646332268544, '/core/dict/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752670466293760, 1728752347702018048, '文件管理', '文件列表', 'SYSTEM_FILE', 1764842401228152834, '/resource/file', 1, 0, 'iconfont iconicon_doc', '_self', 3, '', '', '', 2, 1, '2021-03-03 22:33:41', 1, '2024-04-23 16:47:47', 0, 1728752845331021824, '-1,1764842401228152834');
INSERT INTO `tb_core_function` VALUES (1728752674559934464, 1728752347702018048, '文件上传', '上传', 'FILE_ADD', -1, '/core/file/upload', 2, 0, '', '_self', 106, NULL, NULL, NULL, 3, 1, '2021-03-03 22:33:41', 1, '2024-04-24 09:26:04', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752676334125056, 1728752347702018048, '批量删除', '批量删除', 'SYSTEM_FILE_DELETE', 1728752670466293760, '/core/file/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752678917816320, 1728752347702018048, '文件详情', '文件详情', 'SYSTEM_FILE_DETAIL', 1728752670466293760, '/core/file/get', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752680771698688, 1728752347702018048, '文件列表', '文件列表', 'SYSTEM_FILE_LIST', 1728752670466293760, '/core/file/listPage', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752682763993088, 1728752347702018048, '修改文件', '修改文件', 'SYSTEM_FILE_UPDATE', 1728752670466293760, '/core/file/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752684378800128, 1728752347702018048, '功能管理', '功能列表', 'SYSTEM_FUNCTION', 1728752599901323264, '/core/function', 1, 0, 'iconfont icon-caidan', '_self', 5, NULL, NULL, NULL, 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752686949908480, 1728752347702018048, '新增', '新增', 'SYSTEM_FUNCTION_ADD', 1728752684378800128, '/core/function/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752688317251584, 1728752347702018048, '删除', '删除', 'SYSTEM_FUNCTION_DELETE', 1728752684378800128, '/core/function/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752690909331456, 1728752347702018048, '功能点同步', '同步', 'SYSTEM_FUNCTION_GENERATE', 1728752684378800128, '/core/function/generate', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752692343783424, 1728752347702018048, '菜单开关', '同步', 'SYSTEM_FUNCTION_HIDE', 1728752684378800128, '/core/function/hide', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752694231220224, 1728752347702018048, '菜单树形', '菜单树形', 'SYSTEM_FUNCTION_MENU', 1728752684378800128, '/core/function/menuTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752695665672192, 1728752347702018048, '修改', '修改', 'SYSTEM_FUNCTION_UPDATE', 1728752684378800128, '/core/function/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752697632800768, 1728752347702018048, '部门管理', '部门列表', 'SYSTEM_ORG', 1728752544486178816, '/core/org', 1, 0, 'iconfont iconicon_group', '_self', 2, NULL, NULL, NULL, 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752700317155328, 1728752347702018048, '下级部门', '下级部门', 'SYSTEM_ORGCHILDER_LIST', 1728752697632800768, '/core/org/listLazyByParent', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752704528236544, 1728752347702018048, '删除', '删除', 'SYSTEM_ORG_DELETE', 1728752697632800768, '/core/org/deleteStatus', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752706197569536, 1728752347702018048, '树形部门', '树形部门', 'SYSTEM_ORG_TREE', 1728752697632800768, '/core/org/tree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752708013703168, 1728752347702018048, '树形列表', '树形列表', 'SYSTEM_ORG_TREELIST', 1728752697632800768, '/core/org/listLazy', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752709712396288, 1728752347702018048, '编辑', '编辑', 'SYSTEM_ORG_UPDATE', 1728752697632800768, '/core/org/update', 0, 0, NULL, '_self', 40, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2024-03-22 10:03:29', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752712317059072, 1728752347702018048, '参数管理', '参数列表', 'SYSTEM_PARAMS', 1728752599901323264, '/core/param', 1, 0, 'iconfont icon-biaodan', '_self', 6, NULL, NULL, NULL, 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752716112904192, 1728752347702018048, '新增', '新增', 'SYSTEM_PARAMS_ADD', 1728752712317059072, '/core/param/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752721745854464, 1728752347702018048, '删除', '删除', 'SYSTEM_PARAMS_DELETE', 1728752712317059072, '/core/param/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752723910115328, 1728752347702018048, '详情', '详情', 'SYSTEM_PARAMS_DETAIL', 1728752712317059072, '/core/param/queryById', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752726028238848, 1728752347702018048, '编辑', '编辑', 'SYSTEM_PARAMS_UPDATE', 1728752712317059072, '/core/param/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752728398020608, 1728752347702018048, '角色管理', '角色列表', 'SYSTEM_ROLE', 1728752468548304896, '/core/role', 1, 0, 'iconfont iconicon_ding', '_self', 4, NULL, NULL, NULL, 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752731355004928, 1728752347702018048, '新增', '新增', 'SYSTEM_ROLE_ADD', 1728752728398020608, '/core/role/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752733187915776, 1728752347702018048, '角色新增用户', '角色新增用户', 'SYSTEM_ROLE_ADDUSER', 1728752728398020608, '/core/user/addRoleUser', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752734903386112, 1728752347702018048, '菜单资源', '菜单资源', 'SYSTEM_ROLE_BUT', 1728752728398020608, '/core/function/listButByMenu', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752737277362176, 1728752347702018048, '删除', '删除', 'SYSTEM_ROLE_DELETE', 1728752728398020608, '/core/role/deleteStatus', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752738820866048, 1728752347702018048, '角色去除用户', '角色去除用户', 'SYSTEM_ROLE_DELUSER', 1728752728398020608, '/core/user/deleteRoleUser', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752741018681344, 1728752347702018048, '树形角色列表', '树形角色列表', 'SYSTEM_ROLE_LIST_TREE', 1728752728398020608, '/core/role/listTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752742557990912, 1728752347702018048, '树形部门', '树形部门', 'SYSTEM_ROLE_ORG', 1728752728398020608, '/core/org/tree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752744407678976, 1728752347702018048, '角色权限', '角色权限', 'SYSTEM_ROLE_SELECT_URL', 1728752728398020608, '/core/function/queryUrlIdByRole', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752746290921472, 1728752347702018048, '修改', '修改', 'SYSTEM_ROLE_UPDATE', 1728752728398020608, '/core/role/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752748673286144, 1728752347702018048, '设置权限', '设置权限', 'SYSTEM_ROLE_UPDATEFUNCTION', 1728752728398020608, '/core/role/addFunction', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752750640414720, 1728752347702018048, '角色用户列表', '角色用户列表', 'SYSTEM_ROLE_USER', 1728752728398020608, '/core/user/listByRole', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752752674652160, 1728752347702018048, '租户管理', '租户管理', 'SYSTEM_TENANT', 1728752787206356992, '/core/tenant', 1, 0, 'iconfont iconicon_boss', '_self', 0, '', '', '', 1, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752754411094016, 1728752347702018048, '新增租户', '新增租户', 'SYSTEM_TENANT_ADD', 1728752752674652160, '/core/tenant/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752755770048512, 1728752347702018048, '删除租户', '删除租户', 'SYSTEM_TENANT_DELETE', 1728752752674652160, '/core/tenant/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752757401632768, 1728752347702018048, '域名查询租户', '域名查询租户', 'SYSTEM_TENANT_DOMAIN', -1, '/core/tenant/queryByDomain', 2, 0, '', '_self', 104, '', '', '', 1, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752760216010752, 1728752347702018048, '授权配置', '授权配置', 'SYSTEM_TENANT_SETTING', 1728752752674652160, '/core/tenant/setting', 0, 0, NULL, '_self', 0, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2024-03-22 09:53:47', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752761784680448, 1728752347702018048, '修改租户', '修改租户', 'SYSTEM_TENANT_UPDATE', 1728752752674652160, '/core/tenant/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752763823112192, 1728752347702018048, '顶级菜单', '顶级菜单', 'SYSTEM_TOPMENU', 1728752599901323264, '/core/menu', 1, 0, 'iconfont iconicon_subordinate', '_self', 4, NULL, NULL, NULL, NULL, 1, '2022-10-26 20:55:50', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752767056920576, 1728752347702018048, '用户管理', '用户列表', 'SYSTEM_USER', 1728752544486178816, '/core/user', 1, 0, 'iconfont icon-yonghu', '_self', 3, NULL, NULL, NULL, 2, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752769846132736, 1728752347702018048, '新增用户', '新增用户', 'SYSTEM_USER_ADD', 1728752767056920576, '/core/user/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752771544825856, 1728752347702018048, '删除用户', '删除用户', 'SYSTEM_USER_DELETE', 1728752767056920576, '/core/user/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752773285462016, 1728752347702018048, '模板下载', '模板下载', 'SYSTEM_USER_DOWNLOADTEMPLATE', 1728752767056920576, '/core/user/downloadTemplate', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752775151927296, 1728752347702018048, '导出用户', '导出用户', 'SYSTEM_USER_EXPORTUSER', 1728752767056920576, '/core/user/exportUser', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752777530097664, 1728752347702018048, '导入用户', '导入用户', 'SYSTEM_USER_IMPORTUSER', 1728752767056920576, '/core/user/importUser', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752779589500928, 1728752347702018048, '树形部门', '树形部门', 'SYSTEM_USER_ORG', 1728752767056920576, '/core/org/tree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752781363691520, 1728752347702018048, '重置密码', '重置密码', 'SYSTEM_USER_RESETPASSWORD', 1728752767056920576, '/core/user/resetPassword', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752782907195392, 1728752347702018048, '修改用户', '修改用户', 'SYSTEM_USER_UPDATE', 1728752767056920576, '/core/user/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752785151148032, 1728752347702018048, '设置角色', '设置角色', 'SYSTEM_USER_UPDATEROLE', 1728752767056920576, '/core/user/addRole', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752787206356992, 1728752347702018048, '租户设置', '租户', 'TENANT', -1, '/tenant', 1, 0, 'iconfont icon-rizhi1', '_self', 0, '', '', '', 1, 1, '2021-03-03 22:33:41', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752789169291264, 1728752347702018048, '租户列表', '租户列表', 'TENANT_LIST', 1728752752674652160, '/core/tenant/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752790679240704, 1728752347702018048, '租户下拉', '租户下拉', 'TENANT_SELECT', -1, '/core/tenant/selectors', 2, 0, NULL, '_self', 100, NULL, NULL, NULL, NULL, 1, '2022-10-27 19:59:56', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752792348573696, 1728752347702018048, '新增菜单', '新增菜单', 'TOPMENU_ADD', 1728752763823112192, '/core/menu/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752794315702272, 1728752347702018048, '客户端下拉', '客户端下拉', 'TOPMENU_CLIENT_SELECT', 1728752763823112192, '/core/client/selectList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752796102475776, 1728752347702018048, '删除菜单', '删除菜单', 'TOPMENU_DELETE', 1728752763823112192, '/core/menu/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752798287708160, 1728752347702018048, '一级菜单', '一级菜单', 'TOPMENU_FUNCTION', 1728752763823112192, '/core/menu/listFunction', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752799822823424, 1728752347702018048, '关联一级菜单ID', '关联一级菜单ID', 'TOPMENU_FUNCTION_ID', 1728752763823112192, '/core/menu/listFunctionId', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752801504739328, 1728752347702018048, '保存一级菜单', '保存一级菜单', 'TOPMENU_FUNCTION_SAVE', 1728752763823112192, '/core/menu/saveFunction', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752803429924864, 1728752347702018048, '菜单列表', '菜单列表', 'TOPMENU_LIST', 1728752763823112192, '/core/menu/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752805208309760, 1728752347702018048, '更新菜单', '更新菜单', 'TOPMENU_UPDATE', 1728752763823112192, '/core/menu/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752807376764928, 1728752347702018048, '顶部菜单', '顶部菜单', 'TOP_MENU', -1, '/core/menu/roleMenu', 2, 0, NULL, '_self', 99, NULL, NULL, NULL, NULL, 1, '2022-10-27 19:14:51', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752809180315648, 1728752347702018048, '修改登录人信息', '修改登录人信息', 'UPDATE_LOGIN', -1, '/core/user/updateLogin', 0, 0, '', '_self', 50, '', '', '', NULL, 1, '2021-03-03 22:33:41', 1, '2024-03-22 10:04:57', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752811646566400, 1728752347702018048, '修改密码', '修改密码', 'UPDATE_PASSWORD', -1, '/core/user/updatePassword', 0, 0, NULL, '_self', 50, NULL, NULL, NULL, 1, 1, '2021-03-03 22:33:41', 1, '2024-03-22 10:02:15', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752813676609536, 1728752347702018048, '用户详情', '用户详情', 'USER_DETAIL', 1728752767056920576, '/core/user/getById', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752815874424832, 1728752347702018048, '用户列表', '用户列表', 'USER_LIST', 1728752767056920576, '/core/user/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752817375985664, 1728752347702018048, '踢下线', '踢下线', 'USER_OFFLINE', 1728752767056920576, '/core/user/offline', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752819838042112, 1728752347702018048, '用户在线信息', '用户在线信息', 'USER_ONLINE', 1728752767056920576, '/core/user/online', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1728752821935194112, 1728752347702018048, '角色树形', '角色树形', 'USER_ROLE_TREE', 1728752767056920576, '/core/role/tree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2023-04-23 15:36:55', 1, '2023-11-26 12:29:34', 0, 1728752845331021824, NULL);
INSERT INTO `tb_core_function` VALUES (1764842401228152834, 1728752347702018048, '资源管理', '资源', 'RESOURCE', -1, '/resource', 1, 0, 'iconfont iconicon_study', '_self', 4, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:36:32', 1, '2024-03-05 10:40:57', 0, 1728752845331021824, '-1');
INSERT INTO `tb_core_function` VALUES (1764843053698277377, 1728752347702018048, '短信配置', 'SMS', 'SMS', 1764842401228152834, '/resource/sms', 1, 0, 'iconfont iconicon_sms', '_self', 1, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:39:08', 1, '2024-03-05 11:07:17', 0, 1728752845331021824, '1764842401228152834,-1');
INSERT INTO `tb_core_function` VALUES (1764846846288617473, 1728752347702018048, '菜单按钮树形', '菜单按钮树形', 'SYSTEM_FUNCTION_MENUBTN', 1728752684378800128, '/core/function/treeMenuTypeByClientId', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '-1,1728752684378800128');
INSERT INTO `tb_core_function` VALUES (1764846846288617474, 1728752347702018048, '设置层级', '设置层级', 'SYSTEM_FUNCTION_HIERARCHY', 1728752684378800128, '/core/function/saveHierarchy', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '-1,1728752684378800128');
INSERT INTO `tb_core_function` VALUES (1764846846292811778, 1728752347702018048, '树形按钮', '树形按钮', 'SYSTEM_ROLE_BUT_TREE', 1728752728398020608, '/core/function/treeButByMenu', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '-1,1728752728398020608');
INSERT INTO `tb_core_function` VALUES (1764846846301200386, 1728752347702018048, '菜单开关', '菜单开关', 'TOPMENU_SWITCH', 1728752763823112192, '/core/menu/switch', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '-1,1728752763823112192');
INSERT INTO `tb_core_function` VALUES (1764846846305394690, 1728752347702018048, '复制', '复制', 'SYSTEM_DATASCOPE_COPY', 1728752623653666816, '/core/dataScope/copy', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '-1,1728752623653666816');
INSERT INTO `tb_core_function` VALUES (1764846846305394691, 1728752347702018048, '删除', '删除', 'SMS_DEL', 1764843053698277377, '/sms/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '1764842401228152834,-1,1764843053698277377');
INSERT INTO `tb_core_function` VALUES (1764846846313783297, 1728752347702018048, '新增', '新增', 'SMS_ADD', 1764843053698277377, '/sms/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '1764842401228152834,-1,1764843053698277377');
INSERT INTO `tb_core_function` VALUES (1764846846313783298, 1728752347702018048, '编辑', '编辑', 'SMS_UPDATE', 1764843053698277377, '/sms/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '1764842401228152834,-1,1764843053698277377');
INSERT INTO `tb_core_function` VALUES (1764846846313783299, 1728752347702018048, '列表', '列表', 'SMS_LIST', 1764843053698277377, '/sms/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 10:54:12', 1, '2024-03-05 10:54:12', 0, 1728752845331021824, '1764842401228152834,-1,1764843053698277377');
INSERT INTO `tb_core_function` VALUES (1764849364200611842, 1728752347702018048, '接口资源', '接口资源', 'ROLE_INTERFACE_LIST', 1728752748673286144, '/core/function/listInterface', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 11:04:12', 1, '2024-03-05 11:04:12', 0, 1728752845331021824, '-1,1728752748673286144');
INSERT INTO `tb_core_function` VALUES (1764952105904267265, 1728752347702018048, '调试', '调试', 'SMS_TEST', 1764843053698277377, '/sms/test', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-03-05 17:52:28', 1, '2024-03-05 17:52:28', 0, 1728752845331021824, '1764842401228152834,-1,1764843053698277377');
INSERT INTO `tb_core_function` VALUES (1782695353636618241, 1728752347702018048, '对象存储', 'OSS', 'OSS', 1764842401228152834, '/resource/oss', 1, 0, 'iconfont icon-iconset0265', '_self', 2, NULL, NULL, NULL, NULL, 1, '2024-04-23 16:57:48', 1, '2024-04-24 09:19:41', 0, 1728752845331021824, '1764842401228152834,-1');
INSERT INTO `tb_core_function` VALUES (1782700222611193857, 1728752347702018048, '修改租户配置', '修改租户配置', 'TENANT_UPDATE_CONFIG', 1728752752674652160, '/core/tenant/updateConfig/{id}', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-04-23 17:17:09', 1, '2024-04-23 17:17:09', 0, 1728752845331021824, '-1,1728752752674652160');
INSERT INTO `tb_core_function` VALUES (1782700222611193858, 1728752347702018048, '新增', '新增', 'SYSTEM_ORG_ADD', 1728752697632800768, '/core/org/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-04-23 17:17:09', 1, '2024-04-23 17:17:09', 0, 1728752845331021824, '-1,1728752697632800768');
INSERT INTO `tb_core_function` VALUES (1782700222615388161, 1728752347702018048, '更新', '更新', 'OSS_UPDATE', 1782695353636618241, '/oss/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-04-23 17:17:09', 1, '2024-04-23 17:17:09', 0, 1728752845331021824, '1764842401228152834,-1,1782695353636618241');
INSERT INTO `tb_core_function` VALUES (1782700222615388162, 1728752347702018048, '删除', '删除', 'OSS_DELETE', 1782695353636618241, '/oss/delete/{ids}', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-04-23 17:17:09', 1, '2024-04-23 17:17:09', 0, 1728752845331021824, '1764842401228152834,-1,1782695353636618241');
INSERT INTO `tb_core_function` VALUES (1782700222615388163, 1728752347702018048, '新增', '新增', 'OSS_ADD', 1782695353636618241, '/oss/add', 0, 0, NULL, '_self', 0, NULL, NULL, NULL, NULL, 1, '2024-04-23 17:17:09', 1, '2024-04-23 17:17:27', 0, 1728752845331021824, '1764842401228152834,-1,1782695353636618241');
INSERT INTO `tb_core_function` VALUES (1782700222615388164, 1728752347702018048, '列表', '列表', 'OSS_LIST', 1782695353636618241, '/oss/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-04-23 17:17:09', 1, '2024-04-23 17:17:09', 0, 1728752845331021824, '1764842401228152834,-1,1782695353636618241');
INSERT INTO `tb_core_function` VALUES (1782700222804131841, 1728752347702018048, '查询租户配置', '查询租户配置', 'TENANT_CONFIG', 1782700222611193857, '/core/tenant/config', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, 1, '2024-04-23 17:17:09', 1, '2024-04-23 17:17:09', 0, 1728752845331021824, '-1,1728752752674652160,1782700222611193857');
INSERT INTO `tb_core_function` VALUES (1782942278277402626, 1728752347702018048, '上传类型', '上传类型', 'FILE_STORAGE_TYPE', 1782944188829011970, '/core/file/storageType', 2, 0, NULL, '_self', 0, NULL, NULL, NULL, NULL, 1, '2024-04-24 09:18:59', 1, '2024-04-24 09:26:57', 0, 1728752845331021824, '-1,1764842401228152834,1782944188829011970');
INSERT INTO `tb_core_function` VALUES (1782944188829011970, 1728752347702018048, '文件上传', '文件上传', 'SYSTEM_FILE_ADD', 1728752670466293760, '/core/file/upload', 0, 0, NULL, '_self', 1, NULL, NULL, NULL, NULL, 1, '2024-04-24 09:26:35', 1, '2024-04-24 09:26:35', 0, 1728752845331021824, '1728752670466293760,-1,1764842401228152834');
INSERT INTO `tb_core_function` VALUES (1782979167181602817, 1728752347702018048, '文件外链', '文件外链', 'FILE_URL', -1, '/core/file/url/{base}', 2, 0, NULL, '_self', 107, NULL, NULL, NULL, NULL, 1, '2024-04-24 11:45:34', 1, '2024-04-24 11:45:34', 0, 1728752845331021824, '-1');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_function_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_function_menu`;
CREATE TABLE `tb_core_function_menu` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `function_id` bigint(20) NOT NULL COMMENT '功能ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `function_id_fk` (`function_id`),
  KEY `menu_id_fk` (`menu_id`),
  CONSTRAINT `function_id_fk` FOREIGN KEY (`function_id`) REFERENCES `tb_core_function` (`id`) ON UPDATE RESTRICT,
  CONSTRAINT `menu_id_fk` FOREIGN KEY (`menu_id`) REFERENCES `tb_core_top_menu` (`id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='功能菜单顶级菜单关联表';

-- ----------------------------
-- Records of tb_core_function_menu
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_function_menu` VALUES (1728752827031273472, 1728752544486178816, 1728753037014908928, 1, '2022-10-27 00:47:37', 1, '2023-11-26 12:30:17', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1728752829493329920, 1728752468548304896, 1728753037014908928, 1, '2022-10-27 00:47:37', 1, '2023-11-26 12:30:17', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1728752830302830592, 1728752506099908608, 1728753042182291456, 1, '2022-10-27 19:08:34', 1, '2023-11-26 12:30:18', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1728752833800880128, 1728752510478761984, 1728753042182291456, 1, '2022-10-27 19:08:34', 1, '2023-11-26 12:30:18', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1728752834979479552, 1728752512726908928, 1728753044619182080, 1, '2022-10-27 19:11:49', 1, '2023-11-26 12:30:19', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1728752838708215808, 1728752516157849600, 1728753044619182080, 1, '2022-10-27 19:11:49', 1, '2023-11-26 12:30:19', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445602254849, 1728752787206356992, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445610643458, 1728752544486178816, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445610643459, 1728752468548304896, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445610643460, 1728752599901323264, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445610643461, 1764842401228152834, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445614837762, 1728752512726908928, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445614837763, 1728752516157849600, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445614837764, 1728752506099908608, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782691445619032065, 1728752510478761984, 1728753034091479040, 1, '2024-04-23 16:42:16', 1, '2024-04-23 16:42:16', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782694272441516034, 1728752599901323264, 1728753039409856512, 1, '2024-04-23 16:53:30', 1, '2024-04-23 16:53:30', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782694272441516035, 1764842401228152834, 1728753039409856512, 1, '2024-04-23 16:53:30', 1, '2024-04-23 16:53:30', 0, 1728752845331021824);
INSERT INTO `tb_core_function_menu` VALUES (1782694272441516036, 1728752512726908928, 1728753039409856512, 1, '2024-04-23 16:53:30', 1, '2024-04-23 16:53:30', 0, 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_org
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_org`;
CREATE TABLE `tb_core_org` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织机构编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织机构名称',
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '父级ID',
  `ancestor_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '祖级ID',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `head_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人姓名',
  `head_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人电话',
  `head_email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人邮箱',
  `contact_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人电话',
  `contact_email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
  `type` int(3) DEFAULT '0' COMMENT '机构类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Records of tb_core_org
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_org` VALUES (1728752845331021824, '000000', '牛逼科技', -1, '-1', NULL, 0, NULL, NULL, NULL, '公司老大', '11011071100', NULL, NULL, 1, NULL, 1, '2021-03-03 22:33:52', 1, '2023-11-26 12:29:32', 0, '000000', 1728752845331021824);
INSERT INTO `tb_core_org` VALUES (1728752862460559360, 'programmer', '程序员部门', 1728752845331021824, '1728752845331021824,-1', NULL, 0, '', '', '', '秃顶老大', '12012011200', '', '', 1, '', 1, '2021-03-03 22:33:52', 1, '2023-11-26 12:29:35', 0, '000000', 1728752845331021824);
INSERT INTO `tb_core_org` VALUES (1728752863471386624, 'TEST', '人事部门', 1728752845331021824, '1728752845331021824,-1', NULL, 0, '', '', '', '老大手下', '11011011001', '', '', 1, '', 1, '2021-03-03 22:33:52', 1, '2023-11-26 12:29:35', 0, '000000', 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_param`;
CREATE TABLE `tb_core_param` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数code',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数名称',
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数值',
  `note` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user` bigint(20) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '-1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统参数表';

-- ----------------------------
-- Records of tb_core_param
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_param` VALUES (1728752864696123392, 'JPOWER_IS_ACTIVATION', '新增用户默认是否激活', '1', '注册用户默认是否激活：1代表是，0代表否', 1, '2021-03-03 22:33:47', 1, '2023-11-26 12:29:36', 0, 1728752845331021824);
INSERT INTO `tb_core_param` VALUES (1728752866197684224, 'JPOWER_IS_REGISTER', '是否开启注册', 'false', '是否开启注册', 1, '2022-06-08 16:05:14', 1, '2023-11-26 13:19:11', 0, NULL);
INSERT INTO `tb_core_param` VALUES (1728752867351117824, 'JPOWER_USER_DEFAULT_PASSWORD', '用户默认登录密码', '123456', '系统用户默认登录密码', 1, '2021-03-03 22:33:47', 1, '2023-11-26 12:29:36', 0, 1728752845331021824);
INSERT INTO `tb_core_param` VALUES (1728752868500357120, 'REGISTER_ROLE', '注册用户角色ID', '', '注册用户角色ID', 1, '2022-06-08 16:10:18', 1, '2023-11-26 13:19:11', 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_post`;
CREATE TABLE `tb_core_post` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `type` int(11) DEFAULT NULL COMMENT '岗位类型 字典：POST_TYPE',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `describe` varchar(556) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位描述',
  `condition` varchar(556) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上岗条件',
  `create_user` bigint(20) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '-1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '是否启用 字典：YN01',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '000000' COMMENT '租户编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='岗位表';

-- ----------------------------
-- Records of tb_core_post
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_post` VALUES (1728752869339217920, 'JAVA', 'JAVA', 3, 1, '会写JAVA得程序员', '会JAVA', 1, '2022-09-17 16:01:52', 1, '2023-11-26 12:29:37', 1, 0, 1728752845331021824, '000000');
INSERT INTO `tb_core_post` VALUES (1728752871381843968, 'CS', '测试', 3, 3, '测试程序是否正常', '会测试', 1, '2022-09-17 16:02:39', 1, '2023-11-26 12:29:37', 0, 0, 1728752845331021824, '000000');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role`;
CREATE TABLE `tb_core_role` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `alias` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色别名',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `parent_id` bigint(20) DEFAULT '-1' COMMENT '上级ID',
  `icon_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标地址',
  `is_sys_role` int(11) DEFAULT '1' COMMENT '是否系统角色 0:否 1:是',
  `ancestor_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '祖级ID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_user` bigint(20) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '-1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of tb_core_role
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_role` VALUES (1, 'root', '超级管理员', -1, NULL, 1, '-1', '这是系统内置角色，不要删除，会影响功能', 1, 1, '2021-03-03 22:34:00', 1, '2023-11-26 12:29:33', 0, '000000', 1728752845331021824);
INSERT INTO `tb_core_role` VALUES (2, 'anonymous', '匿名用户', -1, NULL, 1, '-1', '这是系统内置角色，不要删除，会影响功能', 1, 1, '2021-03-03 22:34:00', 1, '2023-11-26 12:29:33', 0, '000000', 1728752845331021824);
INSERT INTO `tb_core_role` VALUES (1728752872212316160, 'ADMIN', '管理员', 1, NULL, 0, '-1,1', NULL, 0, 1, '2022-09-16 16:53:57', 1, '2023-11-26 12:29:38', 0, '000000', 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_role_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_data`;
CREATE TABLE `tb_core_role_data` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `data_id` bigint(20) NOT NULL COMMENT '数据权限ID',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`data_id`) USING BTREE,
  KEY `data_id_fk` (`data_id`),
  CONSTRAINT `data_id_fk` FOREIGN KEY (`data_id`) REFERENCES `tb_core_data_scope` (`id`) ON UPDATE RESTRICT,
  CONSTRAINT `role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `tb_core_role` (`id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色数据权限关联表';

-- ----------------------------
-- Table structure for tb_core_role_function
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_function`;
CREATE TABLE `tb_core_role_function` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `function_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`function_id`) USING BTREE,
  KEY `role_function_id_fk` (`function_id`),
  CONSTRAINT `role_function_id_fk` FOREIGN KEY (`function_id`) REFERENCES `tb_core_function` (`id`) ON UPDATE RESTRICT,
  CONSTRAINT `role_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `tb_core_role` (`id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
-- Records of tb_core_role_function
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_role_function` VALUES (1728752876196904960, 1728752872212316160, 1728752468548304896, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:39', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752877522305024, 1728752872212316160, 1728752473178816512, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:39', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752879279718400, 1728752872212316160, 1728752475166916608, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:39', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752880462512128, 1728752872212316160, 1728752477540892672, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:40', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752881406230528, 1728752872212316160, 1728752481009582080, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:40', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752882521915392, 1728752872212316160, 1728752482834104320, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:40', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752883427885056, 1728752872212316160, 1728752485220663296, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:40', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752884400963584, 1728752872212316160, 1728752488563523584, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:40', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752885285961728, 1728752872212316160, 1728752491759583232, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:41', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752886829465600, 1728752872212316160, 1728752494343274496, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:41', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752888423301120, 1728752872212316160, 1728752496788553728, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:41', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752889887113216, 1728752872212316160, 1728752498541772800, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:42', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752891254456320, 1728752872212316160, 1728752501008023552, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:42', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752892286255104, 1728752872212316160, 1728752503692378112, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:42', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752893838147584, 1728752872212316160, 1728752512726908928, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:43', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752895301959680, 1728752872212316160, 1728752539486568448, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:43', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752896547667968, 1728752872212316160, 1728752542833623040, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:43', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752897554300928, 1728752872212316160, 1728752544486178816, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:44', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752898565128192, 1728752872212316160, 1728752547980034048, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:44', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752899911499776, 1728752872212316160, 1728752549510955008, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:44', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752900913938432, 1728752872212316160, 1728752551624884224, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:44', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752901962514432, 1728752872212316160, 1728752553889808384, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:45', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752902813958144, 1728752872212316160, 1728752556381224960, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:45', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752903644430336, 1728752872212316160, 1728752565197651968, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:45', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752904500068352, 1728752872212316160, 1728752572306997248, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:45', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752905389260800, 1728752872212316160, 1728752577394688000, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:45', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752906341367808, 1728752872212316160, 1728752580817240064, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:46', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752907381555200, 1728752872212316160, 1728752583459651584, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:46', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752908845367296, 1728752872212316160, 1728752585988816896, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:46', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752909654867968, 1728752872212316160, 1728752588639617024, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:46', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752910602780672, 1728752872212316160, 1728752591604989952, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:47', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752911772991488, 1728752872212316160, 1728752593567924224, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:47', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752913052254208, 1728752872212316160, 1728752599901323264, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:47', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752914260213760, 1728752872212316160, 1728752605429415936, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:48', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752915174572032, 1728752872212316160, 1728752607845335040, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:48', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752916252508160, 1728752872212316160, 1728752609959264256, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:48', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752917343027200, 1728752872212316160, 1728752611938975744, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:48', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752918156722176, 1728752872212316160, 1728752614065487872, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:49', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752919075274752, 1728752872212316160, 1728752616913420288, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:49', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752920207736832, 1728752872212316160, 1728752618784079872, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:49', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752921046597632, 1728752872212316160, 1728752620776374272, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:49', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752922124533760, 1728752872212316160, 1728752623653666816, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:49', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752923311521792, 1728752872212316160, 1728752628695220224, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:50', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752924469149696, 1728752872212316160, 1728752630628794368, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:50', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752925698080768, 1728752872212316160, 1728752632491065344, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:50', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752927119949824, 1728752872212316160, 1728752634797932544, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:51', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752928499875840, 1728752872212316160, 1728752636916056064, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:51', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752929493925888, 1728752872212316160, 1728752639038373888, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:51', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752930513141760, 1728752872212316160, 1728752640732872704, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:51', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752931616243712, 1728752872212316160, 1728752642410594304, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:52', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752933033918464, 1728752872212316160, 1728752644541300736, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:52', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752934413844480, 1728752872212316160, 1728752646332268544, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:52', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752935655358464, 1728752872212316160, 1728752649532522496, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:53', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752936787820544, 1728752872212316160, 1728752651470290944, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:53', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752937958031360, 1728752872212316160, 1728752653533888512, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:53', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752938700423168, 1728752872212316160, 1728752655182249984, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:53', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752939769970688, 1728752872212316160, 1728752657212293120, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:54', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752940701106176, 1728752872212316160, 1728752659615629312, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:54', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752942009729024, 1728752872212316160, 1728752661452734464, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:54', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752942911504384, 1728752872212316160, 1728752663252090880, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:54', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752943834251264, 1728752872212316160, 1728752665244385280, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:55', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752944752803840, 1728752872212316160, 1728752666892746752, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:55', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752945805574144, 1728752872212316160, 1728752668935372800, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:55', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752946677989376, 1728752872212316160, 1728752670466293760, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:55', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752947663650816, 1728752872212316160, 1728752674559934464, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:56', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752948573814784, 1728752872212316160, 1728752676334125056, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:56', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752949370732544, 1728752872212316160, 1728752678917816320, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:56', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752950641606656, 1728752872212316160, 1728752680771698688, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:56', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752951753097216, 1728752872212316160, 1728752682763993088, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:57', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752952822644736, 1728752872212316160, 1728752684378800128, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:57', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752954009632768, 1728752872212316160, 1728752686949908480, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:57', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752955003682816, 1728752872212316160, 1728752688317251584, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:57', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752956203253760, 1728752872212316160, 1728752690909331456, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:58', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752957205692416, 1728752872212316160, 1728752692343783424, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:58', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752958275239936, 1728752872212316160, 1728752694231220224, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:58', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752959529336832, 1728752872212316160, 1728752695665672192, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:58', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752960733102080, 1728752872212316160, 1728752697632800768, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:59', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752962054307840, 1728752872212316160, 1728752700317155328, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:59', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752963937550336, 1728752872212316160, 1728752704528236544, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:29:59', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752964805771264, 1728752872212316160, 1728752706197569536, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:00', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752965581717504, 1728752872212316160, 1728752708013703168, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:00', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752967448182784, 1728752872212316160, 1728752709712396288, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:00', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752968819720192, 1728752872212316160, 1728752712317059072, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:01', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752970031874048, 1728752872212316160, 1728752716112904192, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:01', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752971562795008, 1728752872212316160, 1728752721745854464, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:01', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752972309381120, 1728752872212316160, 1728752723910115328, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:01', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752973148241920, 1728752872212316160, 1728752726028238848, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:02', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752974482030592, 1728752872212316160, 1728752728398020608, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:02', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752975832596480, 1728752872212316160, 1728752731355004928, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:02', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752976902144000, 1728752872212316160, 1728752733187915776, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:03', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752977841668096, 1728752872212316160, 1728752734903386112, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:03', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752978890244096, 1728752872212316160, 1728752737277362176, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:03', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752979980763136, 1728752872212316160, 1728752738820866048, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:03', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752981121613824, 1728752872212316160, 1728752741018681344, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:04', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752982136635392, 1728752872212316160, 1728752742557990912, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:04', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752983415898112, 1728752872212316160, 1728752744407678976, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:04', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752984196038656, 1728752872212316160, 1728752746290921472, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:04', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752985320112128, 1728752872212316160, 1728752748673286144, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:05', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752986326745088, 1728752872212316160, 1728752750640414720, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:05', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752987517927424, 1728752872212316160, 1728752763823112192, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:05', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752988864299008, 1728752872212316160, 1728752767056920576, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:05', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752989610885120, 1728752872212316160, 1728752769846132736, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:06', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752990537826304, 1728752872212316160, 1728752771544825856, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:06', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752991997444096, 1728752872212316160, 1728752773285462016, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:06', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752992907608064, 1728752872212316160, 1728752775151927296, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:06', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752993968766976, 1728752872212316160, 1728752777530097664, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:07', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752995130589184, 1728752872212316160, 1728752779589500928, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:07', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752996468572160, 1728752872212316160, 1728752781363691520, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:07', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752997286461440, 1728752872212316160, 1728752782907195392, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:07', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752998200819712, 1728752872212316160, 1728752785151148032, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:08', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728752999220035584, 1728752872212316160, 1728752790679240704, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:08', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753000675459072, 1728752872212316160, 1728752792348573696, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:08', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753001833086976, 1728752872212316160, 1728752794315702272, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:08', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753002881662976, 1728752872212316160, 1728752796102475776, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:09', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753004106399744, 1728752872212316160, 1728752798287708160, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:09', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753005540851712, 1728752872212316160, 1728752799822823424, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:09', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753006606204928, 1728752872212316160, 1728752801504739328, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:10', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753007788998656, 1728752872212316160, 1728752803429924864, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:10', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753009194090496, 1728752872212316160, 1728752805208309760, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:10', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753010397855744, 1728752872212316160, 1728752807376764928, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:11', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753011500957696, 1728752872212316160, 1728752809180315648, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:11', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753012675362816, 1728752872212316160, 1728752811646566400, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:11', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753013933654016, 1728752872212316160, 1728752813676609536, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:11', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753014915121152, 1728752872212316160, 1728752815874424832, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:12', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753016152440832, 1728752872212316160, 1728752817375985664, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:12', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753017515589632, 1728752872212316160, 1728752819838042112, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:12', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753018731937792, 1728752872212316160, 1728752821935194112, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:13', 0, 1728752845331021824);
INSERT INTO `tb_core_role_function` VALUES (1728753019918925824, 2, 1728752757401632768, 1, '2023-04-23 15:21:53', 1, '2023-11-26 12:30:13', 0, 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_menu`;
CREATE TABLE `tb_core_role_menu` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `menu_role_id_fk` (`role_id`),
  KEY `role_menu_menu_id_fk` (`menu_id`),
  CONSTRAINT `menu_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `tb_core_role` (`id`) ON UPDATE RESTRICT,
  CONSTRAINT `role_menu_menu_id_fk` FOREIGN KEY (`menu_id`) REFERENCES `tb_core_top_menu` (`id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色顶级菜单关联表';

-- ----------------------------
-- Records of tb_core_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_role_menu` VALUES (1728753021584064512, 1728752872212316160, 1728753044619182080, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:19', 0, 1728752845331021824);
INSERT INTO `tb_core_role_menu` VALUES (1728753022657806336, 1, 1728753034091479040, 1, '2023-04-23 12:29:47', 1, '2023-11-26 12:30:17', 0, 1728752845331021824);
INSERT INTO `tb_core_role_menu` VALUES (1728753023349866496, 1, 1728753042182291456, 1, '2023-04-23 12:29:47', 1, '2023-11-26 12:30:18', 0, 1728752845331021824);
INSERT INTO `tb_core_role_menu` VALUES (1728753024515883008, 1, 1728753044619182080, 1, '2023-04-23 12:29:47', 1, '2023-11-26 12:30:19', 0, 1728752845331021824);
INSERT INTO `tb_core_role_menu` VALUES (1728753025895809024, 1, 1728753037014908928, 1, '2023-04-23 12:29:47', 1, '2023-11-26 12:30:17', 0, 1728752845331021824);
INSERT INTO `tb_core_role_menu` VALUES (1728753027363815424, 1728752872212316160, 1728753039409856512, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:18', 0, 1728752845331021824);
INSERT INTO `tb_core_role_menu` VALUES (1728753028232036352, 1728752872212316160, 1728753034091479040, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:17', 0, 1728752845331021824);
INSERT INTO `tb_core_role_menu` VALUES (1728753029255446528, 1728752872212316160, 1728753037014908928, 1, '2023-04-23 15:44:54', 1, '2023-11-26 12:30:17', 0, 1728752845331021824);
INSERT INTO `tb_core_role_menu` VALUES (1728753031096745984, 1, 1728753039409856512, 1, '2023-04-23 12:29:47', 1, '2023-11-26 12:30:18', 0, 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_tenant
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_tenant`;
CREATE TABLE `tb_core_tenant` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编码',
  `tenant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `domain` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '域名',
  `logo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户Logo',
  `contact_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人电话',
  `address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
  `account_number` int(11) DEFAULT '-1' COMMENT '账号额度',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `license_key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权码',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='租户表';

-- ----------------------------
-- Records of tb_core_tenant
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_tenant` VALUES (1728753032510226432, '000000', '管理组', '', '', '老总', '15011071226', '', -1, NULL, 'e15fca1478f6e9b4', 1, '2021-03-03 22:34:15', 1, '2023-11-26 12:30:16', 0, 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_top_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_top_menu`;
CREATE TABLE `tb_core_top_menu` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `client_id` bigint(20) NOT NULL COMMENT '客户端ID',
  `code` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单编号',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `router` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '首页路由',
  `sort_num` int(11) NOT NULL DEFAULT '1' COMMENT '排序',
  `note` varchar(525) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注说明',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1启用 0停用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `menu_client_id_fk` (`client_id`),
  CONSTRAINT `menu_client_id_fk` FOREIGN KEY (`client_id`) REFERENCES `tb_core_client` (`id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='顶级菜单表';

-- ----------------------------
-- Records of tb_core_top_menu
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_top_menu` VALUES (1728753034091479040, 1728752347702018048, 'ALL', '全部', 'iconfont iconicon_work', '', 0, NULL, 1, '2022-10-26 21:51:19', 1, '2024-04-23 16:40:31', 1, 0, 1728752845331021824);
INSERT INTO `tb_core_top_menu` VALUES (1728753037014908928, 1728752347702018048, 'QX', '权限管理', 'iconfont icon-yanzhengma', '', 0, NULL, 1, '2022-10-27 00:11:35', 1, '2024-04-23 16:40:34', 1, 0, 1728752845331021824);
INSERT INTO `tb_core_top_menu` VALUES (1728753039409856512, 1728752347702018048, 'SYSTEM', '系统设置', 'iconfont iconicon_setting', '', 3, NULL, 1, '2022-10-27 19:04:32', 1, '2024-04-23 16:40:38', 1, 0, 1728752845331021824);
INSERT INTO `tb_core_top_menu` VALUES (1728753042182291456, 1728752347702018048, 'JPOWER', 'Jpower介绍', 'iconfont iconicon_affiliations_li', '', 10, NULL, 1, '2022-10-27 19:08:13', 1, '2024-04-23 16:40:48', 1, 0, 1728752845331021824);
INSERT INTO `tb_core_top_menu` VALUES (1728753044619182080, 1728752347702018048, 'FUWU', '服务监控', 'iconfont icon-wxbgongju', '', 4, NULL, 1, '2022-10-27 19:09:21', 1, '2024-04-23 16:40:42', 1, 0, 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_user`;
CREATE TABLE `tb_core_user` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织机构主键',
  `post_id` bigint(20) DEFAULT NULL COMMENT '岗位ID',
  `login_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '真实姓名',
  `id_type` tinyint(1) DEFAULT '1' COMMENT '证件类型 1:身份证 2:中国护照 3:台胞证 4:外国护照 5:外国人永居证',
  `id_no` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '证件编码',
  `user_type` tinyint(1) DEFAULT '0' COMMENT '用户类型 0:系统用户 1：普通用户 2：单位用户 3:会员 9：匿名用户',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱地址',
  `telephone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通讯地址',
  `post_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮编',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `login_count` int(11) DEFAULT '0' COMMENT '登录次数',
  `nick_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `other_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方平台标识',
  `activation_status` tinyint(1) DEFAULT '1' COMMENT '激活状态 1：激活 0：未激活',
  `activation_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '激活码',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '最后更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编码',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `post_id_fk` (`post_id`),
  KEY `org_id_fk` (`org_id`),
  CONSTRAINT `org_id_fk` FOREIGN KEY (`org_id`) REFERENCES `tb_core_org` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `post_id_fk` FOREIGN KEY (`post_id`) REFERENCES `tb_core_post` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统登录用户表';

-- ----------------------------
-- Records of tb_core_user
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_user` VALUES (1, 1728752845331021824, NULL, 'root', '$2a$05$zQGxuIbCA/nulLcaVhyvfebr3vtlA/I5nrS9U1dK7zcsSn27q4M.2', '', '超级管理员', 1, NULL, 0, NULL, '1634566606@qq.com', '15011071226', '内蒙古', '012000', '2024-04-24 16:17:16', 1549, '超级用户', '', 1, '', 1, '2021-03-03 22:34:20', 2, '2024-04-24 16:17:16', 0, '000000', 1728752845331021824);
INSERT INTO `tb_core_user` VALUES (2, 1728752845331021824, NULL, 'anonymous', '', NULL, '匿名用户', 1, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, '匿名用户', NULL, 1, NULL, 1, '2021-03-03 22:34:20', 1, '2023-11-26 12:29:33', 0, '000000', 1728752845331021824);
INSERT INTO `tb_core_user` VALUES (1728753047802658816, 1728752845331021824, 1728752869339217920, 'admin', '$2a$05$JdGqOXozOpWLV5fhDncCNue7vrWcZS/OKyEYUMPDGTmkOYSd6akQy', NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2023-04-23 15:45:16', 10, '管理员', NULL, 1, NULL, 1, '2022-09-16 16:57:32', 2, '2023-11-26 12:30:19', 0, '000000', 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_user_role`;
CREATE TABLE `tb_core_user_role` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`user_id`) USING BTREE,
  KEY `user_user_id_fk` (`user_id`),
  CONSTRAINT `user_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `tb_core_role` (`id`) ON UPDATE RESTRICT,
  CONSTRAINT `user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `tb_core_user` (`id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of tb_core_user_role
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_user_role` VALUES (1728753051489452032, 1, 1, 1, '2021-03-03 22:34:25', 1, '2023-11-26 12:30:20', 0, 1728752845331021824);
INSERT INTO `tb_core_user_role` VALUES (1728753052286369792, 1728752872212316160, 1728753047802658816, 1, '2022-09-17 16:30:25', 1, '2023-11-26 12:30:21', 0, 1728752845331021824);
INSERT INTO `tb_core_user_role` VALUES (1728753053506912256, 2, 2, 1, '2021-03-03 22:34:25', 1, '2023-11-26 12:30:21', 0, 1728752845331021824);
COMMIT;

-- ----------------------------
-- Table structure for tb_log_error
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_error`;
CREATE TABLE `tb_log_error` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `server_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `server_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务器ip',
  `server_host` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务器名',
  `env` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '环境',
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求接口',
  `method` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作方式',
  `method_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法类',
  `method_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法名',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `oper_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作IP地址',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人员',
  `oper_user_type` tinyint(1) DEFAULT NULL COMMENT '操作人员类型，是系统用户还是业务用户 0系统1业务2白名单',
  `client_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作客户端',
  `error` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误信息',
  `line_number` int(11) DEFAULT NULL COMMENT '报错行号',
  `exception_name` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常名称',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '异常信息',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='错误日志表';

-- ----------------------------
-- Table structure for tb_log_monitor_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_param`;
CREATE TABLE `tb_log_monitor_param` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `server` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `path` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '监控地址',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方式',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数类型 字典 PARAM_TYPE（header、path、body、query）',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数名称',
  `value` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数值',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='接口监控参数表';

-- ----------------------------
-- Table structure for tb_log_monitor_result
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_result`;
CREATE TABLE `tb_log_monitor_result` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测试地址',
  `tags` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求接口',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方式',
  `error` varchar(289) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求错误',
  `respose` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应数据',
  `respose_code` int(11) DEFAULT NULL COMMENT '响应编码',
  `restful_response` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '接口返回数据',
  `header` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'header参数',
  `body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'body参数',
  `is_success` int(11) DEFAULT NULL COMMENT '是否成功 0否 1是',
  `response_time` int(11) DEFAULT NULL COMMENT '执行时长 单位毫秒',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='接口监控详情';

-- ----------------------------
-- Table structure for tb_log_monitor_setting
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_setting`;
CREATE TABLE `tb_log_monitor_setting` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `server` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `path` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '监控地址',
  `tag` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方式',
  `is_monitor` int(11) DEFAULT '3' COMMENT '是否监控 0:否 1:是 3:未设置',
  `code` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '200' COMMENT 'respose正确code,多个逗号分割',
  `exec_js` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'js代码',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='接口监控设置表';

-- ----------------------------
-- Table structure for tb_log_operate
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_operate`;
CREATE TABLE `tb_log_operate` (
  `id` bigint(20) NOT NULL COMMENT '主建',
  `server_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `server_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务器ip',
  `server_host` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务器名',
  `env` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '环境',
  `url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求接口',
  `method` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作方式',
  `method_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法类',
  `method_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '方法名',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `oper_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作IP地址',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人员',
  `oper_user_type` tinyint(1) DEFAULT NULL COMMENT '操作人员类型，是系统用户还是业务用户 0系统1业务2白名单',
  `client_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作客户端',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作标题',
  `business_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务类型（OTHER=其它,INSERT=新增,UPDATE=修改,DELETE=删除,GRANT=授权,EXPORT=导出,IMPORT=导入,FORCE=强退,GENCODE=生成代码,CLEAN=清空数据,REVIEW=审核）',
  `return_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '记录内容',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '返回内容',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误消息',
  `record_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '记录ID',
  `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '操作状态（0正常 1异常）',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='操作日志表';

-- ----------------------------
-- Table structure for tb_resource_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource_file`;
CREATE TABLE `tb_resource_file` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名称',
  `file_size` int(11) DEFAULT NULL COMMENT '文件大小 单位：字节',
  `file_type` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件类型',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件路径',
  `content` longblob COMMENT '文件内容',
  `mark` varchar(156) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件标识',
  `storage_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'SERVER' COMMENT '存储类型 字典FILE_STORAGE_TYPE',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user` bigint(20) DEFAULT '1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '最后更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文件表';

-- ----------------------------
-- Table structure for tb_resource_oss
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource_oss`;
CREATE TABLE `tb_resource_oss` (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `category` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类 字典：OSS_CATEGORY',
  `name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编号',
  `access_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'accessKey',
  `secret_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'secretKey',
  `internal_address` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '内网资源地址',
  `external_address` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '外链地址',
  `bucket_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '存储桶',
  `create_user` bigint(20) DEFAULT '1' COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '最后更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='OSS配置表';

-- ----------------------------
-- Table structure for tb_resource_sms
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource_sms`;
CREATE TABLE `tb_resource_sms` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `category` varchar(10) COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类 字典：SMS_CATEGORY',
  `code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编号',
  `template` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板ID',
  `access_key` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'accessKey',
  `secret_key` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'secretKey',
  `sign` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '短信签名',
  `parameters` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送参数',
  `region_id` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区域ID',
  `create_user` bigint(20) DEFAULT '1' COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '最后更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` bigint(20) DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='短信配置表';

-- ----------------------------
-- Records of tb_resource_sms
-- ----------------------------
BEGIN;
INSERT INTO `tb_resource_sms` VALUES (1765182328482582529, '测试', 'ali', 'validate', 'fewfew', 'dsfsdfes', 'fewr2332r', 'grfg', 'fgrefgre,ewe3', 'ergerg', 1, '2024-03-06 09:07:17', 1, '2024-04-23 16:44:27', 0, 1728752845331021824);
COMMIT;

CREATE TABLE `tb_core_city` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '城市编码',
    `pcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上级编码',
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `fullname` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '全称',
    `rankd` int(11) DEFAULT NULL COMMENT '级别 1：省份/直辖市 2：地市 3：区县 4：乡镇/街道 5：村委',
    `lng` double(10,6) DEFAULT NULL COMMENT '经度',
    `lat` double(10,6) DEFAULT NULL COMMENT '维度',
    `country_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家编码',
    `city_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市类型1：首都、2：直辖市、3：地级市、4县级市、9：其他',
    `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `sort_num` int(11) DEFAULT '0' COMMENT '排序',
    `create_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` bigint(20) NOT NULL DEFAULT '1' COMMENT '更新人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
    `create_org` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `tb_core_city_code_index` (`code`) USING BTREE,
    KEY `tb_core_city_code_name_index` (`code`,`name`) USING BTREE,
    KEY `tb_core_city_pcode_index` (`pcode`) USING BTREE,
    KEY `tb_core_city_rankd_index` (`rankd`) USING BTREE,
    KEY `tb_core_city_fullname_index` (`fullname`) USING BTREE,
    KEY `code_index` (`code`) USING BTREE,
    KEY `pcode_index` (`pcode`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='城市地区表'

SET FOREIGN_KEY_CHECKS = 1;
