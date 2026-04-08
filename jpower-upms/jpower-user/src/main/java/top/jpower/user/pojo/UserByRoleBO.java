package top.jpower.user.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户角色业务对象
 *
 * @author mr.g
 */
@Data
public class UserByRoleBO implements java.io.Serializable {

	@Schema(description = "角色ID等于")
	private Long roleIdEq;
	@Schema(description = "角色ID不等于")
	private Long roleIdNe;
	@Schema(description = "组织ID")
	private Long orgId;
	@Schema(description = "登录用户名")
	private String loginId;
	@Schema(description = "昵称")
	private String nickName;
	@Schema(description = "用户名称")
	private String userName;
	@Schema(description = "证件号")
	private String idNo;
	@Schema(description = "用户类型")
	private Integer userType;
	@Schema(description = "手机号")
	private String telephone;

}
