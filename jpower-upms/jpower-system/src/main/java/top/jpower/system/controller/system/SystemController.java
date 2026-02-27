package top.jpower.system.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.ChainMap;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static top.jpower.core.auth.utils.constant.RoleConstant.ANONYMOUS_ID;
import static top.jpower.core.auth.utils.constant.RoleConstant.ROOT_ID;

/**
 * 系统设置控制器
 * 
 * @author mr.g
 */
@Tag(name = "系统设置")
@RestController
@RequestMapping("/core/system")
@RequiredArgsConstructor
public class SystemController extends BaseController {

    private final JpowerTenantProperties tenantProperties;

    @Operation(summary = "前端配置")
    @GetMapping(value = "/configure" , produces = APPLICATION_JSON_VALUE)
    public R<Map<String,Object>> configure(){
        return R.data(ChainMap.<String,Object>create().put("isTenant", tenantProperties.getEnable())
				.put("anonymousRoleId", ANONYMOUS_ID)
				.put("rootRoleId", ROOT_ID)
				.put("menuCodeHeader", JpowerConstants.HEADER_MENU)
				.put("tenantCodeHeader", JpowerConstants.HEADER_TENANT)
				.build());
    }
}
