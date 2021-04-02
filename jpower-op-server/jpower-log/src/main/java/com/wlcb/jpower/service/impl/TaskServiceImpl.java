package com.wlcb.jpower.service.impl;

import com.wlcb.jpower.feign.DynamicFeignClient;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.service.TaskService;
import feign.Feign;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

/**
 * @author mr.g
 * @date 2021-04-02 11:27
 */
@Service
@AllArgsConstructor
//@Import(FeignClientsConfiguration.class)
public class TaskServiceImpl implements TaskService {

    private DynamicFeignClient feignClient;

//    @Autowired
//    public TaskServiceImpl(Decoder decoder, Encoder encoder) {
//        feignClient = Feign.builder()
//                //.client(client)
//                .encoder(encoder)
//                .decoder(decoder)
//                .target(Target.EmptyTarget.create(DynamicFeignClient.class));
//    }

    @Override
    @SneakyThrows
    public void process(MonitorRestfulProperties.Routes route) {



        FeignClient client = DynamicFeignClient.class.getAnnotation(FeignClient.class);
        InvocationHandler h = Proxy.getInvocationHandler(client);
        Field hField = h.getClass().getDeclaredField("memberValues");
        hField.setAccessible(true);
        Map memberValues = (Map) hField.get(h);
        memberValues.put("value", route.getUrl());


        Method method = DynamicFeignClient.class.getMethod("get",Map.class);
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        h = Proxy.getInvocationHandler(getMapping);
        hField = h.getClass().getDeclaredField("memberValues");
        hField.setAccessible(true);
        memberValues = (Map) hField.get(h);
        memberValues.put("value", route.getLocation());

        Object o = feignClient.get(ChainMap.newMap());
        System.out.println(o);
//        if (!StringUtil.startsWithIgnoreCase(route.getUrl(),"http")){
//            uri = "http://"+route.getUrl();
//        }
//
//        Object o = feignClient.get(URI.create(uri+route.getLocation()), ChainMap.newMap());
//        System.out.println(o);
    }
}
