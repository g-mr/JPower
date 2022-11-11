alter table tb_core_client
	add login_limit varchar(10) null comment '登录限制' after refresh_token_validity;

INSERT INTO tb_core_dict_type(id, dict_type_code, dict_type_name, note, del_enabled, sort_num, parent_id, create_user, create_time, update_user, update_time, status, is_deleted, is_tree, create_org) VALUES ('ec3e14d6464ca0a0f3072b2106757c8f', 'LOGIN_LIMIT', '登录限制', null, 'N', 10, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2022-11-11 19:05:48', '1', '2022-11-11 19:05:48', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_dict(id, dict_type_code, code, name, is_stop, parent_id, locale, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org) VALUES ('7c3398567fe2551836b0b8bdc49a4e70', 'LOGIN_LIMIT', 'NONE', '不限制', 'N', '-1', 'zh', null, 0, '1', '2022-11-11 19:06:03', '1', '2022-11-11 19:06:27', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_dict(id, dict_type_code, code, name, is_stop, parent_id, locale, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org) VALUES ('81442a92c65be42291350078b320645f', 'LOGIN_LIMIT', 'ONE', '单模式', 'N', '-1', 'zh', null, 0, '1', '2022-11-11 19:06:14', '1', '2022-11-11 19:06:14', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_dict(id, dict_type_code, code, name, is_stop, parent_id, locale, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org) VALUES ('89290d1c6cdda60e5dd9a8dd675c5923', 'LOGIN_LIMIT', 'SQUEEZE', '挤掉模式', 'N', '-1', 'zh', null, 0, '1', '2022-11-11 19:06:44', '1', '2022-11-11 19:06:44', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');

-- ----------------------------
-- Table structure for tb_core_top_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_top_menu`;
CREATE TABLE `tb_core_top_menu`  (
    `id` varchar(32)  NOT NULL COMMENT '主键',
    `client_id` varchar(32) NOT NULL COMMENT '客户端ID',
    `code` varchar(25) NOT NULL COMMENT '菜单编号',
    `name` varchar(128) NOT NULL COMMENT '菜单名称',
    `icon` varchar(100) DEFAULT NULL COMMENT '图标',
    `router` varchar(50) DEFAULT NULL COMMENT '首页路由',
    `sort_num` int(6) NOT NULL DEFAULT 1 COMMENT '排序',
    `note` varchar(525) NULL DEFAULT NULL COMMENT '备注说明',
    `create_user` varchar(32)  NOT NULL DEFAULT 'root' COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_user` varchar(32)  NOT NULL DEFAULT 'root' COMMENT '更新人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1启用 0停用',
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0否 1是',
    `create_org` varchar(32)  NULL DEFAULT NULL COMMENT '创建部门',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '顶级菜单表';

-- ----------------------------
-- Table structure for tb_core_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_role_menu`;
CREATE TABLE `tb_core_role_menu`  (
    `id` varchar(32)  NOT NULL COMMENT '主键',
    `role_id` varchar(32) NOT NULL COMMENT '角色ID',
    `menu_id` varchar(32) NOT NULL COMMENT '菜单ID',
    `create_user` varchar(32)  NOT NULL DEFAULT 'root' COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_user` varchar(32)  NOT NULL DEFAULT 'root' COMMENT '更新人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态',
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0否 1是',
    `create_org` varchar(32)  NULL DEFAULT NULL COMMENT '创建部门',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色顶级菜单关联表';

-- ----------------------------
-- Table structure for tb_core_function_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_core_function_menu`;
CREATE TABLE `tb_core_function_menu`  (
    `id` varchar(32)  NOT NULL COMMENT '主键',
    `function_id` varchar(32) NOT NULL COMMENT '功能ID',
    `menu_id` varchar(32) NOT NULL COMMENT '菜单ID',
    `create_user` varchar(32)  NOT NULL DEFAULT 'root' COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_user` varchar(32)  NOT NULL DEFAULT 'root' COMMENT '更新人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态',
    `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0否 1是',
    `create_org` varchar(32)  NULL DEFAULT NULL COMMENT '创建部门',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '功能菜单顶级菜单关联表';



INSERT INTO tb_core_top_menu (id, client_id, code, name, sort_num, router, icon, note, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('257dbca6fd17f9016b3801fa567642ab', '75cfc202d34f11ea97e4a34c90effc21', 'ALL', '全部', 0, '/wel/index', 'iconfont iconicon_work', null, '1', '2022-10-26 21:51:19', '1', '2022-10-27 18:56:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_top_menu (id, client_id, code, name, sort_num, router, icon, note, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('7585f0e1863b262d61b0cff37dcf28a3', '75cfc202d34f11ea97e4a34c90effc21', 'QX', '权限管理', 0, '/wel/index', 'iconfont icon-yanzhengma', null, '1', '2022-10-27 00:11:35', '1', '2022-10-27 19:09:32', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_top_menu (id, client_id, code, name, sort_num, router, icon, note, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('77e6ad97d4804f5be0e9da463459f220', '75cfc202d34f11ea97e4a34c90effc21', 'SYSTEM', '系统设置', 3, '/wel/index', 'iconfont iconicon_setting', null, '1', '2022-10-27 19:04:32', '1', '2022-10-27 19:04:32', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_top_menu (id, client_id, code, name, sort_num, router, icon, note, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('9ac0634d6ddc381f56822121ed0baf96', '75cfc202d34f11ea97e4a34c90effc21', 'JPOWER', 'Jpower介绍', 10, '/wel/index', 'iconfont iconicon_affiliations_li', null, '1', '2022-10-27 19:08:13', '1', '2022-10-27 19:08:13', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_top_menu (id, client_id, code, name, sort_num, router, icon, note, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('e3491279813fd194220ae0a9e703ea73', '75cfc202d34f11ea97e4a34c90effc21', 'FUWU', '服务监控', 4, '/wel/index', 'iconfont icon-wxbgongju', null, '1', '2022-10-27 19:09:21', '1', '2022-10-27 19:09:21', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');


INSERT INTO tb_core_role_menu (id, role_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('551dea8ba46f71f626b9a68f488a6cd3', '1', '9ac0634d6ddc381f56822121ed0baf96', '1', '2022-10-27 19:10:09', '1', '2022-10-27 19:10:09', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_menu (id, role_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('5e1a6de1963d9286c1811718ccfc3d2a', '1', '77e6ad97d4804f5be0e9da463459f220', '1', '2022-10-27 19:10:09', '1', '2022-10-27 19:10:09', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_menu (id, role_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('ea8548dd7aa437000de488784bf569bb', '1', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 19:10:09', '1', '2022-10-27 19:10:09', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_menu (id, role_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('f987e4d59e6763bf40ef2e137fb3c6a6', '1', 'e3491279813fd194220ae0a9e703ea73', '1', '2022-10-27 19:10:09', '1', '2022-10-27 19:10:09', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_menu (id, role_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('fcc71e9a67f959a481885be7dd1389f4', '1', '7585f0e1863b262d61b0cff37dcf28a3', '1', '2022-10-27 19:10:09', '1', '2022-10-27 19:10:09', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');



INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('0876c884638a0e6e2728896852e1b39c', '051f2e0b81bb4b80706a61e5b78ba507', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('3ed2ae3ed7511309006aae781608afa5', '28a8f330d5b53fffb1284f1af20e7e07', '7585f0e1863b262d61b0cff37dcf28a3', '1', '2022-10-27 00:47:37', '1', '2022-10-27 00:47:37', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('4d4e9d9b7d35a63ba14f2b4844710364', '2d250d5a426f7c84faae2dfe21142284', '7585f0e1863b262d61b0cff37dcf28a3', '1', '2022-10-27 00:47:37', '1', '2022-10-27 00:47:37', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('5cf53193d0efdd0a004d3feb8b2f8912', '051f2e0b81bb4b80706a61e5b78ba507', '9ac0634d6ddc381f56822121ed0baf96', '1', '2022-10-27 19:08:34', '1', '2022-10-27 19:08:34', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('a7dc8a0f25f7bf1d447d7e729fae6967', '30b4baffc04b6260fgtb79a25f307462', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('b7685bc41006b778e621fb5b42fb91a3', '2d250d5a426f7c84faae2dfe21142284', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('b7a4450bd28341bb978545cba965f37f', '9cf521fe6e03970fe8f503ca9c5052a2', '9ac0634d6ddc381f56822121ed0baf96', '1', '2022-10-27 19:08:34', '1', '2022-10-27 19:08:34', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('bb60f0b11671bcf8c6cdd2d0539a9f59', '30b4baffc04b6260fgtb79a25f307462', 'e3491279813fd194220ae0a9e703ea73', '1', '2022-10-27 19:11:49', '1', '2022-10-27 19:11:49', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('c64a9618a09b70503dbd2650645eae47', 'a499bc6cd34711ea97e4a34c90effc21', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('c79c16fdde1a7378af1a65af850bbd8b', '9cf521fe6e03970fe8f503ca9c5052a2', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('de419fcfd3de52dfc1d2219a335dd2b4', '30b4baffc04b62602bdb79a25f307462', 'e3491279813fd194220ae0a9e703ea73', '1', '2022-10-27 19:11:49', '1', '2022-10-27 19:11:49', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('e0b185f70505b832feb77e2b2f1420ff', 'a499bc6cd34711ea97e4a34c90effc21', '77e6ad97d4804f5be0e9da463459f220', '1', '2022-10-27 19:04:51', '1', '2022-10-27 19:04:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('e7bbff78543bb9897719626ee7be3072', '30b4baffc04b62602bdb79a25f307462', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('f4d7f9c3357de803c5fed1611c9b39bf', 'b9ae75ec173611eb84474dda60506f3f', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function_menu (id, function_id, menu_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('f7c5cd445b88b9797dcdf42aa77e9846', '28a8f330d5b53fffb1284f1af20e7e07', '257dbca6fd17f9016b3801fa567642ab', '1', '2022-10-27 00:47:26', '1', '2022-10-27 00:47:26', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');



INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('8e8146f3fc6f1a859aa006a87f1ab74d', '75cfc202d34f11ea97e4a34c90effc21', '顶部菜单', '顶部菜单', 'TOP_MENU', '-1', '/core/menu/roleMenu', 0, null, '_self', 99, null, null, null, null, '1', '2022-10-27 19:14:51', '1', '2022-10-27 19:15:10', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('034043b57b9719cf4ac81803856e9915', '75cfc202d34f11ea97e4a34c90effc21', '顶部菜单ID', '顶部菜单ID', 'ROLE_TOPMENU_ID', 'a4a32590d34711ea97e4a34c90effc21', '/core/role/topMenuId', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('b27277b12c1edc6bca22f82538994c58', '75cfc202d34f11ea97e4a34c90effc21', '保存顶部菜单', '保存顶部菜单', 'ROLE_TOPMENU_SAVE', 'a4a32590d34711ea97e4a34c90effc21', '/core/role/saveTopMenu', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('bd6e60ca158935d619f3dcac5bf2ed61', '75cfc202d34f11ea97e4a34c90effc21', '顶部菜单', '顶部菜单', 'ROLE_TOPMENU', 'a4a32590d34711ea97e4a34c90effc21', '/core/menu/listName', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('2749d1d15707546fe611e78745d6605e', '75cfc202d34f11ea97e4a34c90effc21', '顶级菜单', '顶级菜单', 'SYSTEM_TOPMENU', 'a499bc6cd34711ea97e4a34c90effc21', '/core/menu', 1, 'iconfont iconicon_subordinate', '_self', 4, null, null, null, null, '1', '2022-10-26 20:55:50', '1', '2022-10-26 20:55:50', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('172c0f6a1e42981c5b084c2d03f55bfe', '75cfc202d34f11ea97e4a34c90effc21', '客户端下拉', '客户端下拉', 'TOPMENU_CLIENT_SELECT', '2749d1d15707546fe611e78745d6605e', '/core/client/selectList', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('27307e66a972f66dcfc3302b9e206282', '75cfc202d34f11ea97e4a34c90effc21', '删除菜单', '删除菜单', 'TOPMENU_DELETE', '2749d1d15707546fe611e78745d6605e', '/core/menu/delete', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('3da7c3acebe4eca1dd7edbe60f603c4e', '75cfc202d34f11ea97e4a34c90effc21', '新增菜单', '新增菜单', 'TOPMENU_ADD', '2749d1d15707546fe611e78745d6605e', '/core/menu/add', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('4686b06a18eb3d8d327f11c6fca3ee9a', '75cfc202d34f11ea97e4a34c90effc21', '更新菜单', '更新菜单', 'TOPMENU_UPDATE', '2749d1d15707546fe611e78745d6605e', '/core/menu/update', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('4c94c9d54c61a520e9f66f5e0bb13c23', '75cfc202d34f11ea97e4a34c90effc21', '菜单列表', '菜单列表', 'TOPMENU_LIST', '2749d1d15707546fe611e78745d6605e', '/core/menu/list', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('d2557eeb36723514bc8fbb6158f075bf', '75cfc202d34f11ea97e4a34c90effc21', '一级菜单', '一级菜单', 'TOPMENU_FUNCTION', '2749d1d15707546fe611e78745d6605e', '/core/menu/listFunction', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('d8dd12737421937269b92b0cded6ed9c', '75cfc202d34f11ea97e4a34c90effc21', '保存一级菜单', '保存一级菜单', 'TOPMENU_FUNCTION_SAVE', '2749d1d15707546fe611e78745d6605e', '/core/menu/saveFunction', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function(id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('daba34e9d19328d482ca1f86648b38c2', '75cfc202d34f11ea97e4a34c90effc21', '关联一级菜单ID', '关联一级菜单ID', 'TOPMENU_FUNCTION_ID', '2749d1d15707546fe611e78745d6605e', '/core/menu/listFunctionId', 0, null, '_self', null, null, null, null, null, '1', '2022-10-26 20:56:08', '1', '2022-10-26 20:56:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');




INSERT INTO tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('395c881erwf744f880728d22ca8bc4e9', '1', '8e8146f3fc6f1a859aa006a87f1ab74d', '1', '2022-10-27 19:18:08', '1', '2022-10-27 19:18:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('00aa020f191c4e0da36d1c8fc1999995', '1', '034043b57b9719cf4ac81803856e9915', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('625a5132527c454f8fc5751119c03fe9', '1', '172c0f6a1e42981c5b084c2d03f55bfe', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('3cf860a36fb14b3e9708927b8c250a90', '1', '27307e66a972f66dcfc3302b9e206282', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('ee59a8f67aa04f5196f99d2ade65c72e', '1', '2749d1d15707546fe611e78745d6605e', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('766d176d26824d38b56a8c443df6445e', '1', '3da7c3acebe4eca1dd7edbe60f603c4e', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('f38a55be992b45d98176db0b1ff3fc7f', '1', '4686b06a18eb3d8d327f11c6fca3ee9a', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('7048c6a4b3bf48ec9362c4de6c25d0d7', '1', '4c94c9d54c61a520e9f66f5e0bb13c23', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('218d8e912f034d558542212419003918', '1', 'b27277b12c1edc6bca22f82538994c58', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('26e5c774dba14ad29a3c18ae59ace2e9', '1', 'bd6e60ca158935d619f3dcac5bf2ed61', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('5ea3adf5e96247d1a561c0b0dcf58203', '1', 'd2557eeb36723514bc8fbb6158f075bf', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('6dd09220e75a490e84e08221560f9d76', '1', 'd8dd12737421937269b92b0cded6ed9c', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_role_function(id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('a9aa014747d84b21bfad467c8bd5aab9', '1', 'daba34e9d19328d482ca1f86648b38c2', '1', '2022-10-26 21:30:51', '1', '2022-10-26 21:30:51', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');