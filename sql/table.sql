drop table if exists tb_core_param;
create table tb_core_param(
  id varchar(32) not null COMMENT '主建',
  code varchar(100) not null COMMENT '参数code',
  name varchar(100) default null COMMENT '参数名称',
  value text not null COMMENT '参数值',
  note varchar(256)  default null COMMENT '备注',
  create_user varchar(32) DEFAULT NULL COMMENT '新增人',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  update_user varchar(32) DEFAULT NULL COMMENT '更新人',
  update_time  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  status  tinyint(1) default 1,
  PRIMARY KEY (id) USING BTREE,
  unique key code_index(code) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';