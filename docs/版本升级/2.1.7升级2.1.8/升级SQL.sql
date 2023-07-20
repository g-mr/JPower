alter table tb_core_function add is_hide tinyint(1) not null default 0 comment '是否隐藏' after function_type;

alter table tb_log_operate add record_id varchar(32) null comment '记录ID' after error_msg;

alter table tb_log_operate add content varchar(255) null comment '记录内容' after return_content;