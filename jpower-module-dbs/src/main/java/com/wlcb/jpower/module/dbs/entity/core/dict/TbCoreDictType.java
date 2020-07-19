package com.wlcb.jpower.module.dbs.entity.core.dict;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

/**
 * @ClassName TbCoreDictType
 * @Description TODO 字典类型表
 * @Author 郭丁志
 * @Date 2020-07-13 15:42
 * @Version 1.0
 */
@Data
public class TbCoreDictType extends BaseEntity {

    private static final long serialVersionUID = 2104502370643051282L;

    private String dictTypeCode;
    private String dictTypeName;
    private String localeCode;
    private String note;
    @Dict(name = "YN",attributes = "delEnabledStr")
    private String delEnabled;
    private Integer sortNum;
    private String dictTypePcode;

    @TableField(exist = false)
    private String delEnabledStr;
}
