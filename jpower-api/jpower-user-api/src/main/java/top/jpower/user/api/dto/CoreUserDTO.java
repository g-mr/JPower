package top.jpower.user.api.dto;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户信息DTO
 *
 * @author mr.g
 */
@Data
public class CoreUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "登录用户名")
    private String loginId;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "用户姓名")
    private String userName;
    @Schema(description = "证件类型 字典ID_TYPE")
    private Integer idType;
    @Schema(description = "证件号码")
    private String idNo;
    @Schema(description = "出生日期")
    @JSONField(format="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATE_PATTERN,locale = "zh_CN")
    private Date birthday;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "电话")
    private String telephone;
    @Schema(description = "地址")
    private String address;
    @Schema(description = "邮编")
    private String postCode;
    @Schema(description = "部门ID")
    private Long orgId;
    @Schema(description = "岗位ID")
    private Long postId;
    @Schema(description = "角色ID")
    private List<Long> roleIds;

}
