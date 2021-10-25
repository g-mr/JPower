-- 文件表新增字段
alter table tb_core_file add `storage_type` varchar(20) DEFAULT 'SERVER' COMMENT '存储类型 字典FILE_STORAGE_TYPE';

-- 新增字典类型
INSERT INTO tb_core_dict_type (id, dict_type_code, dict_type_name, note, del_enabled, sort_num, parent_id, create_user, create_time, update_user, update_time, status, is_deleted, tenant_code, is_tree, create_org)
VALUES ('35b9e892d34e11ea97e4a34c56effc21', 'FILE_STORAGE_TYPE', '文件存储类型', null, 'N', 1, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2021-03-03 22:33:28', '1', '2021-03-03 22:33:28', 1, 0, '000000', null, '6836de3b179d11eb8189fa163e5c4fd4');

-- 新增字典
INSERT INTO tb_core_dict (id, dict_type_code, code, `name`, parent_id, locale_id, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org)
VALUES ('1b31a29bc63f5729ba7732dbe878c4b6', 'FILE_STORAGE_TYPE', 'SERVER', '服务器', '-1', 'zh_cn', null, 1, '1', '2021-03-05 16:58:37', '1', '2021-03-05 16:58:37', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_dict (id, dict_type_code, code, `name`, parent_id, locale_id, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org)
VALUES ('1b31a29bsz3f5729ba7732dbe878c4b6', 'FILE_STORAGE_TYPE', 'FASTDFS', 'fastdfs', '-1', 'zh_cn', null, 1, '1', '2021-03-05 16:58:37', '1', '2021-03-05 16:58:37', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_dict (id, dict_type_code, code, `name`, parent_id, locale_id, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org)
VALUES ('1b31a29br53f5729ba7732dbe878c4b6', 'FILE_STORAGE_TYPE', 'DATABASE', '数据库', '-1', 'zh_cn', null, 1, '1', '2021-03-05 16:58:37', '1', '2021-03-05 16:58:37', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');