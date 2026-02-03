package top.jpower.user.dbs.entity;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.common.validated.Mobile;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;
import top.jpower.core.util.support.excel.Excel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import static top.jpower.common.constants.ServiceCodeConstants.EMAIL_NOT_LEGAL;

/**
 * 用户信息
 *
 * @author mr.g
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Table("tb_core_user")
public class CoreUser extends TenantEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8829495593714085987L;

    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "登录用户名")
    @Excel(name = "登录用户名")
    @NotBlank(message = "用户名不可为空")
    private String loginId;
    @Schema(description = "密码",hidden = true)
    @Column(isLarge=true)
    private String password;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "昵称")
    @Excel(name = "昵称")
    private String nickName;
    @Schema(description = "用户姓名")
    @Excel(name = "用户姓名")
    private String userName;
    @Schema(description = "证件类型 字典ID_TYPE")
    @Excel(name = "证件类型",readConverterExp = "1=身份证,2=中国护照,3=台胞证,4=外国护照,5=外国人永居证",combo={"身份证","中国护照","台胞证","外国护照","外国人永居证"})
    @Dict(name = "ID_TYPE")
    private Integer idType;
    @Schema(description = "证件号码")
    @Excel(name = "证件号码")
    private String idNo;
    @Schema(description = "用户类型 字典USER_TYPE")
    @Excel(name ="用户类型",readConverterExp = "0=系统用户,1=普通用户,2=单位用户,3=会员,9=匿名用户",combo={"系统用户,","普通用户","单位用户","会员"})
    @Dict(name = "USER_TYPE")
    private Integer userType;
    @Schema(description = "出生日期")
    @Excel(name ="出生日期")
    @JSONField(format="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATE_PATTERN,locale = "zh_CN")
    private Date birthday;
    @Schema(description = "邮箱")
    @Excel(name ="邮箱")
    @Nullable
    @Email(message = EMAIL_NOT_LEGAL)
    private String email;
    @Schema(description = "电话")
    @Excel(name ="电话")
    @Mobile
    private String telephone;
    @Schema(description = "地址")
    @Excel(name ="地址")
    private String address;
    @Schema(description = "邮编")
    @Excel(name ="邮编")
    private String postCode;
    @Schema(description = "第三方平台标识")
    private String otherCode;
    @Schema(description = "最后登录日期")
    @Excel(name ="最后登录日期",dateFormat = "yyyy-MM-dd HH:mm:ss",type = Excel.Type.EXPORT)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    private Date lastLoginTime;
    @Schema(description = "登录次数")
    @Excel(name ="登录次数",type = Excel.Type.EXPORT)
    private Integer loginCount;
    @Schema(description = "是否激活 字典YN01")
    @Excel(name ="是否激活",readConverterExp = "1=是,0=否",combo={"是,","否"})
    @Dict(name = "YN01")
    private Integer activationStatus;
    @Schema(description = "激活码")
    private String activationCode;
    @Schema(description = "部门主键")
    @Excel(name = "部门ID",type = Excel.Type.IMPORT)
    private Long orgId;
    @Schema(description = "岗位ID")
    private Long postId;

}
