package top.jpower.boot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.redis.cache.RedisService;
@RestController
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class TestController {
    private RedisService redisService;
    @GetMapping("test")
    public void test(){
        redisService.delete("xxx");
    }

}
