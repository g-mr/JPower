package com.wlcb.jpower.module.dbs.entity.core.dict;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

/**
 * @ClassName TbCoreDict
 * @Description TODO 字典
 * @Author 郭丁志
 * @Date 2020-07-13 15:47
 * @Version 1.0
 */
@Data
public class TbCoreDict extends BaseEntity {
    private static final long serialVersionUID = 2963005021265897302L;

    private String dictTypeCode;
    private String code;
    private String name;
    @Dict(name = "YYZL",attributes = "localeStr")
    private String localeId;
    private String note;
    private Integer sortNum;
    private String dictTypeId;
    private String pcode;
    private Integer dictLevel;

    @TableField(exist = false)
    private String localeStr;
}
