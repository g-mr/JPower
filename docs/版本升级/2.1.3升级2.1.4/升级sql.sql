insert into tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status, is_deleted, is_effect, create_org) values ('39d36d80997f11ea20eb7a10df2f4ffc', 'JPOWER_IS_REGISTER', '是否开启注册', 'false', '是否开启注册', 'root', '2022-06-08 16:05:14', 'root', '2022-06-08 16:05:14', 1, 0, 1, null);
insert into tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status, is_deleted, is_effect, create_org) values ('39d36d80997f11ra80eb7a10df2f4ffc', 'REGISTER_ROLE', '注册用户角色ID', '', '注册用户角色ID', 'root', '2022-06-08 16:10:18', 'root', '2022-06-08 16:10:21', 1, 0, 1, null);

alter table tb_core_user modify password varchar(100) not null comment '登录密码';

alter table tb_core_param drop column is_effect;

drop index code_index on tb_core_org;

create index code_index on tb_core_org (code) using BTREE ;

create index code_index on tb_core_city (code) using btree ;

create index pcode_index on tb_core_city (pcode) using btree ;

alter table tb_log_error modify param text null;

alter table tb_log_operate modify param text null;

alter table tb_core_function add client_id varchar(32) null comment '客户端ID' after id;

-- 加入这个字段后需要给这个字段的值是默认的客户端ID即可
update tb_core_function set client_id = '75cfc202d34f11ea97e4a34c90effc21';

delete from tb_core_function where id in ('a4f353b2d34711ea97e4a34c90effc21','a4f5525cd34711ea97e4a34c90effc21');
delete from tb_core_role_function where function_id in ('a4f353b2d34711ea97e4a34c90effc21','a4f5525cd34711ea97e4a34c90effc21');

UPDATE tb_core_function SET client_id='75cfc202d34f11ea97e4a34c90effc21', function_name='客户端菜单树形', alias='客户端菜单树', code='SYSTEM_CLIENT_MENUTREE', parent_id='f05fe7b4c8817b808f93b095b070d16b', url='/core/function/clientMenuTree', is_menu=0, target='_self', sort=3, function_level=3, update_user='1', update_time='2022-08-10 03:23:23', status=1 WHERE id='4e2203c3d111d8ab1d70c2bae80bba33';

INSERT INTO tb_core_function ( id, client_id, function_name, alias, code, parent_id, url, is_menu, target, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '4d1a9cc9dee1f5274d2502ab9cca621d', '75cfc202d34f11ea97e4a34c90effc21', '树形字典', '树形字典', 'TREE_DICT', 'a4acf71ed34711ea97e4a34c90effc21', '/core/dict/treeDict', 0, '_self', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:10:39', '1', '2022-08-10 03:10:39', 1, false );
INSERT INTO tb_core_function ( id, client_id, function_name, alias, code, parent_id, url, is_menu, target, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '3022dcd0ee45496eb94d0fdba1e9f8c6', '75cfc202d34f11ea97e4a34c90effc21', '树形菜单', '菜单树', 'MENU_TREE', 'a4a5737cd34711ea97e4a34c90effc21', '/core/function/menuTree', 0, '_self', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:26:00', '1', '2022-08-10 03:26:00', 1, false );
INSERT INTO tb_core_function ( id, client_id, function_name, alias, code, parent_id, url, is_menu, target, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '1c566bdb08e3e61ce3917980c5f58d64', '75cfc202d34f11ea97e4a34c90effc21', '客户端下拉列表', '客户端下拉框', 'FUNCTION_CLIENT_SELECT', 'a4a5737cd34711ea97e4a34c90effc21', '/core/client/selectList', 0, '_self', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:29:06', '1', '2022-08-10 03:29:06', 1, false );
INSERT INTO tb_core_function ( id, client_id, function_name, alias, code, parent_id, url, is_menu, target, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( 'dac491e3cce67e6dab1af37cc01b9062', '75cfc202d34f11ea97e4a34c90effc21', '客户端下拉列表', '客户端下拉框', 'DATASCOPE_CLIENT_SELECT', 'a49e8d5ad34711ea97e4a34c90effcdz', '/core/client/selectList', 0, '_self', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:30:15', '1', '2022-08-10 03:30:15', 1, false );
INSERT INTO tb_core_function ( id, client_id, function_name, alias, code, parent_id, url, is_menu, target, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '726ab3ed0fbe2cac0cfe7d5101886b2f', '75cfc202d34f11ea97e4a34c90effc21', '客户端下拉列表', '客户端下拉框', 'ROLE_CLIENT_SELECT', 'a4a32590d34711ea97e4a34c90effc21', '/core/client/selectList', 0, '_self', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:31:17', '1', '2022-08-10 03:31:17', 1, false );


INSERT INTO tb_core_role_function ( id, role_id, function_id, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '5fbc4fed913e471d969f2f7a6a7c4f4d', '1', '3022dcd0ee45496eb94d0fdba1e9f8c6', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:13:17', '1', '2022-08-10 03:13:17', 1, false );
INSERT INTO tb_core_role_function ( id, role_id, function_id, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '5fbc4fed913e471d969f232a6a7c4f4d', '1', '4d1a9cc9dee1f5274d2502ab9cca621d', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:13:17', '1', '2022-08-10 03:13:17', 1, false );
INSERT INTO tb_core_role_function ( id, role_id, function_id, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '5fbc4fed913e471d969f245a6a7c4f4d', '1', '1c566bdb08e3e61ce3917980c5f58d64', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:13:17', '1', '2022-08-10 03:13:17', 1, false );
INSERT INTO tb_core_role_function ( id, role_id, function_id, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '5fbc4fed913e471d969f212a6a7c4f4d', '1', 'dac491e3cce67e6dab1af37cc01b9062', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:13:17', '1', '2022-08-10 03:13:17', 1, false );
INSERT INTO tb_core_role_function ( id, role_id, function_id, create_org, create_user, create_time, update_user, update_time, status, is_deleted ) VALUES ( '5fbc4fed913e471d969f256a6a7c4f4d', '1', '726ab3ed0fbe2cac0cfe7d5101886b2f', '9c356b1f61fca21919bea15e1ea9641b', '1', '2022-08-10 03:13:17', '1', '2022-08-10 03:13:17', 1, false );
