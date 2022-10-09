INSERT INTO tb_core_function (id, client_id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('35ffa84430244dbc17aad13d7c9997b8', '75cfc202d34f11ea97e4a34c90effc21', '功能点同步', '同步', 'SYSTEM_FUNCTION_GENERATE', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/generate', 0, null, null, null, null, null, null, null, '1', '2022-10-05 20:25:00', '1', '2022-10-05 20:25:00', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');

INSERT INTO tb_core_role_function (id, role_id, function_id, create_user, create_time, update_user, update_time, status, is_deleted, create_org) VALUES ('88e3675c36194f28a1b5d91362778fcd', '1', '35ffa84430244dbc17aad13d7c9997b8', '1', '2022-10-05 20:33:44', '1', '2022-10-05 20:33:44', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');

alter table tb_core_dict add is_stop char(1) default 'N' not null comment '是否停用' after name;

alter table tb_core_dict change locale_id locale varchar(20) default 'zh' not null comment '语言 zh en';

update tb_core_dict set locale = 'zh' where locale = 'zh_cn';