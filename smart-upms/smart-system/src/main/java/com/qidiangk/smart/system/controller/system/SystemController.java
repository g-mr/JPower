package com.qidiangk.smart.system.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qidiangk.smart.common.constants.ParamsConstants;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.config.properties.DemoProperties;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.ChainMap;
import com.qidiangk.smart.system.api.cache.param.ParamCache;

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
    private final DemoProperties demoProperties;

    @Operation(summary = "前端配置")
    @GetMapping(value = "/configure" , produces = APPLICATION_JSON_VALUE)
    public R<Map<String,Object>> configure(){
        return R.data(ChainMap.<String,Object>create()
				.put("anonymousRoleId", ANONYMOUS_ID)
				.put("rootRoleId", ROOT_ID)
				.put("menuCodeHeader", JpowerConstants.HEADER_MENU)
				.put("tenantCodeHeader", JpowerConstants.HEADER_TENANT)
                .put("languageCodeHeader", JpowerConstants.I18N_KEY)
                .put("enableDemo", demoProperties.isEnable())
                .put("enableTenant", tenantProperties.getEnable())
                .put("enableRegister", ParamCache.getBoolean(ParamsConstants.IS_REGISTER,Boolean.FALSE))
                .put("enableForgetPassword", ParamCache.getBoolean(ParamsConstants.IS_FORGET_PASSWORD,Boolean.FALSE))
				.build());
    }

}
