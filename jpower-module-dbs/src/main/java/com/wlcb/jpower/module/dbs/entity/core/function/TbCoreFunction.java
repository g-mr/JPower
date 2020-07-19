package com.wlcb.jpower.module.dbs.entity.core.function;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreUser
 * @Description TODO 菜单信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreFunction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7093626905745914312L;

    private String functionName;
    private String alias;
    private String parentId;
    private String code;
    private String parentCode;
    private String url;
    @Dict(name = "YN01",attributes = "isMenuStr")
    private Integer isMenu;
    private String icon;
    private Integer sort;
    private String remark;
    private String moudeSummary;
    private String operateInstruction;
    private Integer functionLevel;

    @TableField(exist = false)
    private String isMenuStr;
}
