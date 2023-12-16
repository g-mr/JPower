
update tb_core_function set url = '/core/dataScope/listDataByParent' where id = '3219465dad1388ab564407ca225f16b1'


alter table tb_core_org change is_virtual type int(1) default 0 null comment '机构类型 字典：ORG_TYPE';

INSERT INTO tb_core_dict_type (id, dict_type_code, dict_type_name, note, del_enabled, sort_num, parent_id, create_user, create_time, update_user, update_time, status, is_deleted, is_tree, create_org) VALUES ('1730156050968240130', 'ORG_TYPE', '机构类型', null, 'Y', 0, '35b7cae4d34e11ea97e4a34c90effc21', '1', '2023-11-30 17:25:22', '1', '2023-11-30 17:25:22', 1, 0, 0, '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_dict (id, dict_type_code, code, name, is_stop, parent_id, locale, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org) VALUES ('1730156093074857986', 'ORG_TYPE', '1', '公司', 'N', '-1', 'zh', null, 0, '1', '2023-11-30 17:25:32', '1', '2023-11-30 17:25:32', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_dict (id, dict_type_code, code, name, is_stop, parent_id, locale, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org) VALUES ('1730156115350806530', 'ORG_TYPE', '2', '部门', 'N', '-1', 'zh', null, 0, '1', '2023-11-30 17:25:37', '1', '2023-11-30 17:25:37', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');
INSERT INTO tb_core_dict (id, dict_type_code, code, name, is_stop, parent_id, locale, note, sort_num, create_user, create_time, update_user, update_time, status, is_deleted, dict_level, pcode, tenant_code, create_org) VALUES ('1730156145692401665', 'ORG_TYPE', '3', '小组', 'N', '-1', 'zh', null, 0, '1', '2023-11-30 17:25:45', '1', '2023-11-30 17:25:45', 1, 0, null, '-1', '000000', '6836de3b179d11eb8189fa163e5c4fd4');


alter table tb_core_function add ancestor_id varchar(255) default '-1' not null comment '祖级ID' after parent_id;


UPDATE tb_core_function
SET ancestor_id = concat(ancestor_id,',',parent_id)
WHERE
        parent_id IN (
        SELECT id
        FROM (
                 SELECT
                     id
                 FROM
                     tb_core_function
                 WHERE
                         parent_id = '-1'
             ) as tmp
    );



UPDATE tb_core_function f left join  tb_core_function f1 on f.parent_id = f1.id
SET f.ancestor_id = concat(f1.ancestor_id,',',f.parent_id)
WHERE
        f.parent_id IN (
        SELECT id
        FROM (
                 select id
                 from tb_core_function where parent_id IN (

                     SELECT
                         id
                     FROM
                         tb_core_function
                     WHERE
                             parent_id = '-1'

                 )) as tmp
    );



UPDATE tb_core_function f left join  tb_core_function f1 on f.parent_id = f1.id
SET f.ancestor_id = concat(f1.ancestor_id,',',f.parent_id)
WHERE
        f. parent_id in (
        SELECT id
        FROM (
                 select id
                 from tb_core_function
                 WHERE
                         parent_id IN (

                         select id
                         from tb_core_function where parent_id IN (

                             SELECT
                                 id
                             FROM
                                 tb_core_function
                             WHERE
                                     parent_id = '-1'

                         )
                     )
             ) as tmp
    );


alter table tb_core_data_scope drop key uk_scope_code;