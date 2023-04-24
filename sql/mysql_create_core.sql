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

 Date: 23/04/2023 15:56:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_core_city
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_city`;
CREATE TABLE `tb_core_city` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '城市编码',
  `pcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上级编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `fullname` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '全称',
  `rankd` int(6) DEFAULT NULL COMMENT '级别 1：省份/直辖市 2：地市 3：区县 4：乡镇/街道 5：村委',
  `lng` double(10,6) DEFAULT NULL COMMENT '经度',
  `lat` double(10,6) DEFAULT NULL COMMENT '维度',
  `country_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家编码',
  `city_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市类型1：首都、2：直辖市、3：地级市、4县级市、9：其他',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `sort_num` int(6) DEFAULT '0' COMMENT '排序',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `tb_core_city_code_index` (`code`) USING BTREE,
  KEY `tb_core_city_code_name_index` (`code`,`name`) USING BTREE,
  KEY `tb_core_city_pcode_index` (`pcode`) USING BTREE,
  KEY `tb_core_city_rankd_index` (`rankd`) USING BTREE,
  KEY `tb_core_city_fullname_index` (`fullname`) USING BTREE,
  KEY `code_index` (`code`) USING BTREE,
  KEY `pcode_index` (`pcode`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='城市地区表';

-- ----------------------------
-- Table structure for tb_core_client
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_client`;
CREATE TABLE `tb_core_client` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端名称',
  `client_code` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端CODE，唯一约束',
  `client_secret` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端密钥',
  `access_token_validity` int(10) NOT NULL DEFAULT '0' COMMENT 'token过期时间，单位秒',
  `refresh_token_validity` int(10) NOT NULL DEFAULT '0' COMMENT '刷新token时间，单位秒，时间应该比token过期时间更长',
  `login_limit` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录限制',
  `sort_num` int(6) DEFAULT NULL COMMENT '排序',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_client_code` (`client_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='客户端系统表';

-- ----------------------------
-- Records of tb_core_client
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_client` VALUES ('75cfc202d34f11ea97e4a34c90effc21', '后台管理平台', 'admin', 'SCewmm', 1800, 2400, 'NONE', 1, NULL, '1', '2021-03-03 22:33:10', '1', '2022-11-11 19:09:07', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_data_scope
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_data_scope`;
CREATE TABLE `tb_core_data_scope` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `menu_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单主键',
  `scope_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编号',
  `scope_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限名称',
  `scope_field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限字段',
  `scope_class` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限类名',
  `scope_column` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '*' COMMENT '数据可见字段',
  `scope_type` tinyint(1) DEFAULT NULL COMMENT '数据权限类型 字典：DATA_SCOPE_TYPE',
  `scope_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限值域',
  `all_role` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否所有角色都执行',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限备注',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_scope_code` (`scope_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='数据权限表';

-- ----------------------------
-- Records of tb_core_data_scope
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_data_scope` VALUES ('144bf26e9976d1b7b7f149840c508a48', 'a4a0a4aad34711ea97dxa34c90effc21', 'FILE_ALL', '文件管理 [全部可见]', '*', 'com.wlcb.dbs.dao.mapper.selectPage', '-', 1, '', 0, NULL, '1', '2021-03-03 22:33:17', '1', '2021-03-03 22:33:17', '6836de3b179d11eb8189fa163e5c4fd4', 1, 0);
INSERT INTO `tb_core_data_scope` VALUES ('32e27aae5aceeac3ca2a1ea534024ff0', 'a4a0a4aad34711ea97dxa34c90effc21', 'FILE_USER', '文件管理 [本人可见]', '*', 'com.wlcb.dbs.dao.mapper.selectPage', 'create_user', 2, 'create_user = {userId}', 0, NULL, '1', '2021-03-03 22:33:17', '1', '2021-03-03 22:33:17', '6836de3b179d11eb8189fa163e5c4fd4', 1, 0);
INSERT INTO `tb_core_data_scope` VALUES ('4a14323e125259dc0f1cc2cae32e77a5', 'a4a0a4aad34711ea97dxa34c90effc21', 'FILE_ORG_CHILD', '文件管理 [所在机构可见及子级可见]', '*', 'com.wlcb.dbs.dao.mapper.selectPage', 'create_org', 4, 'create_org in {orgId}', 0, NULL, '1', '2021-03-03 22:33:17', '1', '2021-03-03 22:33:17', '6836de3b179d11eb8189fa163e5c4fd4', 1, 0);
INSERT INTO `tb_core_data_scope` VALUES ('c0d68b39f021bf49a7f1429a42339183', 'a49e8d5ad34711ea97e4a34c90effc21', 'ORG_CHILD', '部门管理 [所在机构可见及子级可见]', NULL, 'com.wlcb.dbs.dao.org.mapper.TbCoreOrgMapper.listLazyByParent', 'id', 4, 'id in {orgId}', 0, NULL, '1', '2022-09-15 16:36:12', '1', '2022-09-15 16:36:12', '6836de3b179d11eb8189fa163e5c4fd4', 1, 0);
INSERT INTO `tb_core_data_scope` VALUES ('db9448014f2c4005d45406886559b2f3', 'a4a0a4aad34711ea97dxa34c90effc21', 'FILE_ORG', '文件管理 [所在机构可见]', '*', 'com.wlcb.dbs.dao.mapper.selectPage', 'create_org', 3, 'create_org = {orgId}', 0, NULL, '1', '2021-03-03 22:33:17', '1', '2021-03-03 22:33:17', '6836de3b179d11eb8189fa163e5c4fd4', 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_dict
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_dict`;
CREATE TABLE `tb_core_dict` (
  `id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `dict_type_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型代码',
  `code` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典代码',
  `name` varchar(250) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `is_stop` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '是否停用',
  `parent_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '-1' COMMENT '上级ID',
  `locale` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'zh' COMMENT '语言 zh en',
  `note` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `sort_num` int(6) DEFAULT '0' COMMENT '排序',
  `create_user` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `dict_level` int(6) DEFAULT NULL COMMENT '树形字典结构的级别',
  `pcode` varchar(100) COLLATE utf8mb4_general_ci DEFAULT '-1' COMMENT '上级代码',
  `tenant_code` varchar(6) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='字典表';

-- ----------------------------
-- Records of tb_core_dict
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_dict` VALUES ('042da36f48ee1f7b770b9bc268d92ef7', 'DKFS', '_parent', '_parent', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('0ef40a2ab64209d0f3a5b6a7497567cf', 'DATA_SCOPE_TYPE', '1', '全部可见', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('11d904cc5adb42f6474f0b7f71248a8d', 'DKFS', '_top', '_top', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('170626gf1747hklx10a8de9a4347f5b', 'BUSINESS_TYPE', 'REVIEW', '审核', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:37', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('170626gf17d9jklx10a8de9a4347f5b', 'BUSINESS_TYPE', 'CLEAN', '清空数据', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:37', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('170626gf17d9yulx10a8de9a4347f5b', 'OPERATE_STATUS', '0', '正常', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:37', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('170626gfu7d9jklx10a8de9a4347f5b', 'OPERATE_STATUS', '1', '异常', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:37', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('17062df17d9da95103a8de9a4347fd5b', 'PARAM_TYPE', 'header', 'header', 'N', '-1', 'zh', '', 0, '1', '2021-04-27 17:15:36', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('17062df17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'OTHER', '其它', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:35', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('17062rf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'INSERT', '新增', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:35', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('17064ff17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'GENCODE', '生成代码', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:37', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1706asf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'EXPORT', '导出', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:36', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1706fgf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'UPDATE', '修改', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:36', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1706q3f17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'FORCE', '强退', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:36', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1706vbf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'GRANT', '授权', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:36', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1706yuf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'DELETE', '删除', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:36', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1706zxf17d9jklx103a8de9a4347f5b', 'BUSINESS_TYPE', 'IMPORT', '导入', 'N', '-1', 'zh', '', 0, '1', '2022-05-10 23:30:36', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1b31a29bc63f5729ba7732dbe878c4b6', 'FILE_STORAGE_TYPE', 'SERVER', '服务器', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-05 16:58:37', '329e4e57adcdcb4b832074086ea9384d', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1b31a29br53f5729ba7732dbe878c4b6', 'FILE_STORAGE_TYPE', 'DATABASE', '数据库', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-05 16:58:37', '329e4e57adcdcb4b832074086ea9384d', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1b31a29bsz3f5729ba7732dbe878c4b6', 'FILE_STORAGE_TYPE', 'FASTDFS', 'fastdfs', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-05 16:58:37', '329e4e57adcdcb4b832074086ea9384d', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1dcb2f6806a14c96dd59b2470287e23d', 'FUNCTION_TYPE', '0', '按钮', 'N', '-1', 'zh', NULL, 0, '1', '2022-11-16 01:09:30', '1', '2022-11-16 01:09:30', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1ebf911a58af5dbe5edf7d1040913037', 'DKFS', '_blank', '_blank', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('1fe791cd18043c28ca8560a53edad78b', 'FUNCTION_TYPE', '2', '接口', 'N', '-1', 'zh', NULL, 0, '1', '2022-11-16 01:10:45', '1', '2022-11-16 01:10:45', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('38105ad47be56df20eaa40018e665e94', 'YYZL', 'ja', '日文', 'N', '-1', 'zh', NULL, 0, '1', '2022-10-09 19:44:55', '1', '2022-10-09 19:44:55', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3a54a00ea3f1b721a88fd40dfd4050d1', 'PARAM_TYPE', 'path', 'path', 'N', '-1', 'zh', '', 1, '1', '2021-04-27 17:15:46', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3aab45eed34e11ea97e4a34c90effc21', 'CITY_TYPE', '1', '首都', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3aae10bcd34e11ea97e4a34c90effc21', 'CITY_TYPE', '2', '直辖市', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ab01632d34e11ea97e4a34c90effc21', 'CITY_TYPE', '3', '地级市', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ab1e21ed34e11ea97e4a34c90effc21', 'CITY_TYPE', '4', '县级市', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ab3cea8d34e11ea97e4a34c90effc21', 'CITY_TYPE', '9', '其他', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ab5a0fcd34e11ea97e4a34c90effc21', 'CITY_LEVEL', '1', '省份/直辖市', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ab78282d34e11ea97e4a34c90effc21', 'CITY_LEVEL', '2', '地市', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ab93fa0d34e11ea97e4a34c90effc21', 'CITY_LEVEL', '3', '区县', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3abb673ad34e11ea97e4a34c90effc21', 'CITY_LEVEL', '4', '乡镇/街道', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3abd1e54d34e11ea97e4a34c90effc21', 'CITY_LEVEL', '5', '村委', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3abf0732d34e11ea97e4a34c90effc21', 'YYZL', 'zh', '中文', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ac0a132d34e11ea97e4a34c90effc21', 'YYZL', 'en', '英文', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ac2a374d34e11ea97e4a34c90effc21', 'YN', 'Y', '是', 'N', '-1', 'zh', NULL, 2, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:41:13', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ac41fb0d34e11ea97e4a34c90effc21', 'YN', 'N', '否', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:41:13', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ac5f506d34e11ea97e4a34c90effc21', 'YN01', '1', '是', 'N', '-1', 'zh', NULL, 2, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ac7a338d34e11ea97e4a34c90effc21', 'YN01', '0', '否', 'N', '-1', 'zh', 'null', 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:14:40', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ac96b14d34e11ea97e4a34c90effc21', 'ID_TYPE', '1', '身份证', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3acf7946d34e11ea97e4a34c90effc21', 'ID_TYPE', '2', '中国护照', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ad1086ad34e11ea97e4a34c90effc21', 'ID_TYPE', '3', '台胞证', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ad2d410d34e11ea97e4a34c90effc21', 'ID_TYPE', '4', '外国护照', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ad430f8d34e11ea97e4a34c90effc21', 'ID_TYPE', '5', '外国人永居证', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ad5ef88d34e11ea97e4a34c90effc21', 'USER_TYPE', '0', '系统用户', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ad74ae0d34e11ea97e4a34c90effc21', 'USER_TYPE', '1', '普通用户', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ad8fed0d34e11ea97e4a34c90effc21', 'USER_TYPE', '2', '单位用户', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ada5e38d34e11ea97e4a34c90effc21', 'USER_TYPE', '3', '会员', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3adbaf2cd34e11ea97e4a34c90effc21', 'USER_TYPE', '9', '匿名用户', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3adbaf2cd34e11ea97e4a34c90rffc21', 'USER_TYPE', '4', '客服用户', 'Y', '-1', 'zh', NULL, 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 20:33:48', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3add39aad34e11ea97e4a34c90effc21', 'XBIE', 'FEMALE', '女', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('3ade7ce8d34e11ea97e4a34c90effc21', 'XBIE', 'MAN', '男', 'N', '-1', 'zh', NULL, 1, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('5f8c538867635684e02a63f99700b80f', 'POST_TYPE', '1', '领导', 'N', '-1', 'zh', NULL, 0, '1', '2022-09-17 15:37:34', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('680467c0fe28f1b9b0dba5e3dec166f2', 'DKFS', '_self', '_self', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('7c3398567fe2551836b0b8bdc49a4e70', 'LOGIN_LIMIT', 'NONE', '不限制', 'N', '-1', 'zh', NULL, 0, '1', '2022-11-11 19:06:03', '1', '2022-11-11 19:06:27', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('80d4e15e81dc389365bcd17cc9603d58', 'FUNCTION_TYPE', '1', '菜单', 'N', '-1', 'zh', NULL, 0, '1', '2022-11-16 01:10:17', '1', '2022-11-16 01:10:17', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('81442a92c65be42291350078b320645f', 'LOGIN_LIMIT', 'ONE', '单模式', 'N', '-1', 'zh', NULL, 0, '1', '2022-11-11 19:06:14', '1', '2022-11-11 19:06:14', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('89290d1c6cdda60e5dd9a8dd675c5923', 'LOGIN_LIMIT', 'SQUEEZE', '挤掉模式', 'N', '-1', 'zh', NULL, 0, '1', '2022-11-11 19:06:44', '1', '2022-11-11 19:06:44', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('976db32950232e2d183857e13e25d8fd', 'DATA_SCOPE_TYPE', '2', '本人可见', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('9ecb571f3640ae36818544f0a575892d', 'DATA_SCOPE_TYPE', '4', '所在机构及子级可见', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('a3dda135564dee5ce443145f6e1b4266', 'DATA_SCOPE_TYPE', '5', '自定义', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('a52a54d2b562466f8d2a968f6543e039', 'DATA_SCOPE_TYPE', '3', '所在机构可见', 'N', '-1', 'zh', '', 0, '1', '2021-03-03 22:33:23', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('abc302d0db1b1d1569e9992be60695e8', 'PARAM_TYPE', 'body', 'body', 'N', '-1', 'zh', '', 3, '1', '2021-04-27 17:15:58', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('abf41076fdbe1395d35346e5c2d7a9ee', 'POST_TYPE', '2', '普工', 'N', '-1', 'zh', NULL, 0, '1', '2022-09-17 15:37:49', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('be1b4abf6163cdc783ae1e7008a5b241', 'POST_TYPE', '3', '技工', 'N', '-1', 'zh', NULL, 0, '1', '2022-09-17 15:38:00', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict` VALUES ('cdd0bf652c365ff8b54eaed2763fa6de', 'PARAM_TYPE', 'query', 'query', 'N', '-1', 'zh', '', 4, '1', '2021-04-27 17:16:08', '1', '2022-10-09 19:12:28', 1, 0, NULL, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_dict_type`;
CREATE TABLE `tb_core_dict_type` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `dict_type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型代码',
  `dict_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型名称',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `del_enabled` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'N' COMMENT '是否允许删除 N:不允许 Y允许',
  `sort_num` int(6) DEFAULT '0' COMMENT '排序',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '-1' COMMENT '父字典类型ID',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `is_tree` tinyint(1) DEFAULT NULL COMMENT '是否树形结构 字典YN01',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`dict_type_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- ----------------------------
-- Records of tb_core_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_dict_type` VALUES ('1cde0346b04ff199a6eedc4b5e7e957a', 'POST_TYPE', '岗位类型', NULL, 'Y', 10, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2022-09-17 15:37:08', '1', '2022-09-17 15:37:08', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35b7cae4d34e11ea97e4a34c90effc21', 'TOP_SYSTEM_CORE_ZDIAN', '核心-基础字典', NULL, 'N', 1, '-1', '1', '2021-03-03 22:33:28', '1', '2021-03-04 00:07:06', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35b9e892d34e11ea97e4a34c56effc21', 'FILE_STORAGE_TYPE', '文件存储类型', NULL, 'N', 1, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '329e4e57adcdcb4b832074086ea9384d', '2022-09-16 17:02:06', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35b9e892d34e11ea97e4a34c90effc21', 'CITY_TYPE', '城市类型', NULL, 'N', 1, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, NULL, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35bc35f2d34e11ea97e4a34c90effc21', 'CITY_LEVEL', '行政区级别', NULL, 'N', 1, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35be8fe6d34e11ea97e4a34c90effc21', 'YYZL', '语言种类', NULL, 'N', 2, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35c0dcbad34e11ea97e4a34c90effc21', 'YN', '是否', NULL, 'N', 3, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2022-10-09 19:41:13', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35c394aad34df1ea97e4a34c90effc21', 'DKFS', '页面打开方式', NULL, 'N', 3, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2022-09-14 16:34:19', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35c394aad34e11ea97e4a34c90effc21', 'YN01', '是否01', NULL, 'N', 3, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35c5c23ed34e11ea97e4a34c90effc21', 'ID_TYPE', '证件类型', NULL, 'N', 4, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, NULL, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35c800eed34e11ea97e4a34c90effc21', 'USER_TYPE', '用户类型', NULL, 'N', 4, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, NULL, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('35ca0a42d34e11ea97e4a34c90effc21', 'XBIE', '性别', NULL, 'N', 4, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, NULL, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('52cd701b216e592806acbefe0d75bb05', 'PARAM_TYPE', '参数类型', '', 'Y', 1, 'a5c94db5490032b8e0f39e64b637e408', '1', '2021-04-27 17:15:15', '1', '2021-04-27 17:15:15', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('8084d12d019dbca5a995778d482561ef', 'FUNCTION_TYPE', '功能类型', NULL, 'N', 10, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2022-11-16 01:07:58', '1', '2022-11-16 01:07:58', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('9956ff08f4fce46c636cf3088a79071f', 'DATA_SCOPE_TYPE', '数据权限类型', NULL, 'N', 0, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, NULL, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('a5c94db5490032b8e0f39e64b637e408', 'RESTFUL_MONITOR', '基础-日志监控字典', '', 'Y', 1, '-1', '1', '2021-04-27 17:14:46', '1', '2021-04-27 17:14:46', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('a5c94db5490032brty39e64b637e408', 'BUSINESS_TYPE', '操作日志业务类型', NULL, 'N', 2, 'a5c94db5490032b8e0f39e64b637e408', '1', '2022-05-10 23:30:28', '1', '2022-05-10 23:30:28', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('a5c94db5ghda32brty39e64b637e408', 'OPERATE_STATUS', '操作结果状态', NULL, 'N', 3, 'a5c94db5490032b8e0f39e64b637e408', '1', '2022-05-10 23:30:28', '1', '2022-05-10 23:30:28', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('e3e017c25511bcbae96ceff1c6b4b0e8', 'TOP_BUSINESS_ZDIAN', '业务-基础字典', '', 'Y', 2, '-1', '1', '2021-03-06 00:58:23', '1', '2021-03-06 00:58:23', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_dict_type` VALUES ('ec3e14d6464ca0a0f3072b2106757c8f', 'LOGIN_LIMIT', '登录限制', NULL, 'N', 10, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2022-11-11 19:05:48', '1', '2022-11-11 19:05:48', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_file`;
CREATE TABLE `tb_core_file` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名称',
  `file_size` int(11) DEFAULT NULL COMMENT '文件大小 单位：字节',
  `file_type` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件类型',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件路径',
  `content` longblob COMMENT '文件内容',
  `mark` varchar(156) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件标识',
  `storage_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'SERVER' COMMENT '存储类型 字典FILE_STORAGE_TYPE',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文件表';

-- ----------------------------
-- Table structure for tb_core_function
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_function`;
CREATE TABLE `tb_core_function` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端ID',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `alias` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '别名',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能编码',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '-1' COMMENT '父级ID',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源URL',
  `function_type` int(1) NOT NULL DEFAULT '0' COMMENT '功能类型 0：按钮 1：菜单 2：接口',
  `is_hide` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `target` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '_self' COMMENT '打开方式',
  `sort` int(6) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `moude_summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '模块概述',
  `operate_instruction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '操作说明',
  `function_level` int(2) DEFAULT NULL COMMENT '菜单级别',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='功能菜单表';

-- ----------------------------
-- Records of tb_core_function
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_function` VALUES ('0139ca12f1ccdfdfa77d47ca32be24c1', '75cfc202d34f11ea97e4a34c90effc21', '详情', '详情', 'SYSTEM_PARAMS_DETAIL', 'a4a87d1ad34711ea97e4a34c90effc21', '/core/param/queryById', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('04e71d539cc6d1b562f1fa7785c208f1', '75cfc202d34f11ea97e4a34c90effc21', '角色树形', '角色树形', 'USER_ROLE_TREE', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/role/tree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('051f2e0b81bb4b80706a61e5b78ba507', '75cfc202d34f11ea97e4a34c90effc21', '网关管理', '网关', 'GATEWAY', '-1', '/gateway', 1, 0, 'iconfont iconicon_subordinate', '_blank', 7, '', '', '', NULL, '1', '2021-03-04 00:34:30', '1', '2023-04-11 15:29:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('05e970e55191a5c54b3dc6d064bd0770', '75cfc202d34f11ea97e4a34c90effc21', '详情', '详情', 'SYSTEM_DATASCOPE_DETAIL', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/dataScope/queryById', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('064e59da09abc7a621826805967a1644', '75cfc202d34f11ea97e4a34c90effc21', '删除', '删除', 'SYSTEM_CLIENT_DELETE', 'a49c90a4d34711ea97e4a34c90effc21', '/core/client/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('088334958df3443bedee1c8757512bf6', '75cfc202d34f11ea97e4a34c90effc21', '新增字典类型', '新增字典类型', 'SYSTEM_DICT_TYPE_ADD', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('0d6cc1e9a3c3c2a227ac6181f8b449ff', '75cfc202d34f11ea97e4a34c90effc21', '数据权限', '数据权限', 'SYSTEM_DATASCOPE_LIST', 'a4a32590d34711ea97e4a34c90effc21', '/core/dataScope/listByMenuId', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('0d77c1aa22d261b38d1bb7271058348e', '75cfc202d34f11ea97e4a34c90effc21', '编辑', '编辑', 'SYSTEM_PARAMS_UPDATE', 'a4a87d1ad34711ea97e4a34c90effc21', '/core/param/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('0dd90ff68e3e148c1aa7b071183138b2', '75cfc202d34f11ea97e4a34c90effc21', '删除字典', '删除字典', 'SYSTEM_DICT_DELETE', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/deleteDict', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('102d966d27ff13157f59fbe944338fc0', '75cfc202d34f11ea97e4a34c90effc21', '授权配置', '授权配置', 'SYSTEM_TENANT_SETTING', 'f05fe7b4c8817b808f93b095b070d16b', '/core/tenant/setting', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('104cca5a740ca8c69844e76569a144ff', '75cfc202d34f11ea97e4a34c90effc21', '删除岗位', '删除岗位', 'POST_DELETE', 'd87d67b15ec4f7444470710540472122', '/core/post/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('110172025be388145d6590f85a5b35b3', '75cfc202d34f11ea97e4a34c90effc21', '新增用户', '新增用户', 'SYSTEM_USER_ADD', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1309580f21f0341c2ef8967697732374', '75cfc202d34f11ea97e4a34c90effc21', '编辑岗位', '编辑岗位', 'POST_UPDATE', 'd87d67b15ec4f7444470710540472122', '/core/post/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('14bc75a54268ff9f03427bbcdb8f69f7', '75cfc202d34f11ea97e4a34c90effc21', '树形列表', '树形列表', 'SYSTEM_ORG_TREELIST', 'a49e8d5ad34711ea97e4a34c90effc21', '/core/org/listLazy', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('167586042020d578ca39e9fe5efa611a', '75cfc202d34f11ea97e4a34c90effc21', '客户端下拉', '客户端下拉', 'DATASCOPE_CLIENT_SELECT', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/client/selectList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1704cea2b65eda99fe424f92032fb7af', '75cfc202d34f11ea97e4a34c90effc21', '设置权限', '设置权限', 'SYSTEM_ROLE_UPDATEFUNCTION', 'a4a32590d34711ea97e4a34c90effc21', '/core/role/addFunction', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('19658a547a3e20a49ba934d4e0392e48', '75cfc202d34f11ea97e4a34c90effc21', '保存接口参数', '保存接口参数', 'MONITOR_SAVE_PARAMS', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/save-param', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1a378dc1efc575d6a5038166cd6d59f4', '75cfc202d34f11ea97e4a34c90effc21', '删除', '删除', 'SYSTEM_FUNCTION_DELETE', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1b14c81d5414abc225654140b35340b8', '75cfc202d34f11ea97e4a34c90effc21', '编辑', '编辑', 'SYSTEM_ORG_UPDATE', 'a49e8d5ad34711ea97e4a34c90effc21', '/core/org/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1b4e0ff27dacc493664f633275d82d9e', '75cfc202d34f11ea97e4a34c90effc21', '获取接口参数', '获取接口参数', 'MONITOR_PARAMS', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/param', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1bfd6d4aecaf4a07a5da7464f387d319', '75cfc202d34f11ea97e4a34c90effc21', '顶级菜单选项', '顶级菜单选项', 'DATASCOPE_TOPMENU_SELECT', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/menu/select', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1c7ef4b2ebc27bc5b255e619c58bc79b', '75cfc202d34f11ea97e4a34c90effc21', '修改', '修改', 'SYSTEM_ROLE_UPDATE', 'a4a32590d34711ea97e4a34c90effc21', '/core/role/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1ca4744f906a91d3a15b8f604f12301d', '75cfc202d34f11ea97e4a34c90effc21', '批量删除', '批量删除', 'SYSTEM_FILE_DELETE', 'a4a0a4aad34711ea97dxa34c90effc21', '/core/file/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('1e7d2d641866ad41e0de397114e38e52', '75cfc202d34f11ea97e4a34c90effc21', '操作日志', '操作日志', 'OPERATE_LOG_LIST', '30b4baffc04b6260fgtb79a26f307462', '/log/operate/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2014d9f604369c2fad73c2a758432a87', '75cfc202d34f11ea97e4a34c90effc21', '客户端顶部菜单树', '客户端顶部菜单树', 'ROLE_CLIENT_TOPMENU', 'a4a32590d34711ea97e4a34c90effc21', '/core/menu/listName', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('21082e0fc9e9534139c29e5b6cc66a13', '75cfc202d34f11ea97e4a34c90effc21', '岗位详情', '岗位详情', 'POST_DETAIL', 'd87d67b15ec4f7444470710540472122', '/core/post/get', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('210c1b3795ea98755e303b08b0840b37', '75cfc202d34f11ea97e4a34c90effc21', '文件列表', '文件列表', 'SYSTEM_FILE_LIST', 'a4a0a4aad34711ea97dxa34c90effc21', '/core/file/listPage', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2239cd38a1a2615cb3fb8399e4267422', '75cfc202d34f11ea97e4a34c90effc21', '顶级菜单选项', '顶级菜单选项', 'ROLE_TOPMENU', 'a4a32590d34711ea97e4a34c90effc21', '/core/menu/select', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2285d25ae139eb2d847ae4256e4952cc', '75cfc202d34f11ea97e4a34c90effc21', '文件详情', '文件详情', 'SYSTEM_FILE_DETAIL', 'a4a0a4aad34711ea97dxa34c90effc21', '/core/file/get', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2749d1d15707546fe611e78745d6605e', '75cfc202d34f11ea97e4a34c90effc21', '顶级菜单', '顶级菜单', 'SYSTEM_TOPMENU', 'a499bc6cd34711ea97e4a34c90effc21', '/core/menu', 1, 0, 'iconfont iconicon_subordinate', '_self', 4, NULL, NULL, NULL, NULL, '1', '2022-10-26 20:55:50', '1', '2022-10-26 20:55:50', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2804e33bb44875eda5266f6a5e69d4f1', '75cfc202d34f11ea97e4a34c90effc21', '新增', '新增', 'SYSTEM_ROLE_ADD', 'a4a32590d34711ea97e4a34c90effc21', '/core/role/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('28959acc90138a3737292e4678f9785d', '75cfc202d34f11ea97e4a34c90effc21', '文件下载', '下载', 'DOWNLOAD', '-1', '/core/file/download', 0, 0, '', '_self', 105, '', '', '', 0, '1', '2021-03-03 22:33:41', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('28a8f330d5b53fffb1284f1af20e7e07', '75cfc202d34f11ea97e4a34c90effc21', '组织管理', '组织', 'ORG', '-1', '/org', 1, 0, 'iconfont iconicon_shakehands', '_self', 1, '', '', '', NULL, '1', '2021-03-03 22:33:41', '1', '2023-04-12 16:27:35', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('28da50bebb1ba81ebc000028d8ff3d78', '75cfc202d34f11ea97e4a34c90effc21', '修改', '修改', 'SYSTEM_FUNCTION_UPDATE', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('29a8a209eb57333a8205800839b0b82b', '75cfc202d34f11ea97e4a34c90effc21', '修改字典类型', '修改字典类型', 'SYSTEM_DICT_TYPE_UPDATE', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2a830c7ed8d7d2820c0232558af0ed10', '75cfc202d34f11ea97e4a34c90effc21', '菜单列表', '菜单列表', 'TOPMENU_LIST', '2749d1d15707546fe611e78745d6605e', '/core/menu/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2b71318c5c483c80200bded9298a1bb9', '75cfc202d34f11ea97e4a34c90effc21', '监控结果', '监控结果', 'MONITOR_RESULTS_LIST', '6c3dd5dc10fd974c1674c5b23728c6ec', '/monitor/log/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2bd0e8a69caf613a28104224f2fc14cf', '75cfc202d34f11ea97e4a34c90effc21', '树形部门', '树形部门', 'SYSTEM_USER_ORG', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/org/tree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2cc59e0ef237772faa8879101e6a09d0', '75cfc202d34f11ea97e4a34c90effc21', '监控设置', '监控设置', 'MONITOR_SETTING', 'c339f73d1d78378afe7441471a70f4a9', '/log/monitor/setting', 1, 0, 'iconfont icon-canshu', '_self', 1, '', '主要用来设置接口监控时得参数', '', NULL, '1', '2021-04-25 02:38:03', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2d250d5a426f7c84faae2dfe21142284', '75cfc202d34f11ea97e4a34c90effc21', '权限设置', '权限', 'AUTHORITY', '-1', '/authority', 1, 0, 'iconfont iconicon_safety', '_self', 2, '', '', '', NULL, '1', '2021-03-03 22:33:41', '1', '2023-04-11 15:37:31', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2ef3fa28981e675b47f640ee100362b2', '75cfc202d34f11ea97e4a34c90effc21', '客户端功能树', '客户端功能树', 'CLIENT_MENU_TREE', 'f05fe7b4c8817b808f93b095b070d16b', '/core/function/clientMenuTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('2fea9b3607bd4837e91869289304d9da', '75cfc202d34f11ea97e4a34c90effc21', '链路监控', '链路', 'SKYWALKING', '30b4baffc04b62602bdb79a25f307462', 'http://skywalking.top', 1, 0, 'iconfont icon-iconset0216', '_blank', 0, '', '', '', NULL, '1', '2021-03-04 00:38:42', '1', '2023-04-11 14:36:57', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('301a686128706b8edc5bfdf1574f97d3', '75cfc202d34f11ea97e4a34c90effc21', '详情', '详情', 'SYSTEM_CITY_DETAIL', 'a4aa9780d34711ea97e4a34c90effc21', '/core/city/get', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('30b4baffc04b4850fgtb79a25f307462', '75cfc202d34f11ea97e4a34c90effc21', '错误日志', '错误日志', 'ERROR_LOG', '30b4baffc04b6260fgtb79a25f307462', '/log/error', 1, 0, 'iconfont iconicon_doc', '_self', 5, '', '', '', NULL, '1', '2022-05-10 23:30:56', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('30b4baffc04b62602bdb79a25f307462', '75cfc202d34f11ea97e4a34c90effc21', '系统监控', '监控', 'MONITOR', '-1', '/monitor', 1, 0, 'iconfont icon-yanzhengma', '_blank', 5, '', '', '', NULL, '1', '2021-03-04 00:31:33', '1', '2023-04-11 14:36:56', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('30b4baffc04b6260fgtb79a25f307462', '75cfc202d34f11ea97e4a34c90effc21', '系统日志', '日志', 'LOG', '-1', '/log', 1, 0, 'iconfont icon-caidanguanli', '_self', 5, '', '', '', NULL, '1', '2022-05-10 23:30:55', '1', '2023-04-11 14:36:32', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('30b4baffc04b6260fgtb79a26f307462', '75cfc202d34f11ea97e4a34c90effc21', '操作日志', '操作日志', 'OPERATE_LOG', '30b4baffc04b6260fgtb79a25f307462', '/log/operate', 1, 0, 'iconfont iconicon_compile', '_self', 5, '', '', '', NULL, '1', '2022-05-10 23:30:56', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('3219465dad1388ab564407ca225f16b1', '75cfc202d34f11ea97e4a34c90effc21', '菜单列表', '菜单列表', 'SYSTEM_DATASCOPE_MENU', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/function/listByParent', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('3392f777d70104eeb4e54ca853cc622c', '75cfc202d34f11ea97e4a34c90effc21', '删除', '删除', 'SYSTEM_CITY_DELETE', 'a4aa9780d34711ea97e4a34c90effc21', '/core/city/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('3971ac0afc6351bb6069f79399a58ff8', '75cfc202d34f11ea97e4a34c90effc21', '新增', '新增', 'SYSTEM_FUNCTION_ADD', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('3a056e624c9d0842d2a6bfe156018db9', '75cfc202d34f11ea97e4a34c90effc21', '字典列表', '字典列表', 'SYSTEM_DICT_LIST', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/listByType', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('3c8f3b1eaa17df74ddb71e681f41c1fe', '75cfc202d34f11ea97e4a34c90effc21', '应用监控', '应用', 'SPRING-BOOT-ADMIN', '30b4baffc04b62602bdb79a25f307462', 'http://admin.top', 1, 0, 'iconfont iconicon_safety', '_blank', 0, '', '', '', NULL, '1', '2021-03-04 00:43:27', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('426213f3f8506945ec05f5356487b68a', '75cfc202d34f11ea97e4a34c90effc21', '新增', '新增', 'SYSTEM_PARAMS_ADD', 'a4a87d1ad34711ea97e4a34c90effc21', '/core/param/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('444e0f7df10e2e2501ed42311abc05a9', '75cfc202d34f11ea97e4a34c90effc21', '模板下载', '模板下载', 'SYSTEM_USER_DOWNLOADTEMPLATE', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/downloadTemplate', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('49139aec83b8cb90339ea18331326517', '75cfc202d34f11ea97e4a34c90effc21', '顶级菜单选项', '顶级菜单选项', 'FUNCTION_TOPMENU_SELECT', 'a4a5737cd34711ea97e4a34c90effc21', '/core/menu/select', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('4968faee834a107a882864a1b01928a1', '75cfc202d34f11ea97e4a34c90effc21', '角色用户列表', '角色用户列表', 'SYSTEM_ROLE_USER', 'a4a32590d34711ea97e4a34c90effc21', '/core/user/listByRole', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('4b840fb231878c23c07c4280f41472aa', '75cfc202d34f11ea97e4a34c90effc21', '修改', '修改', 'SYSTEM_DATASCOPE_UPDATE', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/dataScope/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('4c44a399505815a5af7b1ab2cdef72e7', '75cfc202d34f11ea97e4a34c90effc21', '停用字典', '停用字典', 'SYSTEM_DICT_STOP', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/stopDict', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('4cfb90a03a6ac489843fc6125c17aba4', '75cfc202d34f11ea97e4a34c90effc21', '保存', '保存', 'SYSTEM_CLIENT_SAVE', 'a49c90a4d34711ea97e4a34c90effc21', '/core/client/save', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('50da96cb2332551c81e7f2fa080baa08', '75cfc202d34f11ea97e4a34c90effc21', '设置角色', '设置角色', 'SYSTEM_USER_UPDATEROLE', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/addRole', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('52143178927c9b9d44088556172e95fa', '75cfc202d34f11ea97e4a34c90effc21', '删除', '删除', 'SYSTEM_PARAMS_DELETE', 'a4a87d1ad34711ea97e4a34c90effc21', '/core/param/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('5286a6b148e3e9877b967575f9b2d1b1', '75cfc202d34f11ea97e4a34c90effc21', '客户端下拉', '客户端下拉', 'ROLE_CLIENT_SELECT', 'a4a32590d34711ea97e4a34c90effc21', '/core/client/selectList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('52d588f3a3db1004dc314d8cfea85365', '75cfc202d34f11ea97e4a34c90effc21', '踢下线', '踢下线', 'USER_OFFLINE', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/offline', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('5463516a6fc717885fc79becc7e37d21', '75cfc202d34f11ea97e4a34c90effc21', '字典详情', '字典详情', 'SYSTEM_DICT_DETAIL', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/getDict', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('550876d98acd645a6e88bcbcdb3e0f9f', '75cfc202d34f11ea97e4a34c90effc21', '获取接口设置', '获取接口设置', 'MONITOR_SETUP', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/setup', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('55fa29309845b4b2404463f8e82bf305', '75cfc202d34f11ea97e4a34c90effc21', '租户下拉', '租户下拉', 'TENANT_SELECT', '-1', '/core/tenant/selectors', 2, 0, NULL, '_self', 100, NULL, NULL, NULL, NULL, '1', '2022-10-27 19:59:56', '1', '2023-04-23 15:22:22', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('5c6bfd675803e18035e7116595f23345', '75cfc202d34f11ea97e4a34c90effc21', '客户端下拉', '客户端下拉', 'TOPMENU_CLIENT_SELECT', '2749d1d15707546fe611e78745d6605e', '/core/client/selectList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('5e02b179c8009798a58f66ab9df2a019', '75cfc202d34f11ea97e4a34c90effc21', '数据赋权', '数据赋权', 'SYSTEM_DATASCOPE_ROLE', 'a4a32590d34711ea97e4a34c90effc21', '/core/dataScope/roleDataScope', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('64ba63e4f4ced59f3201e5ceb82a29e4', '75cfc202d34f11ea97e4a34c90effc21', '接口树形', '接口树形', 'MONITOR_TREE', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/monitors', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('68c3fc63fec53b74f3433582781e2375', '75cfc202d34f11ea97e4a34c90effc21', '保存一级菜单', '保存一级菜单', 'TOPMENU_FUNCTION_SAVE', '2749d1d15707546fe611e78745d6605e', '/core/menu/saveFunction', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('6b50d84183c45b373e30825a2a41aa2a', '75cfc202d34f11ea97e4a34c90effc21', '删除', '删除', 'SYSTEM_DATASCOPE_DELETE', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/dataScope/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('6c3dd5dc10fd974c1674c5b23728c6ec', '75cfc202d34f11ea97e4a34c90effc21', '监控结果', '监控结果', 'MONITOR_RESULT', 'c339f73d1d78378afe7441471a70f4a9', '/log/monitor/result', 1, 0, 'iconfont icon-debug', '_self', 0, '', '用来查询接口监控得结果，包括第三方服务得监控结果', '', NULL, '1', '2021-04-25 02:35:22', '1', '2022-09-14 16:37:00', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('6dbb90577df794a95f917b9be9ee4e53', '75cfc202d34f11ea97e4a34c90effc21', '新增菜单', '新增菜单', 'TOPMENU_ADD', '2749d1d15707546fe611e78745d6605e', '/core/menu/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('6f7b91967e808549fffc880a298983b4', '75cfc202d34f11ea97e4a34c90effc21', '更新菜单', '更新菜单', 'TOPMENU_UPDATE', '2749d1d15707546fe611e78745d6605e', '/core/menu/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('6f81bbc168d4e5e285534da94889f48c', '75cfc202d34f11ea97e4a34c90effc21', '新增岗位', '新增岗位', 'POST_ADD', 'd87d67b15ec4f7444470710540472122', '/core/post/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('7234a15ac679e8c6f58def847ef64589', '75cfc202d34f11ea97e4a34c90effc21', '租户列表', '租户列表', 'TENANT_LIST', 'f05fe7b4c8817b808f93b095b070d16b', '/core/tenant/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('72b435250bfcdcfcb0c9b3fe8236d8d0', '75cfc202d34f11ea97e4a34c90effc21', '字典子级', '字典子级', 'SYSTEM_DICT_LIST_BY_PARENT', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/listDictChildList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('72e465a2eedb8abe5d92f8eafa73013b', '75cfc202d34f11ea97e4a34c90effc21', '删除用户', '删除用户', 'SYSTEM_USER_DELETE', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('7547057e6e74e3601c966c02ec506e54', '75cfc202d34f11ea97e4a34c90effc21', '导出用户', '导出用户', 'SYSTEM_USER_EXPORTUSER', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/exportUser', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('755965c6ff3e23e560e2f873e63420ea', '75cfc202d34f11ea97e4a34c90effc21', '注册中心', 'NACOS', 'NACOS', '051f2e0b81bb4b80706a61e5b78ba507', 'http://nacos.top', 1, 0, 'iconfont icon-iconset0265', '_blank', 0, '', '', '', NULL, '1', '2021-03-04 00:45:29', '1', '2023-04-11 15:30:12', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('78b1f5a4ea2c69ddc7cac97b7f373dba', '75cfc202d34f11ea97e4a34c90effc21', '角色权限', '角色权限', 'SYSTEM_ROLE_SELECT_URL', 'a4a32590d34711ea97e4a34c90effc21', '/core/function/queryUrlIdByRole', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('78f4264dcd79ba14a06d3ff68eeb8918', '75cfc202d34f11ea97e4a34c90effc21', '角色新增用户', '角色新增用户', 'SYSTEM_ROLE_ADDUSER', 'a4a32590d34711ea97e4a34c90effc21', '/core/user/addRoleUser', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('79cd48eef9a30f8e66cd342132ab1a22', '75cfc202d34f11ea97e4a34c90effc21', '字典类型树', '字典类型树', 'SYSTEM_DICT_TYPELIST', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/dictTypeTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('7a1cb6c01e34a2009478d067dccee622', '75cfc202d34f11ea97e4a34c90effc21', '保存接口设置', '保存接口设置', 'MONITOR_SAVE_SETUP', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/save-setup', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('7c2411b236ad9438bbd306bddec2a321', '75cfc202d34f11ea97e4a34c90effc21', '菜单树形', '菜单树形', 'SYSTEM_FUNCTION_MENU', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/menuTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('7f107796b220f7c28aa55794134beb1f', '75cfc202d34f11ea97e4a34c90effc21', '字典查询', '字典查询', 'DICT_SELECT', '-1', '/core/dict/getDictListByType', 2, 0, NULL, '_self', 100, NULL, NULL, NULL, NULL, '1', '2022-10-27 20:00:42', '1', '2023-04-23 15:22:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('82dc0ab24218918c723929fbf17fc7df', '75cfc202d34f11ea97e4a34c90effc21', '菜单资源', '菜单资源', 'SYSTEM_ROLE_BUT', 'a4a32590d34711ea97e4a34c90effc21', '/core/function/listButByMenu', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('8320d27b295df9bc37fd505535f3291f', '75cfc202d34f11ea97e4a34c90effc21', '客户端下拉', '客户端下拉', 'FUNCTION_CLIENT_SELECT', 'a4a5737cd34711ea97e4a34c90effc21', '/core/client/selectList', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('84c67c24e17068cb20793b319e6912bf', '75cfc202d34f11ea97e4a34c90effc21', '列表', '列表', 'SYSTEM_DATASCOPE_LISTPAGE', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/dataScope/listPage', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('86b572bf45612f4ed4b30121c544b39e', '75cfc202d34f11ea97e4a34c90effc21', '接口限流', '限流', 'SENTINEL', '051f2e0b81bb4b80706a61e5b78ba507', 'http://sentinel.top', 1, 0, 'iconfont iconicon_exchange', '_blank', 0, '', '', '', NULL, '1', '2021-03-04 00:49:42', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('8a76a355bfde58464bc1a0e23e3c85ce', '75cfc202d34f11ea97e4a34c90effc21', '用户列表', '用户列表', 'USER_LIST', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('8c2ddf69633bc8a01f6b11f7a8383105', '75cfc202d34f11ea97e4a34c90effc21', '删除', '删除', 'SYSTEM_ORG_DELETE', 'a49e8d5ad34711ea97e4a34c90effc21', '/core/org/deleteStatus', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('8cfb31c7ac7b9d6cd2fb155d27b0c754', '75cfc202d34f11ea97e4a34c90effc21', '保存字典', '保存字典', 'SYSTEM_DICT_SAVE', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/saveDict', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('8e8146f3fc6f1a859aa006a87f1ab74d', '75cfc202d34f11ea97e4a34c90effc21', '顶部菜单', '顶部菜单', 'TOP_MENU', '-1', '/core/menu/roleMenu', 2, 0, NULL, '_self', 99, NULL, NULL, NULL, NULL, '1', '2022-10-27 19:14:51', '1', '2023-04-23 15:22:27', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('92a3af1767fe29cd89a9937fe784705c', '75cfc202d34f11ea97e4a34c90effc21', '新增', '新增', 'SYSTEM_ORG_ADD', 'a49e8d5ad34711ea97e4a34c90effc21', '/core/org/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('93b43acc92953866e226aa0c8a2f89eb', '75cfc202d34f11ea97e4a34c90effc21', '在线文档', '文档', '/kanyun', '9cf521fe6e03970fe8f503ca9c5052a2', 'https://www.kancloud.cn/guodingzhi/jpower/', 1, 0, 'iconfont iconicon_study', '_blank', 0, '', '', '', NULL, '1', '2021-03-04 00:56:46', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('9562c73e18e848978049af3af1f0c719', '75cfc202d34f11ea97e4a34c90effc21', '岗位下拉', '岗位下拉', 'POST_SELECT', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/post/select', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('9619725f9313cc52db57da1a6e250334', '75cfc202d34f11ea97e4a34c90effc21', 'ELK监控', 'ELK', 'ELK', '30b4baffc04b62602bdb79a25f307462', 'http://elk.top', 1, 0, 'iconfont icon-biaodan', '_blank', 0, '', '', '', NULL, '1', '2021-03-04 00:47:25', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('98f9382cef02ecbd6e8c447b24f4ccc4', '75cfc202d34f11ea97e4a34c90effc21', '列表', '列表', 'PARAM_LIST', 'a4a87d1ad34711ea97e4a34c90effc21', '/core/param/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('9a9168d3b9adec40a0f0dbbca47fcb9e', '75cfc202d34f11ea97e4a34c90effc21', '修改文件', '修改文件', 'SYSTEM_FILE_UPDATE', 'a4a0a4aad34711ea97dxa34c90effc21', '/core/file/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('9c514e527ab7d69aea1ca17b58519aec', '75cfc202d34f11ea97e4a34c90effc21', '列表', '列表', 'CITY_LIST', 'a4aa9780d34711ea97e4a34c90effc21', '/core/city/lazyTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('9cf521fe6e03970fe8f503ca9c5052a2', '75cfc202d34f11ea97e4a34c90effc21', '了解JPower', 'JPower', 'JPOWER', '-1', '/jpower', 1, 0, 'iconfont iconicon_task', '_blank', 10, '', '', '', NULL, '1', '2021-03-04 00:33:04', '1', '2023-04-11 15:38:48', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('9e3b95bedfd69db9345466bbaf67809b', '75cfc202d34f11ea97e4a34c90effc21', '修改租户', '修改租户', 'SYSTEM_TENANT_UPDATE', 'f05fe7b4c8817b808f93b095b070d16b', '/core/tenant/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('9f77f240ade97eb166ec3cd5e965ee1e', '75cfc202d34f11ea97e4a34c90effc21', '新增', '新增', 'SYSTEM_DATASCOPE_ADD', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/dataScope/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a038ee7d9d85284c00986ec0fb53dc89', '75cfc202d34f11ea97e4a34c90effc21', '分组列表', '分组列表', 'MONITOR_TAGS', '6c3dd5dc10fd974c1674c5b23728c6ec', '/monitor/setting/tags', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a3ec6ed70e1c94174b473271693c225e', '75cfc202d34f11ea97e4a34c90effc21', '岗位列表', '岗位列表', 'POST_PAGE', 'd87d67b15ec4f7444470710540472122', '/core/post/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a499bc6cd34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '系统管理', '系统', 'SYSTEM', '-1', '/system', 1, 0, 'iconfont iconicon_setting', '_self', 3, '', '', '', 1, '1', '2021-03-03 22:33:41', '1', '2023-04-12 18:21:30', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a49c90a4d34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '客户端管理', '客户端列表', 'SYSTEM_CLIENT', 'a499bc6cd34711ea97e4a34c90effc21', '/core/client', 1, 0, 'iconfont iconicon_airplay', '_self', 1, NULL, NULL, NULL, 2, '1', '2021-03-03 22:33:41', '1', '2022-09-15 17:58:31', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a49e8d5ad34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '部门管理', '部门列表', 'SYSTEM_ORG', '28a8f330d5b53fffb1284f1af20e7e07', '/core/org', 1, 0, 'iconfont iconicon_group', '_self', 2, NULL, NULL, NULL, 2, '1', '2021-03-03 22:33:41', '1', '2022-09-14 18:08:42', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a49e8d5ad34711ea97e4a34c90effcdz', '75cfc202d34f11ea97e4a34c90effc21', '数据权限', '数据权限', 'SYSTEM_DATASCOPE', '2d250d5a426f7c84faae2dfe21142284', '/core/dataScope', 1, 0, 'iconfont icon-shujuzhanshi2', '_self', 2, '', '', '', 2, '1', '2021-03-03 22:33:41', '1', '2022-09-15 16:40:22', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a4a0a4aad34711cv97dxa34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '文件上传', '上传', 'SYSTEM_FILE_ADD', '-1', '/core/file/upload', 0, 0, '', '_self', 106, NULL, NULL, NULL, 3, '1', '2021-03-03 22:33:41', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a4a0a4aad34711ea97dxa34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '文件管理', '文件列表', 'SYSTEM_FILE', 'a499bc6cd34711ea97e4a34c90effc21', '/core/file', 1, 0, 'iconfont iconicon_doc', '_self', 3, '', '', '', 2, '1', '2021-03-03 22:33:41', '1', '2022-09-15 18:03:32', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a4a0a4aad34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '用户管理', '用户列表', 'SYSTEM_USER', '28a8f330d5b53fffb1284f1af20e7e07', '/core/user', 1, 0, 'iconfont icon-yonghu', '_self', 3, NULL, NULL, NULL, 2, '1', '2021-03-03 22:33:41', '1', '2022-09-14 18:10:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a4a32590d34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '角色管理', '角色列表', 'SYSTEM_ROLE', '2d250d5a426f7c84faae2dfe21142284', '/core/role', 1, 0, 'iconfont iconicon_ding', '_self', 4, NULL, NULL, NULL, 2, '1', '2021-03-03 22:33:41', '1', '2022-09-15 17:21:53', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a4a5737cd34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '功能管理', '功能列表', 'SYSTEM_FUNCTION', 'a499bc6cd34711ea97e4a34c90effc21', '/core/function', 1, 0, 'iconfont icon-caidan', '_self', 5, NULL, NULL, NULL, 2, '1', '2021-03-03 22:33:41', '1', '2023-04-11 16:41:07', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a4a87d1ad34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '参数管理', '参数列表', 'SYSTEM_PARAMS', 'a499bc6cd34711ea97e4a34c90effc21', '/core/param', 1, 0, 'iconfont icon-biaodan', '_self', 6, NULL, NULL, NULL, 2, '1', '2021-03-03 22:33:41', '1', '2022-09-16 16:46:09', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a4aa9780d34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '行政区划', '行政区列表', 'SYSTEM_CITY', 'a499bc6cd34711ea97e4a34c90effc21', '/core/city', 1, 0, 'iconfont iconicon_GPS', '_self', 7, NULL, NULL, NULL, 2, '1', '2021-03-03 22:33:41', '1', '2022-09-16 16:46:03', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a4acf71ed34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '字典管理', '字典列表', 'SYSTEM_DICT', 'a499bc6cd34711ea97e4a34c90effc21', '/core/dict', 1, 0, 'iconfont iconicon_study', '_self', 8, NULL, NULL, NULL, 2, '1', '2021-03-03 22:33:41', '1', '2022-09-16 16:50:34', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a50b70a0d34711ea97e4fg4c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '修改密码', '修改密码', 'UPDATE_PASSWORD', '-1', '/core/user/updatePassword', 0, 0, NULL, '_self', 100, NULL, NULL, NULL, 1, '1', '2021-03-03 22:33:41', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a50ee7f8d34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '菜单列表', '菜单列表', 'FUNCTION_LISTMENUTREE', '-1', '/core/function/listMenuTree', 2, 0, NULL, '_self', 101, NULL, NULL, NULL, 3, '1', '2021-03-03 22:33:41', '1', '2023-04-23 15:22:09', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('a51089b4d34711ea97e4a34c90effc21', '75cfc202d34f11ea97e4a34c90effc21', '菜单下按钮列表', '按钮列表', 'FUNCTION_LISTBUT', '-1', '/core/function/listBut', 2, 0, NULL, '_self', 102, NULL, NULL, NULL, 3, '1', '2021-03-03 22:33:41', '1', '2023-04-23 15:22:36', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('ac304fa4d83dbbc655cbe7d3cf124e31', '75cfc202d34f11ea97e4a34c90effc21', '菜单树形', '菜单树形', 'ROLE_MENU_TREE', 'a4a32590d34711ea97e4a34c90effc21', '/core/function/menuTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('ac8465d912bcea10eb348e38994e973f', '75cfc202d34f11ea97e4a34c90effc21', '获取源码', '源码', 'GITEE', '9cf521fe6e03970fe8f503ca9c5052a2', 'https://gitee.com/gdzWork', 1, 0, 'iconfont icongitee2', '_blank', 0, '', '', '', NULL, '1', '2021-03-04 00:58:14', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('af82fe7d4654c6ff1290b442d2fe8bdf', '75cfc202d34f11ea97e4a34c90effc21', '删除租户', '删除租户', 'SYSTEM_TENANT_DELETE', 'f05fe7b4c8817b808f93b095b070d16b', '/core/tenant/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('afcad6282b8254effe993726126e791c', '75cfc202d34f11ea97e4a34c90effc21', '树形部门', '树形部门', 'SYSTEM_ROLE_ORG', 'a4a32590d34711ea97e4a34c90effc21', '/core/org/tree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('b307757a8ca62654d93d2a946972b3d0', '75cfc202d34f11ea97e4a34c90effc21', '下级部门', '下级部门', 'SYSTEM_ORGCHILDER_LIST', 'a49e8d5ad34711ea97e4a34c90effc21', '/core/org/listLazyByParent', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('b9ae75ec173611eb84474dda60506f3f', '75cfc202d34f11ea97e4a34c90effc21', '租户设置', '租户', 'TENANT', '-1', '/tenant', 1, 0, 'iconfont icon-rizhi1', '_self', 0, '', '', '', 1, '1', '2021-03-03 22:33:41', '1', '2023-04-23 12:46:23', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('b9ty75ec173611eb84454dgh4rhj6f3f', '75cfc202d34f11ea97e4a34c90effc21', '域名查询租户', '域名查询租户', 'SYSTEM_TENANT_DOMAIN', '-1', '/core/tenant/queryByDomain', 2, 0, '', '_self', 104, '', '', '', 1, '1', '2021-03-03 22:33:41', '1', '2023-04-23 15:22:49', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('ba80c61488c1447a916abd686d835ab3', '75cfc202d34f11ea97e4a34c90effc21', '一级菜单', '一级菜单', 'TOPMENU_FUNCTION', '2749d1d15707546fe611e78745d6605e', '/core/menu/listFunction', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('bab173a10b3d6703b8742e889e4001de', '75cfc202d34f11ea97e4a34c90effc21', '树形角色列表', '树形角色列表', 'SYSTEM_ROLE_LIST_TREE', 'a4a32590d34711ea97e4a34c90effc21', '/core/role/listTree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('bafbb61d862d363e2daf71c33d3b3d11', '75cfc202d34f11ea97e4a34c90effc21', '保存', '保存', 'SYSTEM_CITY_SAVE', 'a4aa9780d34711ea97e4a34c90effc21', '/core/city/save', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('bb6c3a49357895d70443b4e17790351b', '75cfc202d34f11ea97e4a34c90effc21', '新增租户', '新增租户', 'SYSTEM_TENANT_ADD', 'f05fe7b4c8817b808f93b095b070d16b', '/core/tenant/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('c1a9c36b526dbceed84716419d14a3db', '75cfc202d34f11ea97e4a34c90effc21', '导入用户', '导入用户', 'SYSTEM_USER_IMPORTUSER', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/importUser', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('c339f73d1d78378afe7441471a70f4a9', '75cfc202d34f11ea97e4a34c90effc21', '接口监控', '接口监控', 'MONITOR_RESTFUL', '30b4baffc04b62602bdb79a25f307462', '/monitor/restful', 1, 0, 'iconfont iconicon_task', '_self', 0, '', '', '', NULL, '1', '2021-04-25 02:32:27', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('c404579d0223d562ead479c8d3578bf7', '75cfc202d34f11ea97e4a34c90effc21', '用户详情', '用户详情', 'USER_DETAIL', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/getById', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('c9f0f7eb5093a53bda4f4c9be371181a', '75cfc202d34f11ea97e4a34c90effc21', '修改用户', '修改用户', 'SYSTEM_USER_UPDATE', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/update', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('cbfae48b6971119dd9f06c6ad72fe6ec', '75cfc202d34f11ea97e4a34c90effc21', '新增', '新增', 'ORG_CHILD_ADD', 'a49e8d5ad34711ea97e4a34c90effc21', '/core/org/add', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('cdce4de7f897cb1cb60ba58c2ea398ce', '75cfc202d34f11ea97e4a34c90effc21', '菜单开关', '同步', 'SYSTEM_FUNCTION_HIDE', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/hide', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('cee1237740f54d5fc16f852e217a2c7e', '75cfc202d34f11ea97e4a34c90effc21', '接口文档', '接口', 'DOC', '9cf521fe6e03970fe8f503ca9c5052a2', 'http://doc.top', 1, 0, 'iconfont iconicon_compile', '_blank', 0, '', '', '', NULL, '1', '2021-03-04 00:54:13', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('cf596ad75c94cd69559ca0df160c25c6', '75cfc202d34f11ea97e4a34c90effc21', '功能点同步', '同步', 'SYSTEM_FUNCTION_GENERATE', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/generate', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('d219059c56effb0012f0f634afb5dc34', '75cfc202d34f11ea97e4a34c90effc21', '导出监控结果', '导出监控结果', 'MONITOR_RESULTS_EXPORT', '6c3dd5dc10fd974c1674c5b23728c6ec', '/monitor/log/export', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('d48b995bfaa1b102e84b9a7a415f0c30', '75cfc202d34f11ea97e4a34c90effc21', '重置密码', '重置密码', 'SYSTEM_USER_RESETPASSWORD', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/resetPassword', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('d5f47b2455de98b4a4b740d4262c4e74', '75cfc202d34f11ea97e4a34c90effc21', '修改登录人信息', '修改登录人信息', 'UPDATE_LOGIN', '-1', '/core/user/updateLogin', 0, 0, '', '_self', 103, '', '', '', NULL, '1', '2021-03-03 22:33:41', '1', '2022-08-10 22:23:17', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('d87d67b15ec4f7444470710540472122', '75cfc202d34f11ea97e4a34c90effc21', '岗位管理', '岗位', 'POST', '28a8f330d5b53fffb1284f1af20e7e07', '/core/post', 1, 0, 'iconfont iconicon_group', '_self', 3, NULL, NULL, NULL, NULL, '1', '2022-09-17 15:14:36', '1', '2022-09-17 15:14:36', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('d8c9c9fd98571139a1da9c783f0d0c6b', '75cfc202d34f11ea97e4a34c90effc21', '数据权限ID', '数据权限ID', 'SYSTEM_DATASCOPE_LISTID', 'a4a32590d34711ea97e4a34c90effc21', '/core/dataScope/listIdByRoleId', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('d90f82ddcb376084ace8b0ed0e71e643', '75cfc202d34f11ea97e4a34c90effc21', '删除', '删除', 'SYSTEM_ROLE_DELETE', 'a4a32590d34711ea97e4a34c90effc21', '/core/role/deleteStatus', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('db7b82686ca567f24f377c515105b8fb', '75cfc202d34f11ea97e4a34c90effc21', '树形部门', '树形部门', 'SYSTEM_ORG_TREE', 'a49e8d5ad34711ea97e4a34c90effc21', '/core/org/tree', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('dc6ee3b7b7462c5d3f2eb4a0589a6a7e', '75cfc202d34f11ea97e4a34c90effc21', '顶部菜单ID', '顶部菜单ID', 'ROLE_TOPMENU_ID', 'a4a32590d34711ea97e4a34c90effc21', '/core/role/topMenuId', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('dd4b2637971d23f8f1555dbbda22bdf7', '75cfc202d34f11ea97e4a34c90effc21', '服务列表', '服务列表', 'MONITOR_SERVERS', '6c3dd5dc10fd974c1674c5b23728c6ec', '/monitor/setting/servers', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('df9acb2ccbfe21671e81fc1d8fdf01a1', '75cfc202d34f11ea97e4a34c90effc21', '菜单列表', '菜单列表', 'CHILD_FUNCTION', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/listByParent', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('e4854964947a25a2b15113625b23f678', '75cfc202d34f11ea97e4a34c90effc21', '关联一级菜单ID', '关联一级菜单ID', 'TOPMENU_FUNCTION_ID', '2749d1d15707546fe611e78745d6605e', '/core/menu/listFunctionId', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('e4fb2aabd10c6a571a13164c1b7e2390', '75cfc202d34f11ea97e4a34c90effc21', '删除字典类型', '删除字典类型', 'SYSTEM_DICT_TYPE_DELETE', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/deleteDictType', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('e541f0720e0decd674d8bc135f67452f', '75cfc202d34f11ea97e4a34c90effc21', '错误日志', '错误日志', 'ERROR_LOG_LIST', '30b4baffc04b4850fgtb79a25f307462', '/log/error/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('e69cdfe6a5fcd86cc8d8bf3b2d7360e3', '75cfc202d34f11ea97e4a34c90effc21', '保存', '保存', 'SYSTEM_CLIENT_ADD', 'a49c90a4d34711ea97e4a34c90effc21', '/core/client/save', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('e8831b3da10be98a0a721523696df23a', '75cfc202d34f11ea97e4a34c90effc21', '删除菜单', '删除菜单', 'TOPMENU_DELETE', '2749d1d15707546fe611e78745d6605e', '/core/menu/delete', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('ee14d49e9febba0223237b134681f294', '75cfc202d34f11ea97e4a34c90effc21', '用户在线信息', '用户在线信息', 'USER_ONLINE', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/user/online', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('f05fe7b4c8817b808f93b095b070d16b', '75cfc202d34f11ea97e4a34c90effc21', '租户管理', '租户管理', 'SYSTEM_TENANT', 'b9ae75ec173611eb84474dda60506f3f', '/core/tenant', 1, 0, 'iconfont iconicon_boss', '_self', 0, '', '', '', 1, '1', '2021-03-03 22:33:41', '1', '2022-09-14 17:54:44', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('f4616c4d9abd8cd57a4eebf4218efb62', '75cfc202d34f11ea97e4a34c90effc21', '列表', '列表', 'CLIENT_LIST', 'a49c90a4d34711ea97e4a34c90effc21', '/core/client/list', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('f5525e01369371cb171da3d70723cbc9', '75cfc202d34f11ea97e4a34c90effc21', '删除接口设置', '删除接口设置', 'MONITOR_DELETE_SETUP', '2cc59e0ef237772faa8879101e6a09d0', '/monitor/setting/delete-setup', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('f9a6eaeea225ca40666c3c3ef6bb087d', '75cfc202d34f11ea97e4a34c90effc21', '字典类型详情', '字典类型详情', 'SYSTEM_DICT_TYPE_DETAIL', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/getDictType', 2, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function` VALUES ('fb74ed3e83c27c06b6c82e1516de00d9', '75cfc202d34f11ea97e4a34c90effc21', '角色去除用户', '角色去除用户', 'SYSTEM_ROLE_DELUSER', 'a4a32590d34711ea97e4a34c90effc21', '/core/user/deleteRoleUser', 0, 0, NULL, '_self', NULL, NULL, NULL, NULL, NULL, '1', '2023-04-23 15:36:55', '1', '2023-04-23 15:36:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_function_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_function_menu`;
CREATE TABLE `tb_core_function_menu` (
  `id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `function_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能ID',
  `menu_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单ID',
  `create_user` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='功能菜单顶级菜单关联表';

-- ----------------------------
-- Records of tb_core_function_menu
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_function_menu` VALUES ('0876c884638a0e6e2728896852e1b39c', '051f2e0b81bb4b80706a61e5b78ba507', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('09d7cbcf1b1faae73253d9d713032699', '5550eb9947769d198fdf828a0c67d0f9', '42bf4c523265405c0f593300ef45c49e', '1', '2023-04-20 14:59:11', '1', '2023-04-20 14:59:11', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('2b009a1ce551e302d68b31f18d1c870c', '48f63d67ff7b0d698d7e52436bf8f4a8', 'b89ed9a54205cc58a3bc4b3ff74d5423', '1', '2023-04-20 14:59:00', '1', '2023-04-20 14:59:00', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('3ed2ae3ed7511309006aae781608afa5', '28a8f330d5b53fffb1284f1af20e7e07', '7585f0e1863b262d61b0cff37dcf28a3', '1', '2022-10-27 00:47:37', '1', '2022-10-27 00:47:37', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('485d610e8453398a5fbab1971d6eaacd', '52fa4c59e6e43733955c3e5dd8987e4c', 'b89ed9a54205cc58a3bc4b3ff74d5423', '1', '2023-04-20 14:59:00', '1', '2023-04-20 14:59:00', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('4d4e9d9b7d35a63ba14f2b4844710364', '2d250d5a426f7c84faae2dfe21142284', '7585f0e1863b262d61b0cff37dcf28a3', '1', '2022-10-27 00:47:37', '1', '2022-10-27 00:47:37', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('5cf53193d0efdd0a004d3feb8b2f8912', '051f2e0b81bb4b80706a61e5b78ba507', '9ac0634d6ddc381f56822121ed0baf96', '1', '2022-10-27 19:08:34', '1', '2022-10-27 19:08:34', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('a7dc8a0f25f7bf1d447d7e729fae6967', '30b4baffc04b6260fgtb79a25f307462', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('b7685bc41006b778e621fb5b42fb91a3', '2d250d5a426f7c84faae2dfe21142284', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('b7a4450bd28341bb978545cba965f37f', '9cf521fe6e03970fe8f503ca9c5052a2', '9ac0634d6ddc381f56822121ed0baf96', '1', '2022-10-27 19:08:34', '1', '2022-10-27 19:08:34', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('bb60f0b11671bcf8c6cdd2d0539a9f59', '30b4baffc04b6260fgtb79a25f307462', 'e3491279813fd194220ae0a9e703ea73', '1', '2022-10-27 19:11:49', '1', '2022-10-27 19:11:49', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('c64a9618a09b70503dbd2650645eae47', 'a499bc6cd34711ea97e4a34c90effc21', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('c79c16fdde1a7378af1a65af850bbd8b', '9cf521fe6e03970fe8f503ca9c5052a2', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('dc859b43fa5282e2295b984cdd20826b', '55b1ac20ca0d42c29715cf937815a28a', 'b899f411521066f52fb59ac10aacce5c', '1', '2023-04-20 14:58:55', '1', '2023-04-20 14:58:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('de419fcfd3de52dfc1d2219a335dd2b4', '30b4baffc04b62602bdb79a25f307462', 'e3491279813fd194220ae0a9e703ea73', '1', '2022-10-27 19:11:49', '1', '2022-10-27 19:11:49', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('e0b185f70505b832feb77e2b2f1420ff', 'a499bc6cd34711ea97e4a34c90effc21', '77e6ad97d4804f5be0e9da463459f220', '1', '2022-10-27 19:04:51', '1', '2022-10-27 19:04:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('e35a4a530b3fa7948b00bf07db9f19e2', '48f63d67ff7b0d698d7e52436bf8f4a8', 'b899f411521066f52fb59ac10aacce5c', '1', '2023-04-20 14:58:55', '1', '2023-04-20 14:58:55', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('e7bbff78543bb9897719626ee7be3072', '30b4baffc04b62602bdb79a25f307462', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('f4d7f9c3357de803c5fed1611c9b39bf', 'b9ae75ec173611eb84474dda60506f3f', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_function_menu` VALUES ('f7c5cd445b88b9797dcdf42aa77e9846', '28a8f330d5b53fffb1284f1af20e7e07', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_org
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_org`;
CREATE TABLE `tb_core_org` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织机构编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织机构名称',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '-1' COMMENT '父级ID',
  `ancestor_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '祖级ID',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `sort` int(6) DEFAULT '0' COMMENT '排序',
  `head_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人姓名',
  `head_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人电话',
  `head_email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人邮箱',
  `contact_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人电话',
  `contact_email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
  `is_virtual` tinyint(1) DEFAULT '0' COMMENT '是否虚拟机构 0:否 1：是',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Records of tb_core_org
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_org` VALUES ('19e8ebdbd2cce9eda63aefc36628cb50', 'TEST', '人事部门', '6836de3b179d11eb8189fa163e5c4fd4', '6836de3b179d11eb8189fa163e5c4fd4,-1', NULL, 0, '', '', '', '老大手下', '11011011001', '', '', 1, '', '1', '2021-03-03 22:33:52', '1', '2021-03-03 22:33:52', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_org` VALUES ('6836de3b179d11eb8189fa163e5c4fd4', '000000', '牛逼科技', '-1', '-1', NULL, 0, NULL, NULL, NULL, '公司老大', '11011071100', NULL, NULL, 1, NULL, '1', '2021-03-03 22:33:52', '1', '2021-03-03 22:33:52', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_org` VALUES ('83bacc1543bb2dc56a38fee32bb4a1fd', 'programmer', '程序员部门', '6836de3b179d11eb8189fa163e5c4fd4', '6836de3b179d11eb8189fa163e5c4fd4,-1', NULL, 0, '', '', '', '秃顶老大', '12012011200', '', '', 1, '', '1', '2021-03-03 22:33:52', '1', '2021-03-05 16:57:38', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_param`;
CREATE TABLE `tb_core_param` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数code',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数名称',
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数值',
  `note` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统参数表';

-- ----------------------------
-- Records of tb_core_param
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_param` VALUES ('39d36d80127f11ea80eb7a10df2f4ffc', 'JPOWER_USER_DEFAULT_PASSWORD', '用户默认登录密码', '123456', '系统用户默认登录密码', '1', '2021-03-03 22:33:47', '1', '2021-03-03 22:33:47', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_param` VALUES ('39d36d80997f11ea20eb7a10df2f4ffc', 'JPOWER_IS_REGISTER', '是否开启注册', 'false', '是否开启注册', 'root', '2022-06-08 16:05:14', 'root', '2022-06-08 16:05:14', 1, 0, NULL);
INSERT INTO `tb_core_param` VALUES ('39d36d80997f11ea80eb7a10df2f4ffc', 'JPOWER_IS_ACTIVATION', '新增用户默认是否激活', '1', '注册用户默认是否激活：1代表是，0代表否', '1', '2021-03-03 22:33:47', '1', '2021-03-05 11:38:27', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_param` VALUES ('39d36d80997f11ra80eb7a10df2f4ffc', 'REGISTER_ROLE', '注册用户角色ID', '', '注册用户角色ID', 'root', '2022-06-08 16:10:18', 'root', '2022-06-08 16:10:21', 1, 0, NULL);
INSERT INTO `tb_core_param` VALUES ('ff4ad7f3c6f04dae3abad634a60ef25b', 'user.login', '登录模式', 'CLIENT_SQUEEZE', '参数值说明：\r\nNONE：不限制\r\nONE：所有客户端只可登录一个次\r\nCLIENT：一个客户端下只可登录一次\r\nONE_SQUEEZE：所有客户端下当产生新的登录以后，后登录用户挤掉之前的登录者\r\nCLIENT_SQUEEZE：一个客户端下当产生新的登录以后，后登录用户挤掉之前的登录者', '1', '2022-11-10 22:44:44', '1', '2023-04-11 15:53:31', 1, 1, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_post`;
CREATE TABLE `tb_core_post` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `type` int(2) DEFAULT NULL COMMENT '岗位类型 字典：POST_TYPE',
  `sort` int(6) DEFAULT NULL COMMENT '排序',
  `describe` varchar(556) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位描述',
  `condition` varchar(556) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上岗条件',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '是否启用 字典：YN01',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '000000' COMMENT '租户编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='岗位表';

-- ----------------------------
-- Records of tb_core_post
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_post` VALUES ('b4595c92fd98536aa54059644f2998dc', 'JAVA', 'JAVA', 3, 1, '会写JAVA得程序员', '会JAVA', '1', '2022-09-17 16:01:52', '1', '2022-09-17 16:01:52', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4', '000000');
INSERT INTO `tb_core_post` VALUES ('d91419e47838875e1dc89e5eacbc6300', 'CS', '测试', 3, 3, '测试程序是否正常', '会测试', '1', '2022-09-17 16:02:39', '1', '2022-09-17 16:17:24', 0, 0, '6836de3b179d11eb8189fa163e5c4fd4', '000000');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role`;
CREATE TABLE `tb_core_role` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
  `alias` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色别名',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上级ID',
  `icon_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标地址',
  `is_sys_role` int(1) DEFAULT '1' COMMENT '是否系统角色 0:否 1:是',
  `ancestor_id` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '祖级ID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '000000' COMMENT '租户编码',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of tb_core_role
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_role` VALUES ('1', 'root', '超级管理员', '-1', NULL, 1, '-1', '这是系统内置角色，不要删除，会影响功能', 1, '1', '2021-03-03 22:34:00', '1', '2022-06-01 01:56:22', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role` VALUES ('2', 'anonymous', '匿名用户', '-1', NULL, 1, '-1', '这是系统内置角色，不要删除，会影响功能', 1, '1', '2021-03-03 22:34:00', '1', '2022-06-01 01:56:22', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role` VALUES ('5fb588ac581ed9dc8587baa30e2235ee', 'ADMIN', '管理员', '1', NULL, 0, '-1,1', NULL, 0, '1', '2022-09-16 16:53:57', '1', '2022-10-27 19:54:30', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_role_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_data`;
CREATE TABLE `tb_core_role_data` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `data_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据权限ID',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`data_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色数据权限关联表';

-- ----------------------------
-- Table structure for tb_core_role_function
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_function`;
CREATE TABLE `tb_core_role_function` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `function_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单ID',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`function_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
-- Records of tb_core_role_function
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_role_function` VALUES ('033131b7ab3f4f07b4c7781ca52b0220', '5fb588ac581ed9dc8587baa30e2235ee', '72b435250bfcdcfcb0c9b3fe8236d8d0', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('043177e5762648a5ab3ef257db4b3ed2', '5fb588ac581ed9dc8587baa30e2235ee', '3a056e624c9d0842d2a6bfe156018db9', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('050c47f3cfba48f0894aa75b84e8020e', '5fb588ac581ed9dc8587baa30e2235ee', '5c6bfd675803e18035e7116595f23345', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('0c1c54a8b936462f86e34651507b2b12', '5fb588ac581ed9dc8587baa30e2235ee', 'cbfae48b6971119dd9f06c6ad72fe6ec', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('0e4fc3e6ef6941dbb77f141d4df83baa', '5fb588ac581ed9dc8587baa30e2235ee', 'f9a6eaeea225ca40666c3c3ef6bb087d', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('0f5afc639d4f48c297d0c28d8693c579', '5fb588ac581ed9dc8587baa30e2235ee', '0d77c1aa22d261b38d1bb7271058348e', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('116f149a30c34cf98ec92616deaa47fd', '5fb588ac581ed9dc8587baa30e2235ee', 'afcad6282b8254effe993726126e791c', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('1176cf89cd2d4e9f804a298777b6cdad', '5fb588ac581ed9dc8587baa30e2235ee', '2749d1d15707546fe611e78745d6605e', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('130cb37118e24238824f1d98ef849d76', '5fb588ac581ed9dc8587baa30e2235ee', '1a378dc1efc575d6a5038166cd6d59f4', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('14f61d9241f9409e96321c522626568e', '5fb588ac581ed9dc8587baa30e2235ee', 'e8831b3da10be98a0a721523696df23a', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('171c944173aa4c0b8ab45989de7f5853', '5fb588ac581ed9dc8587baa30e2235ee', '8c2ddf69633bc8a01f6b11f7a8383105', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('17aa1cd98c1c473398d34cba40152bbb', '5fb588ac581ed9dc8587baa30e2235ee', 'cdce4de7f897cb1cb60ba58c2ea398ce', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('17c2207c2b024250925cc2d17939251b', '5fb588ac581ed9dc8587baa30e2235ee', '21082e0fc9e9534139c29e5b6cc66a13', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('18403b08ea1f4d299e5de7f339c92751', '5fb588ac581ed9dc8587baa30e2235ee', '2239cd38a1a2615cb3fb8399e4267422', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('1a1ef96836b848c88979444e4b2f58a1', '5fb588ac581ed9dc8587baa30e2235ee', '5463516a6fc717885fc79becc7e37d21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('1b30cc9853bd424aa64f3c9be01df084', '5fb588ac581ed9dc8587baa30e2235ee', '4968faee834a107a882864a1b01928a1', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('1c84c3ee7ae34f7bb1a0df33ff047a43', '5fb588ac581ed9dc8587baa30e2235ee', 'a4acf71ed34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('2011062bec1347f1938fa2073a3b1368', '5fb588ac581ed9dc8587baa30e2235ee', '9c514e527ab7d69aea1ca17b58519aec', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('208d2713f9594dc78472cef844415f7f', '5fb588ac581ed9dc8587baa30e2235ee', 'a4a0a4aad34711ea97dxa34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('215c860747214dbfb5aca22445629ec8', '5fb588ac581ed9dc8587baa30e2235ee', '30b4baffc04b4850fgtb79a25f307462', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('246d3707b79e4bb3afcae1e89bb4adae', '5fb588ac581ed9dc8587baa30e2235ee', '9f77f240ade97eb166ec3cd5e965ee1e', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('30730920f4034bdea64bd267b3f332f3', '5fb588ac581ed9dc8587baa30e2235ee', '52143178927c9b9d44088556172e95fa', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('318b9780e25e4b3b83987dc0705212c5', '5fb588ac581ed9dc8587baa30e2235ee', 'a4a0a4aad34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('31b21a5148424ad0a8039c8dfd1a8877', '5fb588ac581ed9dc8587baa30e2235ee', '6f81bbc168d4e5e285534da94889f48c', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('32bec2934d3b4682b57c0896cc2f0cac', '5fb588ac581ed9dc8587baa30e2235ee', '167586042020d578ca39e9fe5efa611a', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('34803313361a498fa76a2301689fd4bf', '5fb588ac581ed9dc8587baa30e2235ee', 'a50ee7f8d34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('35606d18991348aa98a99857da6d0e15', '5fb588ac581ed9dc8587baa30e2235ee', 'bafbb61d862d363e2daf71c33d3b3d11', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('3823cfd22b01413097d19cb91dbb4316', '5fb588ac581ed9dc8587baa30e2235ee', '0d6cc1e9a3c3c2a227ac6181f8b449ff', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('3bb575ed03a1453ba7e229594787be80', '5fb588ac581ed9dc8587baa30e2235ee', '4cfb90a03a6ac489843fc6125c17aba4', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('3bc24e29e01c4a6d84323fa93df4c2db', '5fb588ac581ed9dc8587baa30e2235ee', 'f4616c4d9abd8cd57a4eebf4218efb62', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('3bd5db5854eb4aa28f7b8234bd519bd3', '5fb588ac581ed9dc8587baa30e2235ee', '04e71d539cc6d1b562f1fa7785c208f1', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('3c9d0374f6f14bc48da6ffd352c22dda', '5fb588ac581ed9dc8587baa30e2235ee', 'c1a9c36b526dbceed84716419d14a3db', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('3d8eab0ea1a14e0e9e2128863ec8e4f2', '5fb588ac581ed9dc8587baa30e2235ee', 'd87d67b15ec4f7444470710540472122', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('3fb75e3510d94028a4ca2e84f1cfd186', '5fb588ac581ed9dc8587baa30e2235ee', '78b1f5a4ea2c69ddc7cac97b7f373dba', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('406535034a8b4f249c4ecc4256163498', '5fb588ac581ed9dc8587baa30e2235ee', 'a4a5737cd34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('40c15440266e45378e9569dec6ae8fe7', '5fb588ac581ed9dc8587baa30e2235ee', '52d588f3a3db1004dc314d8cfea85365', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('4128fdf2c30541ddb1f01810efefb8fb', '5fb588ac581ed9dc8587baa30e2235ee', '28a8f330d5b53fffb1284f1af20e7e07', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('44c3994f301549c48c37e34a74afe66c', '5fb588ac581ed9dc8587baa30e2235ee', 'db7b82686ca567f24f377c515105b8fb', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('453679c1fd7f4f77a0c6ee5bf021eace', '5fb588ac581ed9dc8587baa30e2235ee', '6dbb90577df794a95f917b9be9ee4e53', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('4a579a6f3ad04ee09990036716ee95d0', '5fb588ac581ed9dc8587baa30e2235ee', '29a8a209eb57333a8205800839b0b82b', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('4a962e0201f74cb3897b0431d7d41705', '5fb588ac581ed9dc8587baa30e2235ee', '6f7b91967e808549fffc880a298983b4', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('4b05de56a7bc41a290faac5faef02cdb', '5fb588ac581ed9dc8587baa30e2235ee', '110172025be388145d6590f85a5b35b3', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('4c07767c41a04cb98260f5f492efc11d', '5fb588ac581ed9dc8587baa30e2235ee', '2a830c7ed8d7d2820c0232558af0ed10', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('4db45a4b05b44437907c4092382f18c1', '5fb588ac581ed9dc8587baa30e2235ee', '7f107796b220f7c28aa55794134beb1f', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('4e8b9536e2214254949d5477b6264393', '5fb588ac581ed9dc8587baa30e2235ee', 'd8c9c9fd98571139a1da9c783f0d0c6b', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('4e92fd695a5f4939bd8f0db32ac8bb6f', '5fb588ac581ed9dc8587baa30e2235ee', 'a49e8d5ad34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('50285ce904fe4b0db6bb59bbf8b61d2d', '5fb588ac581ed9dc8587baa30e2235ee', '8cfb31c7ac7b9d6cd2fb155d27b0c754', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('53b1cb7e8043473698887cc807c3992d', '5fb588ac581ed9dc8587baa30e2235ee', '72e465a2eedb8abe5d92f8eafa73013b', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('53be59d3739e43948582c40ec7809aa1', '5fb588ac581ed9dc8587baa30e2235ee', '28da50bebb1ba81ebc000028d8ff3d78', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('582631e879b140f59be0a4cc76a3ac25', '5fb588ac581ed9dc8587baa30e2235ee', 'ba80c61488c1447a916abd686d835ab3', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('5a8ca49a1ecd4388b9c05916043137a9', '5fb588ac581ed9dc8587baa30e2235ee', '0dd90ff68e3e148c1aa7b071183138b2', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('5d20a4e2703b462194b90447ae6ecb84', '5fb588ac581ed9dc8587baa30e2235ee', '1309580f21f0341c2ef8967697732374', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('5e5ad5df6a9e49838330e47989e106b6', '5fb588ac581ed9dc8587baa30e2235ee', '210c1b3795ea98755e303b08b0840b37', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('5f31876e8144429394d036376a2b491f', '5fb588ac581ed9dc8587baa30e2235ee', '5e02b179c8009798a58f66ab9df2a019', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('5f97eef172204f42882efdaf13934aad', '5fb588ac581ed9dc8587baa30e2235ee', '2285d25ae139eb2d847ae4256e4952cc', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('64745cfee8c64abea83309951dda2c5f', '5fb588ac581ed9dc8587baa30e2235ee', '92a3af1767fe29cd89a9937fe784705c', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('65443c4b419443ecbf34047cfcb36438', '5fb588ac581ed9dc8587baa30e2235ee', '3219465dad1388ab564407ca225f16b1', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('6606777361ae42baaff33aa81919f73e', '5fb588ac581ed9dc8587baa30e2235ee', '1bfd6d4aecaf4a07a5da7464f387d319', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('685b9885fa1046c3a9b429e0f2ca8df9', '5fb588ac581ed9dc8587baa30e2235ee', 'ac304fa4d83dbbc655cbe7d3cf124e31', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('68dbdd2db4e94de7ad69cc37fddd8762', '5fb588ac581ed9dc8587baa30e2235ee', 'c9f0f7eb5093a53bda4f4c9be371181a', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('695ca002e2f04295ab24f264447ef24f', '5fb588ac581ed9dc8587baa30e2235ee', '1704cea2b65eda99fe424f92032fb7af', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('69e66d1ab0e24a68a0ef44eca92b427e', '5fb588ac581ed9dc8587baa30e2235ee', 'fb74ed3e83c27c06b6c82e1516de00d9', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('6d977dc8c6734a2eb5a374a29fc764fa', '5fb588ac581ed9dc8587baa30e2235ee', '30b4baffc04b6260fgtb79a25f307462', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('6dc95161d59e4a419c991b7a78b66a57', '5fb588ac581ed9dc8587baa30e2235ee', '426213f3f8506945ec05f5356487b68a', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('6dd5b4cbe68f43169f2c61e612b4533f', '5fb588ac581ed9dc8587baa30e2235ee', 'a3ec6ed70e1c94174b473271693c225e', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('6f095a5ec2074852b0b31330351125ca', '5fb588ac581ed9dc8587baa30e2235ee', 'a49e8d5ad34711ea97e4a34c90effcdz', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('6fb96c07a3c74b028f9c184c687ce82f', '5fb588ac581ed9dc8587baa30e2235ee', '2804e33bb44875eda5266f6a5e69d4f1', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('6fcc063740ed44e395e3afafca4ec8cd', '5fb588ac581ed9dc8587baa30e2235ee', '9562c73e18e848978049af3af1f0c719', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('70a24f0ea74445ef9bae9582d599f671', '5fb588ac581ed9dc8587baa30e2235ee', '301a686128706b8edc5bfdf1574f97d3', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('72a7f309482c49a58a4417da2e16b20d', '5fb588ac581ed9dc8587baa30e2235ee', '444e0f7df10e2e2501ed42311abc05a9', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('75f043af010744c2a8d57906ce129fa0', '5fb588ac581ed9dc8587baa30e2235ee', '14bc75a54268ff9f03427bbcdb8f69f7', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('769cf79cfc284f2e88c28e908e905c3e', '5fb588ac581ed9dc8587baa30e2235ee', '2d250d5a426f7c84faae2dfe21142284', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('7999b04594b54159a67e1ffcf8e6f528', '5fb588ac581ed9dc8587baa30e2235ee', 'e69cdfe6a5fcd86cc8d8bf3b2d7360e3', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('79b605863ea74f38a4b17e0e3cc0e0e2', '5fb588ac581ed9dc8587baa30e2235ee', '55fa29309845b4b2404463f8e82bf305', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('7a15f201d06e44bfa978277f6031149b', '5fb588ac581ed9dc8587baa30e2235ee', '1ca4744f906a91d3a15b8f604f12301d', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('7a1ea3a85814465887433c250f4076f5', '5fb588ac581ed9dc8587baa30e2235ee', '5286a6b148e3e9877b967575f9b2d1b1', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('7a98d19cd042490e8b7c3e97bca40d88', '5fb588ac581ed9dc8587baa30e2235ee', 'e541f0720e0decd674d8bc135f67452f', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('7dc3677cd6094c07ae6cb6be93980c18', '5fb588ac581ed9dc8587baa30e2235ee', 'c404579d0223d562ead479c8d3578bf7', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('811c4664b8bd482cad8f07b2bf14bf07', '2', 'b9ty75ec173611eb84454dgh4rhj6f3f', '1', '2023-04-23 15:21:53', '1', '2023-04-23 15:21:53', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('823b451a062743ceb25863cfd1899698', '5fb588ac581ed9dc8587baa30e2235ee', '2014d9f604369c2fad73c2a758432a87', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('8352540351884ec8bcde5b4a16ddb54f', '5fb588ac581ed9dc8587baa30e2235ee', 'bab173a10b3d6703b8742e889e4001de', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('841afd4681de43c29256202795b6f725', '5fb588ac581ed9dc8587baa30e2235ee', '4c44a399505815a5af7b1ab2cdef72e7', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('87260ea6c4184cff957044d50f9a5777', '5fb588ac581ed9dc8587baa30e2235ee', '79cd48eef9a30f8e66cd342132ab1a22', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('8a4f3d1e9b2245449a0a8117cb01656e', '5fb588ac581ed9dc8587baa30e2235ee', '2bd0e8a69caf613a28104224f2fc14cf', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('8c15915b28a643ab8997f16bfe021858', '5fb588ac581ed9dc8587baa30e2235ee', '84c67c24e17068cb20793b319e6912bf', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('8e8179f3f95748808a07caea88a09237', '5fb588ac581ed9dc8587baa30e2235ee', '30b4baffc04b6260fgtb79a26f307462', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('911631ba944a49f8b7a6eb3f7d3e4fc4', '5fb588ac581ed9dc8587baa30e2235ee', '7c2411b236ad9438bbd306bddec2a321', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('93fa4a9b42f34d1cad4128d6795e4a1d', '5fb588ac581ed9dc8587baa30e2235ee', '28959acc90138a3737292e4678f9785d', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('96a7dda3269c44a591bcebe7ab903134', '5fb588ac581ed9dc8587baa30e2235ee', '3971ac0afc6351bb6069f79399a58ff8', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('986584e493694a24b49f06059cb85fd8', '5fb588ac581ed9dc8587baa30e2235ee', '1b14c81d5414abc225654140b35340b8', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('9ba20caba32b4e438585c8bceac2b46d', '5fb588ac581ed9dc8587baa30e2235ee', 'e4854964947a25a2b15113625b23f678', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('9c3ce5d63d1a4ca993729c5f43245eef', '5fb588ac581ed9dc8587baa30e2235ee', '1c7ef4b2ebc27bc5b255e619c58bc79b', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('9f403b8359f84fd9b7eeb84ab9543d2b', '5fb588ac581ed9dc8587baa30e2235ee', 'a49c90a4d34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('a0a08d6bf4554f4eb3258d9da5a2cc78', '5fb588ac581ed9dc8587baa30e2235ee', '68c3fc63fec53b74f3433582781e2375', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('a853d6751b4e4d92be123fcae13d5bec', '5fb588ac581ed9dc8587baa30e2235ee', '82dc0ab24218918c723929fbf17fc7df', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('af96e2dde32f4fd48689315af390f14c', '5fb588ac581ed9dc8587baa30e2235ee', 'a4aa9780d34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('b098bd96a497459b80ac9afb1912769f', '5fb588ac581ed9dc8587baa30e2235ee', 'd48b995bfaa1b102e84b9a7a415f0c30', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('b758921822d644bdacc93bb58bb20d03', '5fb588ac581ed9dc8587baa30e2235ee', '78f4264dcd79ba14a06d3ff68eeb8918', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('b9512988df0b48a680e0895a5b66f330', '5fb588ac581ed9dc8587baa30e2235ee', '50da96cb2332551c81e7f2fa080baa08', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('bb41b0d5ce06475ab479cd8cbe8856b2', '5fb588ac581ed9dc8587baa30e2235ee', '05e970e55191a5c54b3dc6d064bd0770', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('bf0b625c712744baacae9dd950d51128', '5fb588ac581ed9dc8587baa30e2235ee', '9a9168d3b9adec40a0f0dbbca47fcb9e', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('bf1bd6d13fc840ed8fc7da15ad990098', '5fb588ac581ed9dc8587baa30e2235ee', 'ee14d49e9febba0223237b134681f294', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('c08bf0425c994cf0bc1e10f5c67efe52', '5fb588ac581ed9dc8587baa30e2235ee', '064e59da09abc7a621826805967a1644', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('cba4eb350c94464a94b79e488c956ca3', '5fb588ac581ed9dc8587baa30e2235ee', 'a4a32590d34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('cc87870806854afc84110744bd742131', '5fb588ac581ed9dc8587baa30e2235ee', 'd5f47b2455de98b4a4b740d4262c4e74', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('ceac7abc7d7d4baa89fe36aff6bfc0b2', '5fb588ac581ed9dc8587baa30e2235ee', '104cca5a740ca8c69844e76569a144ff', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('d071bca7ec484607bd96a70ffa08e098', '5fb588ac581ed9dc8587baa30e2235ee', '4b840fb231878c23c07c4280f41472aa', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('d0a599f2ac4848e896615f6bf3039eb6', '5fb588ac581ed9dc8587baa30e2235ee', 'b307757a8ca62654d93d2a946972b3d0', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('d1c36b494a0a4b59a0c8225dc413b481', '5fb588ac581ed9dc8587baa30e2235ee', '8e8146f3fc6f1a859aa006a87f1ab74d', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('d30dab4829b4422e8d0fe398c49375f7', '5fb588ac581ed9dc8587baa30e2235ee', '49139aec83b8cb90339ea18331326517', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('d46892d51d6643b583b808ce8cc08dd1', '5fb588ac581ed9dc8587baa30e2235ee', 'a50b70a0d34711ea97e4fg4c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('d5b301cea54443e68841dbe29da93586', '5fb588ac581ed9dc8587baa30e2235ee', '8320d27b295df9bc37fd505535f3291f', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('d5ed77c9f059433799c76adc4894173e', '5fb588ac581ed9dc8587baa30e2235ee', '98f9382cef02ecbd6e8c447b24f4ccc4', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('ded6b1395cb146c39ebd390d667d5be0', '5fb588ac581ed9dc8587baa30e2235ee', '8a76a355bfde58464bc1a0e23e3c85ce', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('dfb37ab2eb5748d3b8b0081105393997', '5fb588ac581ed9dc8587baa30e2235ee', 'a499bc6cd34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('e12bf976d8af4f2bb14845ddbc9f66a2', '5fb588ac581ed9dc8587baa30e2235ee', '088334958df3443bedee1c8757512bf6', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('e2afc223b2f04c1ea14fa39a6bb3c1c9', '5fb588ac581ed9dc8587baa30e2235ee', 'dc6ee3b7b7462c5d3f2eb4a0589a6a7e', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('e73144af025d4185b5fbc92a6fbc94e5', '5fb588ac581ed9dc8587baa30e2235ee', '7547057e6e74e3601c966c02ec506e54', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('e7a519820ca3470b8e4bf4ab11dbedcb', '5fb588ac581ed9dc8587baa30e2235ee', '6b50d84183c45b373e30825a2a41aa2a', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('e98432c2d278455ab314816690fb1fa2', '5fb588ac581ed9dc8587baa30e2235ee', '0139ca12f1ccdfdfa77d47ca32be24c1', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('e9ba0d78bf2c4599b45f080abfa24405', '5fb588ac581ed9dc8587baa30e2235ee', 'd90f82ddcb376084ace8b0ed0e71e643', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('eaab9e6da1954505835b465d77c9c727', '5fb588ac581ed9dc8587baa30e2235ee', '3392f777d70104eeb4e54ca853cc622c', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('ef072d5fd54b4f02a749bdb6640a9300', '5fb588ac581ed9dc8587baa30e2235ee', 'cf596ad75c94cd69559ca0df160c25c6', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('f31726a2493445ed85b33e6754d5ddba', '5fb588ac581ed9dc8587baa30e2235ee', 'a4a0a4aad34711cv97dxa34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('f395d4ae9b674500ba5776f308b6686d', '5fb588ac581ed9dc8587baa30e2235ee', 'df9acb2ccbfe21671e81fc1d8fdf01a1', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('fa193b07f0ca4eb4908bf537c86f4a11', '5fb588ac581ed9dc8587baa30e2235ee', 'e4fb2aabd10c6a571a13164c1b7e2390', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('fa82c81387ce43c6904b4c2c93b8817d', '5fb588ac581ed9dc8587baa30e2235ee', 'a4a87d1ad34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('fc05477a360a4323b1538460d40a3060', '5fb588ac581ed9dc8587baa30e2235ee', '1e7d2d641866ad41e0de397114e38e52', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_function` VALUES ('fe98c287a6dd4a7fb063ebe16ef0e880', '5fb588ac581ed9dc8587baa30e2235ee', 'a51089b4d34711ea97e4a34c90effc21', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_menu`;
CREATE TABLE `tb_core_role_menu` (
  `id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `menu_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单ID',
  `create_user` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色顶级菜单关联表';

-- ----------------------------
-- Records of tb_core_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_role_menu` VALUES ('22c26dd2fbd451290dddc597fcc3a53c', '5fb588ac581ed9dc8587baa30e2235ee', 'e3491279813fd194220ae0a9e703ea73', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_menu` VALUES ('43125255fc052cf1ff667d6664f0b44e', '1', '257dbca6fd17f9016b3801fa567642ab', '1', '2023-04-23 12:29:47', '1', '2023-04-23 12:29:47', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_menu` VALUES ('6abe9b3545a147fd97ee80fca7d9c950', '1', '9ac0634d6ddc381f56822121ed0baf96', '1', '2023-04-23 12:29:47', '1', '2023-04-23 12:29:47', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_menu` VALUES ('7261a2e1d8305f9266b77e0f7f6a8ffb', '1', 'e3491279813fd194220ae0a9e703ea73', '1', '2023-04-23 12:29:47', '1', '2023-04-23 12:29:47', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_menu` VALUES ('7317936b9f3e32d443a39f8da7de9f76', '1', '7585f0e1863b262d61b0cff37dcf28a3', '1', '2023-04-23 12:29:47', '1', '2023-04-23 12:29:47', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_menu` VALUES ('84e92b1c0dd8c06303f5c2c70b87edd7', '5fb588ac581ed9dc8587baa30e2235ee', '77e6ad97d4804f5be0e9da463459f220', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_menu` VALUES ('8b43a5955055dd6c640dc77cfe490d99', '5fb588ac581ed9dc8587baa30e2235ee', '257dbca6fd17f9016b3801fa567642ab', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_menu` VALUES ('b3d745f035ff8a2ab7a238250641eede', '5fb588ac581ed9dc8587baa30e2235ee', '7585f0e1863b262d61b0cff37dcf28a3', '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_role_menu` VALUES ('d85da1107c938ec320464a667aaa1d6b', '1', '77e6ad97d4804f5be0e9da463459f220', '1', '2023-04-23 12:29:47', '1', '2023-04-23 12:29:47', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_tenant
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_tenant`;
CREATE TABLE `tb_core_tenant` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编码',
  `tenant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `domain` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '域名',
  `logo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户Logo',
  `contact_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人电话',
  `address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
  `account_number` int(8) DEFAULT '-1' COMMENT '账号额度',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `license_key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权码',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='租户表';

-- ----------------------------
-- Records of tb_core_tenant
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_tenant` VALUES ('5553e41f179d11eb8189fa163e5c4fd4', '000000', '管理组', '', '', '老总', '15011071226', '', -1, NULL, 'e15fca1478f6e9b4', '1', '2021-03-03 22:34:15', '1', '2023-04-22 20:24:21', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_top_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_top_menu`;
CREATE TABLE `tb_core_top_menu` (
  `id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `client_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
  `code` varchar(25) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单编号',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `icon` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `router` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '首页路由',
  `sort_num` int(6) NOT NULL DEFAULT '1' COMMENT '排序',
  `note` varchar(525) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注说明',
  `create_user` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1启用 0停用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='顶级菜单表';

-- ----------------------------
-- Records of tb_core_top_menu
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_top_menu` VALUES ('257dbca6fd17f9016b3801fa567642ab', '75cfc202d34f11ea97e4a34c90effc21', 'ALL', '全部', 'iconfont iconicon_work', '/wel/index', 0, NULL, '1', '2022-10-26 21:51:19', '1', '2023-04-21 16:55:53', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_top_menu` VALUES ('7585f0e1863b262d61b0cff37dcf28a3', '75cfc202d34f11ea97e4a34c90effc21', 'QX', '权限管理', 'iconfont icon-yanzhengma', '/wel/index', 0, NULL, '1', '2022-10-27 00:11:35', '1', '2022-10-27 19:09:32', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_top_menu` VALUES ('77e6ad97d4804f5be0e9da463459f220', '75cfc202d34f11ea97e4a34c90effc21', 'SYSTEM', '系统设置', 'iconfont iconicon_setting', '/wel/index', 3, NULL, '1', '2022-10-27 19:04:32', '1', '2022-10-27 19:04:32', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_top_menu` VALUES ('9ac0634d6ddc381f56822121ed0baf96', '75cfc202d34f11ea97e4a34c90effc21', 'JPOWER', 'Jpower介绍', 'iconfont iconicon_affiliations_li', '/wel/index', 10, NULL, '1', '2022-10-27 19:08:13', '1', '2022-10-27 19:08:13', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_top_menu` VALUES ('e3491279813fd194220ae0a9e703ea73', '75cfc202d34f11ea97e4a34c90effc21', 'FUWU', '服务监控', 'iconfont icon-wxbgongju', '/wel/index', 4, NULL, '1', '2022-10-27 19:09:21', '1', '2022-10-27 19:09:21', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_core_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_user`;
CREATE TABLE `tb_core_user` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
  `org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组织机构主键',
  `post_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位ID',
  `login_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
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
  `login_count` int(6) DEFAULT '0' COMMENT '登录次数',
  `nick_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `other_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方平台标识',
  `activation_status` tinyint(1) DEFAULT '1' COMMENT '激活状态 1：激活 0：未激活',
  `activation_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '激活码',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `tenant_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '000000' COMMENT '租户编码',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统登录用户表';

-- ----------------------------
-- Records of tb_core_user
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_user` VALUES ('1', '6836de3b179d11eb8189fa163e5c4fd4', NULL, 'root', '$2a$05$zQGxuIbCA/nulLcaVhyvfebr3vtlA/I5nrS9U1dK7zcsSn27q4M.2', '超级管理员', 1, NULL, 0, NULL, '1634566606@qq.com', '15011071226', '内蒙古', '012000', '2023-04-23 15:49:09', 1524, '超级用户', '', 1, '', '1', '2021-03-03 22:34:20', '2', '2023-04-23 15:49:09', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4', 'a553156685aae3282f38837e2d9684b9748527e247e8f480c5718931aba5d6cfe1adcbbd3f07dbd9');
INSERT INTO `tb_core_user` VALUES ('2', '6836de3b179d11eb8189fa163e5c4fd4', NULL, 'anonymous', '', '匿名用户', 1, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, '匿名用户', NULL, 1, NULL, '1', '2021-03-03 22:34:20', '1', '2021-03-03 22:34:20', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4', NULL);
INSERT INTO `tb_core_user` VALUES ('329e4e57adcdcb4b832074086ea9384d', '6836de3b179d11eb8189fa163e5c4fd4', 'b4595c92fd98536aa54059644f2998dc', 'admin', '$2a$05$JdGqOXozOpWLV5fhDncCNue7vrWcZS/OKyEYUMPDGTmkOYSd6akQy', NULL, 1, NULL, 0, NULL, NULL, NULL, NULL, NULL, '2023-04-23 15:45:16', 10, '管理员', NULL, 1, NULL, '1', '2022-09-16 16:57:32', '2', '2023-04-23 15:45:16', 1, 0, '000000', '6836de3b179d11eb8189fa163e5c4fd4', NULL);
COMMIT;

-- ----------------------------
-- Table structure for tb_core_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_user_role`;
CREATE TABLE `tb_core_user_role` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `core_dict_type` (`role_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of tb_core_user_role
-- ----------------------------
BEGIN;
INSERT INTO `tb_core_user_role` VALUES ('611eb4ed59a3d372d12d826fffc8fe3c', '2', '2', '1', '2021-03-03 22:34:25', '1', '2021-03-03 22:34:25', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_user_role` VALUES ('6c2886d3879db2e77e6d348c5cf4089d', '1', '1', '1', '2021-03-03 22:34:25', '1', '2021-03-03 22:34:25', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_user_role` VALUES ('944981497d4950bb97ce0d7a823bc86b', '5fb588ac581ed9dc8587baa30e2235ee', '329e4e57adcdcb4b832074086ea9384d', '1', '2022-09-17 16:30:25', '1', '2022-09-17 16:30:25', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO `tb_core_user_role` VALUES ('9f72c55eaaa23e8d46c0c749a2bf2bad', '6d71adcf2d2e967106a121e4aa90a9d2', '329e4e57adcdcb4b832074086ea9384d', '1', '2022-09-17 17:15:58', '1', '2022-09-17 17:15:58', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
COMMIT;

-- ----------------------------
-- Table structure for tb_log_error
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_error`;
CREATE TABLE `tb_log_error` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
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
  `line_number` int(4) DEFAULT NULL COMMENT '报错行号',
  `exception_name` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常名称',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '异常信息',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='错误日志表';

-- ----------------------------
-- Records of tb_log_error
-- ----------------------------
BEGIN;
INSERT INTO `tb_log_error` VALUES ('0d8a452df7aa1e82195c74c903e7ce5c', 'jpower-system', '127.0.0.1', '127.0.0.1', 'dev', '/core/menu/list', 'GET', 'com.wlcb.jpower.controller.function.TopMenuController', 'list', '{\"pageSize\":[\"20\"],\"pageNum\":[\"1\"],\"clientId_eq\":[\"75cfc202d34f11ea97e4a34c90effc21\"]}', '127.0.0.1', '超级管理员', 0, 'admin', 'java.lang.NumberFormatException: For input string: \"aldjqs是的\"\n	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)\n	at java.lang.Integer.parseInt(Integer.java:580)\n	at java.lang.Integer.parseInt(Integer.java:615)\n	at com.wlcb.jpower.controller.function.TopMenuController.list(TopMenuController.java:111)\n	at com.wlcb.jpower.controller.function.TopMenuController$$FastClassBySpringCGLIB$$e7281da6.invoke(<generated>)\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:783)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:753)\n	at org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor.invoke(MethodBeforeAdviceInterceptor.java:58)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:753)\n	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:64)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:753)\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:753)\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:698)\n	at com.wlcb.jpower.controller.function.TopMenuController$$EnhancerBySpringCGLIB$$8cad180e.list(<generated>)\n	at sun.reflect.GeneratedMethodAccessor371.invoke(Unknown Source)\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n	at java.lang.reflect.Method.invoke(Method.java:498)\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1067)\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655)\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at com.wlcb.jpower.module.common.apm.ApmHttpInfoFilter.doFilter(ApmHttpInfoFilter.java:61)\n	at javax.servlet.http.HttpFilter.doFilter(HttpFilter.java:57)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:124)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at com.wlcb.jpower.module.common.page.PageFilter.doFilter(PageFilter.java:38)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at com.wlcb.jpower.module.configurer.xss.XssFilter.doFilter(XssFilter.java:46)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:96)\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197)\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:540)\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135)\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357)\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382)\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\n	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:895)\n	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1732)\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\n	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\n	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\n	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n	at java.lang.Thread.run(Thread.java:748)\n', 111, 'java.lang.NumberFormatException', 'For input string: \"aldjqs是的\"', '1', '2023-04-22 19:55:39', '1', '2023-04-22 19:55:39', '6836de3b179d11eb8189fa163e5c4fd4', 1, 0);
INSERT INTO `tb_log_error` VALUES ('6da006240bce45f584819de519967614', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', 1, '测试', '测试', 1, '测试', '测试', 'root', '2022-11-02 17:04:40', 'root', '2022-11-02 17:04:40', NULL, 1, 0);
INSERT INTO `tb_log_error` VALUES ('b317d9e4126c479ca4d3cca168a0697f', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', 1, '测试', '测试', 1, '测试', '测试', 'root', '2022-11-02 17:04:44', 'root', '2022-11-02 17:04:44', NULL, 1, 0);
INSERT INTO `tb_log_error` VALUES ('dbb8ca9e62f04567a28807bd90d33ed8', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', 1, '测试', '测试', 1, '测试', '测试', 'root', '2022-11-02 17:04:06', 'root', '2022-11-02 17:04:06', NULL, 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for tb_log_monitor_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_param`;
CREATE TABLE `tb_log_monitor_param` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
  `server` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `path` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '监控地址',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方式',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数类型 字典 PARAM_TYPE（header、path、body、query）',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数名称',
  `value` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '参数值',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='接口监控参数表';

-- ----------------------------
-- Table structure for tb_log_monitor_result
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_result`;
CREATE TABLE `tb_log_monitor_result` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
  `name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '测试地址',
  `tags` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求接口',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方式',
  `error` varchar(289) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求错误',
  `respose` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应数据',
  `respose_code` int(10) DEFAULT NULL COMMENT '响应编码',
  `restful_response` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '接口返回数据',
  `header` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'header参数',
  `body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'body参数',
  `is_success` int(1) DEFAULT NULL COMMENT '是否成功 0否 1是',
  `response_time` int(10) DEFAULT NULL COMMENT '执行时长 单位毫秒',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='接口监控详情';

-- ----------------------------
-- Table structure for tb_log_monitor_setting
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_monitor_setting`;
CREATE TABLE `tb_log_monitor_setting` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
  `server` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `path` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '监控地址',
  `tag` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方式',
  `is_monitor` int(2) DEFAULT '3' COMMENT '是否监控 0:否 1:是 3:未设置',
  `code` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '200' COMMENT 'respose正确code,多个逗号分割',
  `exec_js` varchar(258) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'js代码',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='接口监控设置表';

-- ----------------------------
-- Table structure for tb_log_operate
-- ----------------------------
DROP TABLE IF EXISTS `tb_log_operate`;
CREATE TABLE `tb_log_operate` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主建',
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
  `return_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '返回内容',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '错误消息',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'root' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建部门',
  `status` tinyint(1) DEFAULT '1' COMMENT '操作状态（0正常 1异常）',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='操作日志表';

-- ----------------------------
-- Records of tb_log_operate
-- ----------------------------
BEGIN;
INSERT INTO `tb_log_operate` VALUES ('146f6924a31f1ef66713b1423ab7ecde', 'jpower-system', '127.0.0.1', '127.0.0.1', 'dev', '/core/role/addFunction', 'POST', 'com.wlcb.jpower.controller.role.RoleController', 'addFunction', '{\"roleId\":[\"5fb588ac581ed9dc8587baa30e2235ee\"],\"functionIds\":[\"a50b70a0d34711ea97e4fg4c90effc21,d5f47b2455de98b4a4b740d4262c4e74,28959acc90138a3737292e4678f9785d,a4a0a4aad34711cv97dxa34c90effc21,8e8146f3fc6f1a859aa006a87f1ab74d,55fa29309845b4b2404463f8e82bf305,7f107796b220f7c28aa55794134beb1f,a50ee7f8d34711ea97e4a34c90effc21,a51089b4d34711ea97e4a34c90effc21\"],\"topMenuIds\":[\"\"]}', '127.0.0.1', '超级管理员', 0, 'admin', '重新给角色赋权', 'OTHER', '{\"code\":200,\"message\":\"设置成功\",\"status\":true}', NULL, '1', '2023-04-23 15:40:53', '1', '2023-04-23 15:40:53', '6836de3b179d11eb8189fa163e5c4fd4', 0, 0);
INSERT INTO `tb_log_operate` VALUES ('2d1d39a024f844158acba550b931315b', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', 1, '测试', '测试', '测试', '测试', '测试', 'root', '2022-11-02 16:55:01', 'root', '2022-11-02 16:55:01', NULL, 1, 0);
INSERT INTO `tb_log_operate` VALUES ('a90439ca97a21b31acf4298b289f3ac9', 'jpower-system', '127.0.0.1', '127.0.0.1', 'dev', '/core/role/addFunction', 'POST', 'com.wlcb.jpower.controller.role.RoleController', 'addFunction', '{\"roleId\":[\"5fb588ac581ed9dc8587baa30e2235ee\"],\"functionIds\":[\"55fa29309845b4b2404463f8e82bf305,7f107796b220f7c28aa55794134beb1f,d5f47b2455de98b4a4b740d4262c4e74,a50b70a0d34711ea97e4fg4c90effc21,a4a0a4aad34711cv97dxa34c90effc21,8e8146f3fc6f1a859aa006a87f1ab74d,28959acc90138a3737292e4678f9785d,a51089b4d34711ea97e4a34c90effc21,a50ee7f8d34711ea97e4a34c90effc21,30b4baffc04b6260fgtb79a25f307462,30b4baffc04b6260fgtb79a26f307462,30b4baffc04b4850fgtb79a25f307462,a499bc6cd34711ea97e4a34c90effc21,a4aa9780d34711ea97e4a34c90effc21,a4a5737cd34711ea97e4a34c90effc21,a4acf71ed34711ea97e4a34c90effc21,a4a87d1ad34711ea97e4a34c90effc21,a49c90a4d34711ea97e4a34c90effc21,2749d1d15707546fe611e78745d6605e,a4a0a4aad34711ea97dxa34c90effc21,28a8f330d5b53fffb1284f1af20e7e07,d87d67b15ec4f7444470710540472122,a49e8d5ad34711ea97e4a34c90effc21,a4a0a4aad34711ea97e4a34c90effc21,2d250d5a426f7c84faae2dfe21142284,a4a32590d34711ea97e4a34c90effc21,a49e8d5ad34711ea97e4a34c90effcdz,1e7d2d641866ad41e0de397114e38e52,e541f0720e0decd674d8bc135f67452f,301a686128706b8edc5bfdf1574f97d3,3392f777d70104eeb4e54ca853cc622c,bafbb61d862d363e2daf71c33d3b3d11,9c514e527ab7d69aea1ca17b58519aec,1a378dc1efc575d6a5038166cd6d59f4,28da50bebb1ba81ebc000028d8ff3d78,3971ac0afc6351bb6069f79399a58ff8,cf596ad75c94cd69559ca0df160c25c6,49139aec83b8cb90339ea18331326517,7c2411b236ad9438bbd306bddec2a321,8320d27b295df9bc37fd505535f3291f,cdce4de7f897cb1cb60ba58c2ea398ce,df9acb2ccbfe21671e81fc1d8fdf01a1,088334958df3443bedee1c8757512bf6,0dd90ff68e3e148c1aa7b071183138b2,29a8a209eb57333a8205800839b0b82b,4c44a399505815a5af7b1ab2cdef72e7,5463516a6fc717885fc79becc7e37d21,8cfb31c7ac7b9d6cd2fb155d27b0c754,e4fb2aabd10c6a571a13164c1b7e2390,3a056e624c9d0842d2a6bfe156018db9,72b435250bfcdcfcb0c9b3fe8236d8d0,79cd48eef9a30f8e66cd342132ab1a22,f9a6eaeea225ca40666c3c3ef6bb087d,0139ca12f1ccdfdfa77d47ca32be24c1,0d77c1aa22d261b38d1bb7271058348e,426213f3f8506945ec05f5356487b68a,52143178927c9b9d44088556172e95fa,98f9382cef02ecbd6e8c447b24f4ccc4,064e59da09abc7a621826805967a1644,4cfb90a03a6ac489843fc6125c17aba4,e69cdfe6a5fcd86cc8d8bf3b2d7360e3,f4616c4d9abd8cd57a4eebf4218efb62,68c3fc63fec53b74f3433582781e2375,6dbb90577df794a95f917b9be9ee4e53,6f7b91967e808549fffc880a298983b4,ba80c61488c1447a916abd686d835ab3,e8831b3da10be98a0a721523696df23a,2a830c7ed8d7d2820c0232558af0ed10,5c6bfd675803e18035e7116595f23345,e4854964947a25a2b15113625b23f678,1ca4744f906a91d3a15b8f604f12301d,2285d25ae139eb2d847ae4256e4952cc,9a9168d3b9adec40a0f0dbbca47fcb9e,210c1b3795ea98755e303b08b0840b37,104cca5a740ca8c69844e76569a144ff,1309580f21f0341c2ef8967697732374,21082e0fc9e9534139c29e5b6cc66a13,6f81bbc168d4e5e285534da94889f48c,a3ec6ed70e1c94174b473271693c225e,1b14c81d5414abc225654140b35340b8,8c2ddf69633bc8a01f6b11f7a8383105,92a3af1767fe29cd89a9937fe784705c,cbfae48b6971119dd9f06c6ad72fe6ec,14bc75a54268ff9f03427bbcdb8f69f7,b307757a8ca62654d93d2a946972b3d0,db7b82686ca567f24f377c515105b8fb,110172025be388145d6590f85a5b35b3,50da96cb2332551c81e7f2fa080baa08,52d588f3a3db1004dc314d8cfea85365,72e465a2eedb8abe5d92f8eafa73013b,7547057e6e74e3601c966c02ec506e54,c1a9c36b526dbceed84716419d14a3db,c404579d0223d562ead479c8d3578bf7,c9f0f7eb5093a53bda4f4c9be371181a,d48b995bfaa1b102e84b9a7a415f0c30,ee14d49e9febba0223237b134681f294,04e71d539cc6d1b562f1fa7785c208f1,2bd0e8a69caf613a28104224f2fc14cf,444e0f7df10e2e2501ed42311abc05a9,8a76a355bfde58464bc1a0e23e3c85ce,9562c73e18e848978049af3af1f0c719,0d6cc1e9a3c3c2a227ac6181f8b449ff,1704cea2b65eda99fe424f92032fb7af,1c7ef4b2ebc27bc5b255e619c58bc79b,2804e33bb44875eda5266f6a5e69d4f1,4968faee834a107a882864a1b01928a1,5e02b179c8009798a58f66ab9df2a019,78b1f5a4ea2c69ddc7cac97b7f373dba,78f4264dcd79ba14a06d3ff68eeb8918,d90f82ddcb376084ace8b0ed0e71e643,fb74ed3e83c27c06b6c82e1516de00d9,2014d9f604369c2fad73c2a758432a87,2239cd38a1a2615cb3fb8399e4267422,5286a6b148e3e9877b967575f9b2d1b1,82dc0ab24218918c723929fbf17fc7df,ac304fa4d83dbbc655cbe7d3cf124e31,afcad6282b8254effe993726126e791c,bab173a10b3d6703b8742e889e4001de,d8c9c9fd98571139a1da9c783f0d0c6b,dc6ee3b7b7462c5d3f2eb4a0589a6a7e,05e970e55191a5c54b3dc6d064bd0770,4b840fb231878c23c07c4280f41472aa,6b50d84183c45b373e30825a2a41aa2a,84c67c24e17068cb20793b319e6912bf,9f77f240ade97eb166ec3cd5e965ee1e,167586042020d578ca39e9fe5efa611a,1bfd6d4aecaf4a07a5da7464f387d319,3219465dad1388ab564407ca225f16b1\"],\"topMenuIds\":[\"257dbca6fd17f9016b3801fa567642ab,7585f0e1863b262d61b0cff37dcf28a3,77e6ad97d4804f5be0e9da463459f220,e3491279813fd194220ae0a9e703ea73\"]}', '127.0.0.1', '超级管理员', 0, 'admin', '重新给角色赋权', 'OTHER', '{\"code\":200,\"message\":\"设置成功\",\"status\":true}', NULL, '1', '2023-04-23 15:44:54', '1', '2023-04-23 15:44:54', '6836de3b179d11eb8189fa163e5c4fd4', 0, 0);
INSERT INTO `tb_log_operate` VALUES ('bb1c1e2417534b01b23df28a11da5935', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', '测试', 1, '测试', '测试', '测试', '测试', '测试', 'root', '2022-11-02 16:54:55', 'root', '2022-11-02 16:54:55', NULL, 1, 0);
INSERT INTO `tb_log_operate` VALUES ('ca20313d7371981698638ab10f1b38ec', 'jpower-system', '127.0.0.1', '127.0.0.1', 'dev', '/core/role/addFunction', 'POST', 'com.wlcb.jpower.controller.role.RoleController', 'addFunction', '{\"roleId\":[\"2\"],\"functionIds\":[\"b9ty75ec173611eb84454dgh4rhj6f3f\"],\"topMenuIds\":[\"\"]}', '127.0.0.1', '超级管理员', 0, 'admin', '重新给角色赋权', 'OTHER', '{\"code\":200,\"message\":\"设置成功\",\"status\":true}', NULL, '1', '2023-04-23 15:21:54', '1', '2023-04-23 15:21:54', '6836de3b179d11eb8189fa163e5c4fd4', 0, 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
