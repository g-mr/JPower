package com.wlcb.jpower.module.dbs.entity.core.params;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreParam
 * @Description TODO 系统参数
 * @Author 郭丁志
 * @Date 2020-05-06 16:04
 * @Version 1.0
 */
@Data
public class TbCoreParam extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3631941874174942414L;

    private String code;
    private String name;
    private String value;
    private String note;
}
