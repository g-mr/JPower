alter table tb_core_role modify is_sys_role int(1) default 1 null comment '是否系统角色 0:否 1:是';

alter table tb_core_role modify status int(1) default 1 null comment '状态';

insert into tb_core_function (id, function_name, alias, code, parent_id, url, is_menu, icon, target, sort, remark, moude_summary, operate_instruction, function_level, create_user, create_time, update_user, update_time, status, is_deleted, create_org) values ('540c9b13f9295f0fcf98f9b3525c3185', '数形菜单', '数形菜单', 'MENU_TREE', 'a4a32590d34711ea97e4a34c90effc21', '/core/function/menuTree', 0, null, '_self', 0, null, null, null, null, 1, '2022-04-21 16:43:57', 1, '2022-04-21 16:43:57', 1, 0, '6836de3b179d11eb8189fa163e5c4fd4');
