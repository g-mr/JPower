alter table tb_log_error
    modify create_user bigint default 1 null comment '创建人';
alter table tb_log_error
    modify update_user bigint default 1 null comment '更新人';