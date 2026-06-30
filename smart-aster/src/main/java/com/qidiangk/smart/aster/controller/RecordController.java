package com.qidiangk.smart.aster.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.utils.FileUtil;

import java.io.File;

@Tag(name = "录音接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController extends BaseController {

    @SneakyThrows
    @Operation(summary = "录音下载")
    @GetMapping("download")
    public void download(@Parameter(description="文件路径") @RequestParam("path") String path, HttpServletResponse response) {
        File file = new File(path);
        JpowerAssert.isTrue(file.exists(), JpowerError.NotFind, "文件不存在");

        // 文件下载
        FileUtil.download(file, response,file.getName());
    }

}
