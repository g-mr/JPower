package top.jpower.jpower.dbs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.jpower.module.base.annotation.Dict;
import top.jpower.jpower.module.dbs.entity.base.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * 短信
 *
 * @Author mr.g
 * @Date 2020-07-13 17:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TbResourceSms extends BaseEntity {

    private static final long serialVersionUID = 4002497583099907671L;

    @ApiModelProperty("分类 字典：SMS_CATEGORY")
    @NotBlank(message = "分类 不能为空")
    @Dict(name = "SMS_CATEGORY")
    private String category;
    @ApiModelProperty("编号")
    @NotBlank(message = "编码 不能为空")
    private String code;
    @ApiModelProperty("模板ID")
    private String template;
    @ApiModelProperty("accessKey")
    private String accessKey;
    @ApiModelProperty("secretKey")
    private String secretKey;
    @ApiModelProperty("短信签名")
    private String sign;
    @ApiModelProperty("区域ID")
    private String regionId;
    @ApiModelProperty("发送参数")
    private String parameters;
}
