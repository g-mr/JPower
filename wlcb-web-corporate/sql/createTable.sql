drop table if exists tbl_csrrg_corporate;
CREATE TABLE `tbl_csrrg_corporate` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `area` varchar(30) DEFAULT NULL COMMENT '地区',
  `enterprise_name` varchar(30) NOT NULL COMMENT '公司名称',
  `legal_person` varchar(10) NOT NULL COMMENT '法人',
  `enterprise_range` varchar(100) DEFAULT null COMMENT '经营范围',
  `legal_idcard` varchar(18) DEFAULT null COMMENT '法人身份证号',
  `enterprise_authority` varchar(50) DEFAULT null COMMENT '所属管理局',
  `enterprise_status` varchar(15) DEFAULT null COMMENT '企业状态',
  `organization_code` varchar(50) DEFAULT null COMMENT '组织机构代码',
  `create_user` varchar(32) DEFAULT 'root' COMMENT '作成者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成时间',
  `update_user` varchar(32) DEFAULT 'root' COMMENT '修改者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int(5) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='企业法人表';

drop table if exists tbl_csrrg_record;
CREATE TABLE `tbl_csrrg_record` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `corporate_id` varchar(32) DEFAULT NULL COMMENT '企业信息ID',
  `enterprise_name` varchar(32) DEFAULT NULL COMMENT '企业名称  冗余存 提高查询速度',
  `applicant_openid` varchar(32) DEFAULT NULL COMMENT '申请人openid',
  `applicant_name` varchar(30) NOT NULL COMMENT '申请人姓名',
  `applicant_idcard` varchar(18) NOT NULL COMMENT '申请人身份证号',
  `applicant_username` varchar(100) DEFAULT null COMMENT '申请人用户名',
  `applicant_phone` varchar(18) DEFAULT null COMMENT '申请人电话',
  `applicant_quxian` varchar(50) DEFAULT null COMMENT '申请人区县',
  `applicant_shequ` varchar(15) DEFAULT null COMMENT '申请人社区',
  `applicant_jiedao` varchar(50) DEFAULT null COMMENT '申请人街道',
  `applicant_xiaoqu` varchar(50) DEFAULT null COMMENT '申请人小区',
  `applicant_shequ_id` varchar(32) DEFAULT null COMMENT '申请人社区ID',
  `applicant_jiedao_id` varchar(32) DEFAULT null COMMENT '申请人街道ID',
  `applicant_xiaoqu_id` varchar(32) DEFAULT null COMMENT '申请人小区ID',
  `file_path` varchar(50) DEFAULT null COMMENT '证件地址',
  `applicant_status` integer(50) DEFAULT null COMMENT '申请状态 1：申请成功 2：申请失败 3：审核中 4：匹配成功',
  `fail_reason` varchar(50) DEFAULT null COMMENT '申请失败原因',
  `create_user` varchar(32) DEFAULT 'root' COMMENT '作成者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成时间',
  `update_user` varchar(32) DEFAULT 'root' COMMENT '修改者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int(5) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='申请记录表';


drop table if exists tbl_csrrg_rework;
CREATE TABLE `tbl_csrrg_rework` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `openid` varchar(50) DEFAULT NULL COMMENT '申请人openId',
  `corporate_id` varchar(32) NOT NULL COMMENT '企业ID',
  `corporate_people_sum` int(6) NOT NULL COMMENT '企业人数',
  `corporate_rep_sum` int(6) DEFAULT null COMMENT '外地归来人数',
  `plan_path` varchar(80) DEFAULT null COMMENT '企业防疫预案文件路径',
  `committed_path` varchar(80) DEFAULT null COMMENT '承诺书文件路径',
  `review_stats` int(1) DEFAULT 3 COMMENT '审核状态 1:申请通过 2：申请拒绝 3：审核中',
  `refuse_reason` varchar(30) DEFAULT null COMMENT '审核拒绝原因',
  `create_user` varchar(32) DEFAULT 'root' COMMENT '作成者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成时间',
  `update_user` varchar(32) DEFAULT 'root' COMMENT '修改者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='复工申请表';



drop table if exists tbl_csrrg_log;
CREATE TABLE `tbl_csrrg_log` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `key_id` varchar(32) NOT NULL COMMENT '操作表ID',
  `user_id` varchar(32) NOT NULL COMMENT '操作人用户ID',
  `name` varchar(32) DEFAULT NULL COMMENT '操作人姓名',
  `openid` varchar(32) DEFAULT NULL COMMENT '操作人openID',
  `content` varchar(30) NOT NULL COMMENT '操作内容',
  `table_name` varchar(30) NOT NULL COMMENT '操作表名称',
  `create_user` varchar(32) DEFAULT 'root' COMMENT '作成者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成时间',
  `update_user` varchar(32) DEFAULT 'root' COMMENT '修改者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int(5) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作日志表';