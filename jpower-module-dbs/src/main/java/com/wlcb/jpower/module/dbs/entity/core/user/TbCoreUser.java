package com.wlcb.jpower.module.dbs.entity.core.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.base.annotation.Excel;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
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
public class TbCoreUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8829495593714085987L;

    @Excel(name = "部门ID",isExport = false)
    private String orgId;
    @Excel(name = "登录用户名")
    private String loginId;
    @Excel(name = "密码",isExport = false)
    private String password;
    @Excel(name = "昵称")
    private String nickName;
    @Excel(name = "用户姓名")
    private String userName;
    @Excel(name = "证件类型",readConverterExp = "1=身份证,2=中国护照,3=台胞证,4=外国护照,5=外国人永居证",combo={"身份证","中国护照","台胞证","外国护照","外国人永居证"})
    @Dict(name = "ID_TYPE",attributes = "idTypeStr")
    private Integer idType;
    @Excel(name = "证件号码")
    private String idNo;
    @Excel(name ="用户类型",readConverterExp = "0=系统用户,1=普通用户,2=单位用户,3=会员,9=匿名用户",combo={"系统用户,","普通用户","单位用户","会员"})
    @Dict(name = "USER_TYPE",attributes = "userTypeStr")
    private Integer userType;
    @Excel(name ="出生日期")
    @JSONField(format="yyyy-MM-dd")
    private Date birthday;
    @Excel(name ="邮箱")
    private String email;
    @Excel(name ="电话")
    private String telephone;
    @Excel(name ="地址")
    private String address;
    @Excel(name ="邮编")
    private String postCode;
    @Excel(name ="第三方平台标识")
    private String otherCode;
    @Excel(name ="最后登录日期",dateFormat = "yyyy-MM-dd HH:mm:ss",type = Excel.Type.EXPORT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    @Excel(name ="登录次数",type = Excel.Type.EXPORT)
    private Integer loginCount;
    @Excel(name ="是否激活",readConverterExp = "1=是,0=否",combo={"是,","否"})
    @Dict(name = "YN01",attributes = "activationStatusStr")
    private Integer activationStatus;
    @Excel(name ="激活码",isExport = false)
    private String activationCode;

    @TableField(exist = false)
    @Excel(name = "部门名称")
    private String orgName;

    @TableField(exist = false)
    private String activationStatusStr;
    @TableField(exist = false)
    private String userTypeStr;
    @TableField(exist = false)
    private String idTypeStr;

}
