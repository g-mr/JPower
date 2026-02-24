package top.jpower.system.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;

import java.util.Map;

import static top.jpower.core.auth.utils.constant.RoleConstant.ANONYMOUS_ID;
import static top.jpower.core.auth.utils.constant.RoleConstant.ROOT_ID;

/**
 * 系统设置控制器
 * 
 * @author mr.g
 */
@Api(tags = "系统设置")
@RestController
@RequestMapping("/core/system")
@RequiredArgsConstructor
public class SystemController extends BaseController {

    private final JpowerTenantProperties tenantProperties;

    @ApiOperation("前端配置")
    @GetMapping(value = "/configure" , produces="application/json")
    public ResponseData<Map<String,Object>> configure(){
        return ReturnJsonUtil.ok("获取成功", ChainMap.create().put("isTenant", tenantProperties.getEnable()).put("anonymousRoleId", ANONYMOUS_ID).put("rootRoleId", ROOT_ID).put("menuCodeHeader", JpowerConstants.HEADER_MENU).put("tenantCodeHeader", JpowerConstants.HEADER_TENANT).build());
    }
}
