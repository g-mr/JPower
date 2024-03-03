package top.jpower.jpower.dbs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.jpower.module.base.annotation.Dict;
import top.jpower.jpower.module.dbs.entity.base.BaseEntity;

/**
 * 短信
 *
 * @Author mr.g
 * @Date 2020-07-13 17:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TbResourceSms extends BaseEntity {

    private static final long serialVersionUID = -6889020017108014146L;

    @ApiModelProperty("分类 字典：SMS_CATEGORY")
    @Dict(name = "SMS_CATEGORY")
    private String category;
    @ApiModelProperty("编号")
    private String code;
    @ApiModelProperty("模板ID")
    private String template;
    @ApiModelProperty("accessKey")
    private String accessKey;
    @ApiModelProperty("secretKey")
    private String secretKey;
    @ApiModelProperty("短信签名")
    private String sign;
    @ApiModelProperty("发送参数")
    private String params;
}
