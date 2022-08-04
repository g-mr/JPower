insert into jpower.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status, is_deleted, is_effect, create_org) values ('39d36d80997f11ea20eb7a10df2f4ffc', 'JPOWER_IS_REGISTER', '是否开启注册', 'false', '是否开启注册', 'root', '2022-06-08 16:05:14', 'root', '2022-06-08 16:05:14', 1, 0, 1, null);

insert into tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status, is_deleted, is_effect, create_org) values ('39d36d80997f11ra80eb7a10df2f4ffc', 'REGISTER_ROLE', '注册用户角色ID', '', '注册用户角色ID', 'root', '2022-06-08 16:10:18', 'root', '2022-06-08 16:10:21', 1, 0, 1, null);

alter table tb_core_user modify password varchar(100) not null comment '登录密码';

alter table tb_core_param drop column is_effect;

