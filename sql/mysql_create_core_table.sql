
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
  email varchar(30) default null comment '邮箱地址',
  telephone varchar(11) default null comment '联系电话',
  address varchar(255) default null comment '通讯地址',
  post_code varchar(20) default null comment '邮编',
  last_login_time datetime default null comment '最后登录时间',
  login_count int(6) default 0 comment '登录次数',
  nick_name varchar(256) default null comment '昵称',
  other_code varchar(64) default null comment '第三方平台标识',
  activation_status tinyint(1) default 1 comment '激活状态 1：激活 0：未激活',
  activation_code varchar(10) default null comment '激活码',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  tenant_code varchar(6) not null default '000000' comment '租户编码',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统登录用户表';


drop table if exists tb_core_role;
create table tb_core_role(
  id varchar(32) not null comment '主建',
  alias varchar(20) comment '角色别名',
  name varchar(50) not null comment '角色名称',
  parent_id varchar(32) comment '上级ID',
  icon_url varchar(100) comment '图标地址',
  is_sys_role tinyint(1) default 1 comment '是否系统角色 0:否 1:是',
  remark varchar(255) comment '备注',
  sort int(5) comment '排序',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  tenant_code varchar(6) not null default '000000' comment '租户编码',
  PRIMARY KEY (id) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '角色表';

drop table if exists tb_core_tenant;
create table tb_core_tenant(
  id varchar(32) not null comment '主建',
  tenant_code varchar(6) not null comment '租户编码',
  tenant_name varchar(50) not null comment '租户名称',
  domain varchar(40) comment '域名',
  background_url varchar(100) comment '系统背景',
  contact_name varchar(20) comment '联系人',
  contact_phone varchar(11) comment '联系人电话',
  address varchar(256) comment '地址',
  account_number int(8) default -1 comment '账号额度',
  expire_time datetime default null comment '过期时间',
  license_key varchar(512) comment '授权码',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '租户表';


drop table if exists tb_core_user_role;
create table tb_core_user_role(
  id varchar(32) not null comment '主键',
  role_id varchar(50) not null comment '角色ID',
  user_id varchar(100) not null comment '用户ID',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE,
  unique key core_dict_type(role_id, user_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '用户角色表';



drop table if exists tb_core_org;
create table tb_core_org(
 id varchar(32) not null,
 code varchar(50) comment '组织机构编码',
 name varchar(100) not null comment '组织机构名称',
 parent_id varchar(32) default '-1' comment '父级ID',
 ancestor_id text default null comment '祖级ID',
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
 create_org varchar(32) null default null comment '创建部门',
 status tinyint(1) default 1 comment '状态',
 is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
 tenant_code varchar(6) not null default '000000' comment '租户编码',
 PRIMARY KEY (id) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '部门表';


drop table if exists tb_core_function;
create table tb_core_function(
 id varchar(32) not null comment '主键',
 function_name varchar(50) not null comment '名称',
 alias varchar(50) comment '别名',
 code varchar(64) not null comment '功能编码',
 parent_id varchar(32) default '-1' comment '父级编码',
 url varchar(100) default null comment '资源URL',
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
 create_org varchar(32) null default null comment '创建部门',
 status tinyint(1) default 1 comment '状态',
 is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
 PRIMARY KEY (id) USING BTREE,
 unique key code_index(code) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '功能菜单表';

drop table if exists tb_core_role_function;
create table tb_core_role_function(
  id varchar(32) not null comment '主键',
  role_id varchar(50) not null comment '角色ID',
  function_id varchar(100) not null comment '菜单ID',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE,
  unique key core_dict_type(role_id, function_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '角色菜单表';

DROP TABLE IF EXISTS `tb_core_data_scope`;
CREATE TABLE `tb_core_data_scope`  (
  id varchar(32) NOT NULL COMMENT '主键',
  menu_id varchar(32) NULL DEFAULT NULL COMMENT '菜单主键',
  scope_code varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  scope_name varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限名称',
  scope_field varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限字段',
  scope_class varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限类名',
  scope_column varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据可见字段',
  scope_type tinyint(1) NULL DEFAULT NULL COMMENT '数据权限类型 字典：DATA_SCOPE_TYPE',
  scope_value varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限值域',
  all_role tinyint(1) not NULL DEFAULT 0 COMMENT '是否所有角色都执行',
  note varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限备注',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status tinyint(1) DEFAULT 1 COMMENT '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE,
  unique key uk_scope_code(scope_code) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '数据权限表';

drop table if exists tb_core_role_data;
create table tb_core_role_data(
  id varchar(32) not null comment '主键',
  role_id varchar(50) not null comment '角色ID',
  data_id varchar(100) not null comment '数据权限ID',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE,
  unique key core_dict_type(role_id, data_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '角色数据权限关联表';

drop table if exists tb_core_param;
create table tb_core_param(
  id varchar(32) not null COMMENT '主建',
  code varchar(100) not null COMMENT '参数code',
  name varchar(100) default null COMMENT '参数名称',
  value text not null COMMENT '参数值',
  is_effect tinyint(1) default 1 comment '是否支持立即生效 0否 1是',
  note varchar(256)  default null COMMENT '备注',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';

drop table if exists tb_core_dict_type;
create table tb_core_dict_type(
  id varchar(32) not null comment '主键',
  dict_type_code varchar(50) not null comment '字典类型代码',
  dict_type_name varchar(100) not null comment '字典类型名称',
  note varchar(100) comment '描述',
  del_enabled char(1) default 'N' not null comment '是否允许删除 N:不允许 Y允许',
  sort_num int(6) default 0 comment '排序',
  parent_id varchar(32) comment '父字典类型ID',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  tenant_code varchar(6) not null default '000000' comment '租户编码',
  PRIMARY KEY (id) USING BTREE,
  unique key core_dict_type(dict_type_code, tenant_code) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '字典类型表';



drop table if exists tb_core_dict;
create table tb_core_dict
(
  id varchar(32) not null comment '主键',
  dict_type_code varchar(50) not null comment '字典类型代码',
  code varchar(100) not null comment '字典代码',
  name varchar(250) not null comment '字典名称',
  locale_id varchar(20) default 'zh_cn' not null comment '语言 zh_cn en_us',
  note varchar(100) comment '备注',
  sort_num int(6) default 0 comment '排序',
  parent_id varchar(32) default '-1' comment '上级ID',
  dict_level int(6) comment '树形字典结构的级别',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  tenant_code varchar(6) not null default '000000' comment '租户编码',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '字典表';


drop table if exists tb_core_city;
create table tb_core_city(
  id varchar(32) not null comment '主键',
  code varchar(100) not null comment '城市编码',
  pcode varchar(100) not null comment '上级编码',
  name varchar(100) not null comment '名称',
  fullname varchar(150) comment '全称',
  rankd int(6) comment '级别 1：省份/直辖市 2：地市 3：区县 4：乡镇/街道 5：村委',
  lng double(10,6) comment '经度',
  lat double(10,6) comment '维度',
  country_code varchar(100) comment '国家编码',
  city_type varchar(10) comment '城市类型1：首都、2：直辖市、3：地级市、4县级市、9：其他',
  note varchar(100) comment '备注',
  sort_num int(6) default 0 comment '排序',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '城市地区表';


drop table if exists tb_core_client;
create table tb_core_client(
  id varchar(32) comment '主键',
  name varchar(128) comment '客户端名称',
  client_code varchar(31) not null comment '客户端CODE，唯一约束',
  client_secret varchar(31) not null comment '客户端密钥',
  access_token_validity int(10) default 0 not null comment 'token过期时间，单位秒',
  refresh_token_validity int(10) default 0 not null comment '刷新token时间，单位秒，时间应该比token过期时间更长',
  sort_num int(6) comment '排序',
  note varchar(255) comment '备注',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '客户端系统表';


drop table if exists tb_core_file;
create table tb_core_file(
  id varchar(32) comment '主键',
  name varchar(127) comment '文件名称',
  file_size int comment '文件大小 单位：字节',
  file_type varchar(31) comment '文件类型',
  path varchar(255) comment '文件路径',
  content longblob comment '文件内容',
  sort_num int(6) comment '排序',
  note varchar(255) comment '备注',
  create_user varchar(32) default 'root' not null comment '创建人',
  create_time datetime not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_user varchar(32) default 'root' not null comment '更新人',
  update_time datetime not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  create_org varchar(32) null default null comment '创建部门',
  status   tinyint(1) default 1 comment '状态',
  is_deleted tinyint(1) default 0 comment '是否删除 0否 1是',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '文件表';

