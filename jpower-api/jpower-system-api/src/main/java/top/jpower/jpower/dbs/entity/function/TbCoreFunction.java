package top.jpower.jpower.dbs.entity.function;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

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

    @ApiModelProperty("客户端ID")
    private Long clientId;
    @ApiModelProperty("功能名称")
    private String functionName;
    @ApiModelProperty("别名")
    private String alias;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("父级ID")
    private Long parentId;
    @ApiModelProperty("祖级ID")
    private String ancestorId;
    @ApiModelProperty("地址")
    private String url;
    @ApiModelProperty("功能类型 字典：FUNCTION_TYPE")
    @Dict(name = "FUNCTION_TYPE")
    private Integer functionType;
    @ApiModelProperty("打开方式 字典DKFS")
    @Dict(name = "DKFS",attributes = "targetStr")
    private String target;
    @ApiModelProperty("是否隐藏")
    private Boolean isHide;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("排序")
    @OrderBy(asc = true)
    private Integer sort;
    @ApiModelProperty("说明")
    private String remark;
    @ApiModelProperty("模块概述")
    private String moudeSummary;
    @ApiModelProperty("操作说明")
    private String operateInstruction;

    @TableField(exist = false)
    private String targetStr;

}
