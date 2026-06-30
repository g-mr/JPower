package com.qidiangk.smart.aster.controller.node;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.qidiangk.smart.aster.pojo.node.HttpBO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.JsonUtil;

import java.util.Map;

/**
 * @author mr.g
 */
@Slf4j
@Tag(name = "HTTP")
@Validated
@RestController
@RequestMapping("/http")
public class HttpController extends BaseController {

    @PostMapping("/test")
    @Operation(summary = "接口请求校验")
    public R<String> test(@Valid @RequestBody HttpBO httpBO) {
        HttpRequest request = HttpRequest.of(httpBO.getUrl()).method(Method.valueOf(StrUtil.toUpperCase(httpBO.getMethod()))).headerMap(httpBO.getHeaders(), true);

        if (StrUtil.equalsAnyIgnoreCase(httpBO.getMethod(), Method.PUT.name(), Method.POST.name())) {
            request.body(httpBO.getParameters());
        } else {
            Map<String, Object> params = JsonUtil.toMap(httpBO.getParameters());
            request.form(params);
        }

        String responseBody = request.execute().body();
        return R.data(JsonUtil.isJsonValid(responseBody) ? JSONUtil.toJsonPrettyStr(responseBody) : responseBody);

    }

}
