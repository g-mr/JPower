package top.jpower.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 登录人信息
 *
 * @author mr.g
 */
@Data
public class LoginUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


	@Schema(description = "用户ID")
	private Long userId;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "昵称")
    @NotBlank(message = "昵称不能为空")
    private String realName;
    @Schema(description = "用户姓名")
    private String username;
	@Schema(description = "角色")
//	@RelationOneToMany(
//			selfValueSplitBy = ",", //使用 "," 对 diseaseIds 的值进行分割
//			valueField = "name" //测试只获取某个字段值是否正常
//	)
	private List<Long> roles;
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
