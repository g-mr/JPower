package com.qidiangk.smart.system.vo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import com.qidiangk.smart.common.validated.group.Validation;
import com.qidiangk.smart.system.dbs.entity.tenant.CoreTenant;

import java.util.Set;

/**
 * 租户新增信息
 *
 * @author mr.g
 */
@Data
public class TenantCreateVO extends CoreTenant {

	@NotEmpty(message = "功能CODE不能为空", groups = Validation.Create.class)
	private Set<String> functionCode;

}
