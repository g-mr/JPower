
update tb_core_function set url = '/core/dataScope/listDataByParent' where id = '3219465dad1388ab564407ca225f16b1'


alter table tb_core_org change is_virtual type int(1) default 0 null comment '机构类型 字典：ORG_TYPE';

