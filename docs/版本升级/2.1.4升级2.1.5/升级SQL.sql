update tb_core_function set url = '/core/role/tree' where id = 'a4a0a4aad34711ea97e4a34c90effc21';


alter table jpower.tb_core_user
    add post_id varchar(32) null comment '岗位ID' after org_id;


CREATE TABLE `tb_core_post` (
    `id` varchar(32) NOT NULL COMMENT '主键',
    `code` varchar(100) NOT NULL COMMENT '岗位编码',
    `name` varchar(100) NOT NULL COMMENT '岗位名称',
    `type` int(2) DEFAULT NULL COMMENT '岗位类型 字典：POST_TYPE',
    `sort` int(6) DEFAULT NULL COMMENT '排序',
    `describe` varchar(556) DEFAULT NULL COMMENT '岗位描述',
    `condition` varchar(556) DEFAULT NULL COMMENT '上岗条件',
    `create_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` varchar(32) NOT NULL DEFAULT 'root' COMMENT '更新人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` tinyint(1) DEFAULT '1' COMMENT '是否启用 字典：YN01',
    `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否 1是',
    `create_org` varchar(32) DEFAULT NULL,
    `tenant_code` varchar(6) NOT NULL DEFAULT '000000' COMMENT '租户编码',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';