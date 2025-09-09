package top.jpower.core.feign.config.client;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalControllerInvocationHandler implements InvocationHandler {
    private final ApplicationContext applicationContext;
    private final Class<?> feignClientInterface;
    private final Map<Method, Method> methodCache = new ConcurrentHashMap<>();

    public LocalControllerInvocationHandler(ApplicationContext applicationContext,
                                            Class<?> feignClientInterface) {
        this.applicationContext = applicationContext;
        this.feignClientInterface = feignClientInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 跳过Object类的方法
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        // 查找对应的Controller方法
        Method controllerMethod = findControllerMethod(method);

        // 查找Controller Bean
        Object controller = findController(controllerMethod);

        // 调用Controller方法
        return controllerMethod.invoke(controller, args);
    }

    /**
     * 查找对应的Controller方法
     */
    private Method findControllerMethod(Method feignMethod) {
        return methodCache.computeIfAbsent(feignMethod, method -> {
            // 获取所有Controller Bean
            Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);

            for (Object controller : controllers.values()) {
                // 查找匹配的方法
                for (Method controllerMethod : controller.getClass().getMethods()) {
                    if (isMethodMatch(method, controllerMethod)) {
                        return controllerMethod;
                    }
                }
            }

            throw new RuntimeException("No matching controller method found for: " + method.getName());
        });
    }

    /**
     * 判断Feign方法是否与Controller方法匹配
     */
    private boolean isMethodMatch(Method feignMethod, Method controllerMethod) {
        // 方法名相同
        if (!feignMethod.getName().equals(controllerMethod.getName())) {
            return false;
        }

        // 参数类型和数量相同
        if (!Arrays.equals(feignMethod.getParameterTypes(), controllerMethod.getParameterTypes())) {
            return false;
        }

        // 检查注解匹配（简化实现）
        // 实际应根据@RequestMapping等注解的路径、方法类型等进行匹配

        return true;
    }

    /**
     * 查找Controller Bean
     */
    private Object findController(Method controllerMethod) {
        // 获取所有Controller Bean
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);

        for (Object controller : controllers.values()) {
            try {
                // 检查是否有匹配的方法
                controller.getClass().getMethod(
                        controllerMethod.getName(),
                        controllerMethod.getParameterTypes()
                );
                return controller;
            } catch (NoSuchMethodException e) {
                // 继续查找
            }
        }

        throw new RuntimeException("No controller found for method: " + controllerMethod.getName());
    }
}