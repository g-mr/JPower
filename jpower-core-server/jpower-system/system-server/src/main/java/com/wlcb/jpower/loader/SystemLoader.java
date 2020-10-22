package com.wlcb.jpower.loader;

import com.wlcb.jpower.dbs.entity.dict.TbCoreDictType;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.dict.CoreDictTypeService;
import com.wlcb.jpower.service.params.CoreParamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName CommandLineRunnerImpl
 * @Description TODO 启动执行类
 * @Author 郭丁志
 * @Date 2020-05-18 11:02
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class SystemLoader implements CommandLineRunner {

    private CoreParamService paramService;
    private CoreDictTypeService dictTypeService;
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        log.warn("系统参数初始化...");
        paramService.effectAll();
        log.warn("系统参数加载完成...");

        log.warn("字典初始化...");
        List<Object> list = dictTypeService.listObjs(Condition.<TbCoreDictType>getQueryWrapper().lambda()
                .select(TbCoreDictType::getDictTypeCode));
        redisUtil.removeMembers(CacheNames.DICT_REDIS_CODE_LIST,list.toArray());
        redisUtil.add(CacheNames.DICT_REDIS_CODE_LIST,list.toArray());
        log.warn("字典加载完成...");
    }

}