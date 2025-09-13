package top.jpower.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.jpower.feign.UserClient;
import top.jpower.jpower.vo.UserVo;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserClient userClient;

    @GetMapping("/test")
    public ResponseData<UserVo> test() {
        return userClient.get(1L);
    }

}
