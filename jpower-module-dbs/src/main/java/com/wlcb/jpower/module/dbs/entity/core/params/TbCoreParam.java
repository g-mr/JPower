package com.wlcb.jpower.module.dbs.entity.core.params;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
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
    /** 是否支持立即生效，0否 1是 **/
    @Dict(name = "YN01", attributes = "isEffectStr")
    private Integer isEffect;
    private String note;

    @TableField(exist = false)
    private String isEffectStr;
}
