
alter table tb_core_role add ancestor_id varchar(1000) null comment '祖级ID' after parent_id;

-- 需要手动把tb_core_role表的ancestor_id字段数据补齐，值是所有祖父级ID

alter table tb_core_role modify status int(1) default 1 null comment '状态';

