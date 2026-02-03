package top.jpower.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 登录人信息
 *
 * @author mr.g
 */
@Data
public class LoginUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "昵称")
    @NotBlank(message = "昵称不能为空")
    private String nickName;
    @Schema(description = "用户姓名")
    private String userName;
    @Schema(description = "身份证号")
    private String idNo;
    @Schema(description = "邮政编码")
    private String postCode;
    @Schema(description = "地址")
    private String address;
    @Schema(description = "身份证类型")
    private Integer idType;
    @Schema(description = "出生日期")
    private Date birthday;
}
