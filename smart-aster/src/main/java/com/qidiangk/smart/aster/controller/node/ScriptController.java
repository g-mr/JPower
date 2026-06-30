package com.qidiangk.smart.aster.controller.node;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.qidiangk.smart.aster.lint.groovy.GroovyScriptValidator;
import com.qidiangk.smart.aster.lint.javascript.JavaScriptValidator;
import com.qidiangk.smart.aster.pojo.node.LintBO;
import com.qidiangk.smart.aster.pojo.vo.ScriptIssueVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "脚本")
@Validated
@RestController
@RequestMapping("/script")
public class ScriptController extends BaseController {

    @PostMapping("/lint")
    @Operation(summary = "脚本校验")
    public R<List<ScriptIssueVO>> pylint(@Valid @RequestBody LintBO lintReq) {

        if (Fc.isBlank(lintReq.getCode())){
            return R.data(new ArrayList<>());
        }

        Map<String, Object> params = new HashMap<>();
        lintReq.getParams().forEach((key, value) -> {
            try {
                if (StrUtil.startWith(value, "@class")){
                    params.put(key, ReflectUtil.newInstance(StrUtil.trim(StrUtil.removePrefix(value, "@class"))));
                } else {
                    params.put(key, value);
                }
            } catch (Exception e) {
                log.warn("入参构造失败,已忽略===>{}", e.getMessage());
            }
        });

        if (StrUtil.equalsAnyIgnoreCase(lintReq.getCodeType(), "js", "javascript")){
            List<ScriptIssueVO> result = JavaScriptValidator.validateScript(lintReq.getCode(), params);
            return R.data(result);
        } else if (StrUtil.equalsIgnoreCase(lintReq.getCodeType(), "groovy")){
            List<ScriptIssueVO> result = GroovyScriptValidator.validateScript(lintReq.getCode(), params);
            return R.data(result);
        } else {
            return R.fail("暂不支持该类型脚本");
        }
    }

}
