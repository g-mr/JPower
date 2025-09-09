package top.jpower.core.feign.config.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Map;

@RequiredArgsConstructor
public class FeignClientProxyFactory {

    private final ApplicationContext applicationContext;

    /**
     * 创建Feign客户端代理
     */
    public <T> T createFeignProxy(Class<T> feignClientInterface, String serviceName) {
        return createLocalControllerProxy(feignClientInterface);
    }

    /**
     * 创建指向本地Controller的代理
     */
    private <T> T createLocalControllerProxy(Class<T> feignClientInterface) {
        // 查找实现了Feign客户端接口的本地Controller
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);

        for (Object controller : controllers.values()) {
            if (feignClientInterface.isInstance(controller)) {
                @SuppressWarnings("unchecked")
                T proxy = (T) controller;
                return proxy;
            }
        }

        // 如果没有找到直接实现，使用动态代理将调用路由到匹配的Controller方法
        return createDynamicLocalProxy(feignClientInterface);
    }

    /**
     * 创建动态代理，将Feign调用路由到本地Controller
     */
    @SuppressWarnings("unchecked")
    private <T> T createDynamicLocalProxy(Class<T> feignClientInterface) {
        return (T) Proxy.newProxyInstance(
                feignClientInterface.getClassLoader(),
                new Class<?>[] {feignClientInterface},
                new LocalControllerInvocationHandler(applicationContext, feignClientInterface)
        );
    }

}
