package com.qidiangk.smart.maxkb.controller;

import cn.hutool.core.util.StrUtil;
import com.qidiangk.smart.maxkb.config.property.MaxKBProperty;
import com.qidiangk.smart.maxkb.service.ITokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.ChainMap;

import java.util.Map;

/**
 * @author mr.g
 */
@Tag(name = "Token")
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController extends BaseController {

    private final ITokenService tokenService;
    private final MaxKBProperty maxKBProperty;

    @GetMapping
    @Operation(summary = "获取Token")
    public R<Map<String, String>> token() {
        return R.data(ChainMap.<String, String>create()
                .put("baseUrl", StrUtil.removeAllSuffix(StrUtil.blankToDefault(maxKBProperty.getExternalBaseUrl(), maxKBProperty.getBaseUrl()), "/"))
                .put("token", tokenService.getToken(true))
                .build());
    }

}
