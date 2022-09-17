update tb_core_function set url = '/core/role/tree' where id = 'a4a0a4aad34711ea97e4a34c90effc21';


alter table jpower.tb_core_user add post_id varchar(32) null comment '岗位ID' after org_id;

CREATE TABLE `tb_core_post` (
    `id` varchar(32) NOT NULL COMMENT '主键',
    `code` varchar(100) NOT NULL COMMENT '岗位编码',
    `name` varchar(100) NOT NULL COMMENT '岗位名称',
    `type` int(2) DEFAULT NULL COMMENT '岗位类型 字典：POST_TYPE',
    `sort` int(6) DEFAULT NULL COMMENT '排序',
    `describe` varchar(556) DEFAULT NULL COMMENT '岗位描述',
    `condition` varchar(556) DEFAULT NULL COMMENT '上岗条件',
    `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` tinyint(1) DEFAULT '1' COMMENT '是否启用 字典：YN01',
    `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
    `create_org` varchar(32) DEFAULT NULL,
    `tenant_code` varchar(6) NOT NULL DEFAULT '000000' COMMENT '租户编码',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

update tb_core_function set url = '/core/org' where id = 'a49e8d5ad34711ea97e4a34c90effc21';
update tb_core_function set url = '/core/user' where id = 'a4a0a4aad34711ea97e4a34c90effc21';

update tb_core_function set url = '/core/dataScope' where id = 'a49e8d5ad34711ea97e4a34c90effcdz';
update tb_core_function set url = '/core/role' where id = 'a4a32590d34711ea97e4a34c90effc21';

update tb_core_function set url = '/core/tenant' where id = 'f05fe7b4c8817b808f93b095b070d16b';

update tb_core_function set url = '/core/client' where id = 'a49c90a4d34711ea97e4a34c90effc21';
update tb_core_function set url = '/core/file' where id = 'a4a0a4aad34711ea97dxa34c90effc21';
update tb_core_function set url = '/core/function' where id = 'a4a5737cd34711ea97e4a34c90effc21';
update tb_core_function set url = '/core/param' where id = 'a4a87d1ad34711ea97e4a34c90effc21';
update tb_core_function set url = '/core/city' where id = 'a4aa9780d34711ea97e4a34c90effc21';
update tb_core_function set url = '/core/dict' where id = 'a4acf71ed34711ea97e4a34c90effc21';


INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('3a10cd932465453752ead2ad779c0577', '75cfc202d34f11ea97e4a34c90effc21', '详情', '详情', 'POST_DETAIL', 'd87d67b15ec4f7444470710540472122', '/core/post/get', 0, null, '_self', 0, null, null, null, null, '1', '2022-09-17 15:19:34', '1', '2022-09-17 15:19:34', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('3b5073729964dbd1be6f1fd541316ceb', '75cfc202d34f11ea97e4a34c90effc21', '更新', '更新', 'POST_UPDATE', 'd87d67b15ec4f7444470710540472122', '/core/post/update', 0, null, '_self', 0, null, null, null, null, '1', '2022-09-17 15:18:06', '1', '2022-09-17 15:18:06', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('3e822d8a502e54557b2fc09b98a6da02', '75cfc202d34f11ea97e4a34c90effc21', '删除', '删除', 'POST_DELETE', 'd87d67b15ec4f7444470710540472122', '/core/post/delete', 0, null, '_self', 0, null, null, null, null, '1', '2022-09-17 15:18:31', '1', '2022-09-17 15:18:31', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('8758f8218fe22fb21b837d663e0dab5e', '75cfc202d34f11ea97e4a34c90effc21', '新增', '新增', 'POST_ADD', 'd87d67b15ec4f7444470710540472122', '/core/post/add', 0, null, '_self', 0, null, null, null, null, '1', '2022-09-17 15:17:41', '1', '2022-09-17 15:17:41', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('a05573dcad9833bef8e9f012c08c3a2a', '75cfc202d34f11ea97e4a34c90effc21', '分页', '分页', 'POST_PAGE', 'd87d67b15ec4f7444470710540472122', '/core/post/list', 0, null, '_self', 0, null, null, null, null, '1', '2022-09-17 15:17:08', '1', '2022-09-17 15:17:08', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('d62cd201ddd0a629170278fcce4a8313', '75cfc202d34f11ea97e4a34c90effc21', '岗位下拉', '岗位', 'POST_SELECT', 'a4a0a4aad34711ea97e4a34c90effc21', '/core/post/select', 0, null, '_self', 0, null, null, null, null, '1', '2022-09-17 15:15:24', '1', '2022-09-17 15:15:24', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('d87d67b15ec4f7444470710540472122', '75cfc202d34f11ea97e4a34c90effc21', '岗位管理', '岗位', 'POST', '28a8f330d5b53fffb1284f1af20e7e07', '/core/post', 1, 'iconfont iconicon_group', '_self', 3, null, null, null, null, '1', '2022-09-17 15:14:36', '1', '2022-09-17 15:14:36', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');

INSERT INTO jpower.tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('1a2586877d994c0b99cbd89904586630', '1', '3b5073729964dbd1be6f1fd541316ceb', '1', '2022-09-17 17:04:46', '1', '2022-09-17 17:04:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO jpower.tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('5b8b56ef7ad34356a9c3b57127727400', '1', '8758f8218fe22fb21b837d663e0dab5e', '1', '2022-09-17 17:04:46', '1', '2022-09-17 17:04:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO jpower.tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('6232fb4700b44360937c4070a16f5383', '1', '3a10cd932465453752ead2ad779c0577', '1', '2022-09-17 17:04:46', '1', '2022-09-17 17:04:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO jpower.tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('9b001510217f4005bbb273bfba872fe7', '1', 'a05573dcad9833bef8e9f012c08c3a2a', '1', '2022-09-17 17:04:46', '1', '2022-09-17 17:04:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO jpower.tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('9cb5a3f281cd4a3b947b482709d1b5f7', '1', 'd87d67b15ec4f7444470710540472122', '1', '2022-09-17 17:04:46', '1', '2022-09-17 17:04:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO jpower.tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('a42f560c23af4e99a69632af21d52d44', '1', 'd62cd201ddd0a629170278fcce4a8313', '1', '2022-09-17 17:04:46', '1', '2022-09-17 17:04:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO jpower.tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('f584c99f410746058ed56de497f7ba2a', '1', '3e822d8a502e54557b2fc09b98a6da02', '1', '2022-09-17 17:04:46', '1', '2022-09-17 17:04:46', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
