package com.wlcb.jpower.dbs.entity;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.base.annotation.Excel;
import com.wlcb.jpower.module.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName TbCoreUser
 * @Description TODO 登录用户信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreUser extends TenantEntity implements Serializable {

    private static final long serialVersionUID = 8829495593714085987L;

    @ApiModelProperty("登录用户名")
    @Excel(name = "登录用户名")
    private String loginId;
    @ApiModelProperty(name = "密码",hidden = true)
    private String password;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("昵称")
    @Excel(name = "昵称")
    private String nickName;
    @ApiModelProperty("用户姓名")
    @Excel(name = "用户姓名")
    private String userName;
    @ApiModelProperty("证件类型 字典ID_TYPE")
    @Excel(name = "证件类型",readConverterExp = "1=身份证,2=中国护照,3=台胞证,4=外国护照,5=外国人永居证",combo={"身份证","中国护照","台胞证","外国护照","外国人永居证"})
    @Dict(name = "ID_TYPE",attributes = "idTypeStr")
    private Integer idType;
    @ApiModelProperty("证件号码")
    @Excel(name = "证件号码")
    private String idNo;
    @ApiModelProperty("用户类型 字典USER_TYPE")
    @Excel(name ="用户类型",readConverterExp = "0=系统用户,1=普通用户,2=单位用户,3=会员,9=匿名用户",combo={"系统用户,","普通用户","单位用户","会员"})
    @Dict(name = "USER_TYPE",attributes = "userTypeStr")
    private Integer userType;
    @ApiModelProperty("出生日期")
    @Excel(name ="出生日期")
    @JSONField(format="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATE_PATTERN,locale = "zh_CN")
    private Date birthday;
    @ApiModelProperty("邮箱")
    @Excel(name ="邮箱")
    private String email;
    @ApiModelProperty("电话")
    @Excel(name ="电话")
    private String telephone;
    @ApiModelProperty("地址")
    @Excel(name ="地址")
    private String address;
    @ApiModelProperty("邮编")
    @Excel(name ="邮编")
    private String postCode;
    @ApiModelProperty("第三方平台标识")
    private String otherCode;
    @ApiModelProperty("最后登录日期")
    @Excel(name ="最后登录日期",dateFormat = "yyyy-MM-dd HH:mm:ss",type = Excel.Type.EXPORT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    private Date lastLoginTime;
    @ApiModelProperty("登录次数")
    @Excel(name ="登录次数",type = Excel.Type.EXPORT)
    private Integer loginCount;
    @ApiModelProperty("是否激活 字典YN01")
    @Excel(name ="是否激活",readConverterExp = "1=是,0=否",combo={"是,","否"})
    @Dict(name = "YN01",attributes = "activationStatusStr")
    private Integer activationStatus;
    @ApiModelProperty("激活码")
    private String activationCode;
    @ApiModelProperty("部门主键")
    @Excel(name = "部门ID",type = Excel.Type.IMPORT)
    private String orgId;

    @ApiModelProperty("角色ID，多个逗号分割")
    @TableField(exist = false)
    private String roleIds;

    @TableField(exist = false)
    private String activationStatusStr;
    @TableField(exist = false)
    private String userTypeStr;
    @TableField(exist = false)
    private String idTypeStr;

}
