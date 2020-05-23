drop table if exists tb_core_user;
create table tb_core_user(
  id varchar(32) not null COMMENT '主建',
  org_id varchar(32) default null comment '组织机构主键',
  login_id varchar(50) not null comment '登录用户名',
  password varchar(50) not null comment '登录密码',
  user_name varchar(20) default null comment '真实姓名',
  id_type tinyint(1) default 1 comment '证件类型 1:身份证 2:中国护照 3:台胞证 4:外国护照 5:外国人永居证',
  id_no varchar(40) default null comment '证件编码',
  user_type tinyint(1) default 0 comment '用户类型 0:系统用户 1：普通用户 2：单位用户 3:会员 9：匿名用户',
  birthday datetime default null comment '出生日期',
  email varchar(10) default null comment '邮箱地址',
  telephone varchar(11) default null comment '联系电话',
  address varchar(255) default null comment '通讯地址',
  post_code varchar(20) default null comment '邮编',
  last_login_time datetime default null comment '最后登录时间',
  login_count int(6) default 0 comment '登录次数',
  activation_status tinyint(1) default 1 comment '激活状态 1：激活 0：未激活',
  activation_code varchar(10) default null comment '激活码',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  status   tinyint(1) default 1 comment '状态',
  PRIMARY KEY (id) USING BTREE,
  unique key login_id_index(login_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统登录用户表';

drop table if exists tb_core_org;
create table tb_core_org(
 id varchar(32) not null,
 code varchar(50) comment '组织机构编码',
 name varchar(100) not null comment '组织机构名称',
 parent_id varchar(32) comment '父级ID',
 parent_code varchar(50) comment '父级编码',
 icon varchar(100) comment '图标',
 sort int(6) default 0 comment '排序',
 head_name varchar(30) comment '负责人姓名',
 head_phone varchar(11) comment '负责人电话',
 head_email varchar(30) comment '负责人邮箱',
 contact_name varchar(30) comment '联系人姓名',
 contact_phone varchar(11) comment '联系人电话',
 contact_email varchar(30) comment '联系人邮箱',
 address varchar(255) comment '地址',
 is_virtual tinyint(1) default 0 comment '是否虚拟机构 0:否 1：是',
 remark varchar(255) comment '备注',
 create_user varchar(32) default 'root' not null comment '创建人',
 create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
 update_user varchar(32) default 'root' not null comment '更新人',
 update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
 status tinyint(1) default 1 comment '状态',
 PRIMARY KEY (id) USING BTREE,
 unique key code_index(code) USING BTREE
);


drop table if exists tb_core_role;
create table tb_core_role(
  id varchar(32) not null comment '主建',
  code varchar(50) not null comment '角色代码',
  name varchar(50) not null comment '角色名称',
  parent_id varchar(32) comment '上级ID',
  parent_code varchar(50) comment '上级代码',
  icon_url varchar(100) comment '图标地址',
  is_sys_role tinyint(1) default 1 comment '是否系统角色 0:否 1:是',
  remark varchar(255) comment '备注',
  sort int(5) comment '排序',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  status   tinyint(1) default 1 comment '状态',
  PRIMARY KEY (id) USING BTREE
);

drop table if exists tb_core_function;
create table tb_core_function(
 id varchar(32) not null comment '主键',
 function_name varchar(50) not null comment '名称',
 alias varchar(50) comment '别名',
 parent_id varchar(32) comment '父ID',
 code varchar(20) not null comment '功能编码',
 parent_code varchar(20) comment '父级编码',
 url varchar(100) not null comment '资源URL',
 is_menu tinyint(1) default 0 not null comment '是否菜单 0：否 1：是',
 icon varchar(50) comment '图标',
 sort int(6) default 0 comment '排序',
 remark varchar(255) comment '备注',
 moude_summary text comment '模块概述',
 operate_instruction text comment '操作说明',
 function_level int(2) comment '菜单级别',
 create_user varchar(32) default 'root' not null comment '创建人',
 create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
 update_user varchar(32) default 'root' not null comment '更新人',
 update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
 status tinyint(1) default 1 comment '状态',
 PRIMARY KEY (id) USING BTREE,
 unique key url_index(url) USING BTREE,
 unique key code_index(code) USING BTREE
);


drop table if exists tb_core_param;
create table tb_core_param(
  id varchar(32) not null COMMENT '主建',
  code varchar(100) not null COMMENT '参数code',
  name varchar(100) default null COMMENT '参数名称',
  value text not null COMMENT '参数值',
  note varchar(256)  default null COMMENT '备注',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  status   tinyint(1) default 1 comment '状态',
  PRIMARY KEY (id) USING BTREE,
  unique key code_index(code) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('39d36d80997f11ea80eb7a10df2f4ffc', 'isActivation', '用户激活状态', '1', '注册用户默认是否激活', 'root', '2020-05-12 10:33:50', 'root', '2020-05-12 10:33:50', 1);

INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('a3bf0b1c935a11eaa1c1159dd451177f', 'email.protocol', '邮箱服务器配置', 'smtp', '邮件发送使用的服务器', 'root', '2020-05-11 15:40:09', 'root', '2020-05-11 15:40:09', 1);
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('c2597620935a11eaa1c1159dd451177f', 'email.dir', '邮箱服务器附件邮箱保存路径', '/', '邮件发送使用的服务器', 'root', '2020-05-11 15:41:00', 'root', '2020-05-11 15:41:00', 1);
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('c2b19bae932b11eaa1c1159dd451177f', 'noLoginUrl', '登录过滤接口', '/file/export/download', '多个用,分隔', 'root', '2020-05-11 10:04:34', 'root', '2020-05-12 14:52:18', 1);
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('cd9976c6932c11eaa1c1159dd451177f', 'isLogin', '是否开启登录', '0', '0未开启 1开启', 'root', '2020-05-11 10:12:02', 'root', '2020-05-11 11:25:16', 1);
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('cf79e574935a11eaa1c1159dd451177f', 'email.username', '邮箱发件人账号', 'gaojing@nmgylth.com', '邮件发送使用的服务器', 'root', '2020-05-11 15:41:22', 'root', '2020-05-11 15:41:22', 1);
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('e0effbfe935a11eaa1c1159dd451177f', 'email.password', '邮箱发件人密码', 'Ll123456', '邮件发送使用的服务器', 'root', '2020-05-11 15:41:51', 'root', '2020-05-11 15:41:51', 1);
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('e19e2592933911eaa1c1159dd451177f', 'email.host', '邮箱服务器', 'smtp.mxhichina.com', '邮件发送使用的服务器', 'root', '2020-05-11 11:45:39', 'root', '2020-05-11 11:45:39', 1);
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('f36e13fa933811eaa1c1159dd451177f', 'rsa_file_path', '登录密钥文件', '/root/data/subservice/login/config/RSAKey.txt', '密钥文件所在全路径', 'root', '2020-05-11 11:38:59', 'root', '2020-05-12 14:51:30', 1);
INSERT INTO csrrg.tb_core_param (id, code, name, value, note, create_user, create_time, update_user, update_time, status) VALUES ('aa0274c4932c11eaa1c1159dd451177f', 'tokenExpired', '登录token过期时长', '2400000', null, 'root', '2020-05-11 10:11:02', 'root', '2020-05-11 10:11:02', 1);