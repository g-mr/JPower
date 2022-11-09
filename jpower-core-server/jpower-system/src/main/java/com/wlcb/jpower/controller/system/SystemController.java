package com.wlcb.jpower.controller.system;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.tenant.JpowerTenantProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.wlcb.jpower.module.common.auth.RoleConstant.ANONYMOUS_ID;
import static com.wlcb.jpower.module.common.auth.RoleConstant.ROOT_ID;

/**
 * @author mr.g
 * @date 2022-11-09 16:37
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
        return ReturnJsonUtil.ok("获取成功", ChainMap.create().put("isTenant", tenantProperties.getEnable()).put("anonymousRoleId", ANONYMOUS_ID).put("rootRoleId", ROOT_ID).put("menuCodeHeader", TokenConstant.HEADER_MENU).put("tenantCodeHeader", TokenConstant.HEADER_TENANT).build());
    }
}
