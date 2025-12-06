alter table tb_log_error
    modify create_user bigint default 1 null comment '创建人';
alter table tb_log_error
    modify update_user bigint default 1 null comment '更新人';

alter table tb_core_tenant
    add config text null comment '设置内容' after license_key;

