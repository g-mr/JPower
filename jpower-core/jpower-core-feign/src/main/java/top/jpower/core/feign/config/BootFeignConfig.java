package top.jpower.core.feign.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * BOOT模式Feign本地调用配置
 * <p>
 * 在BOOT(单体)模式下，将Feign Client的JDK代理替换为本地直接调用代理，
 * 使Feign方法调用直接委托给本地实现类（Controller），
 * 避免通过HTTP请求转发到自身，提升性能并简化调用链路。
 * </p>
 *
 * @author mr.g
 */
@AutoConfiguration
@ConditionalOnProperty(name = "jpower.server", havingValue = "BOOT")
@Slf4j
public class BootFeignConfig implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> feignInterface = findFeignClientInterface(bean);
        if (feignInterface != null) {
            log.info("BOOT模式: Feign客户端[{}]将使用本地直接调用", feignInterface.getSimpleName());
            return createLocalProxy(feignInterface, beanName);
        }
        return bean;
    }

    /**
     * 检测bean是否为Feign生成的JDK代理，返回对应的@FeignClient接口
     */
    private Class<?> findFeignClientInterface(Object bean) {
        // 仅处理JDK动态代理（Feign生成的代理均为JDK Proxy）
        if (!Proxy.isProxyClass(bean.getClass())) {
            return null;
        }
        for (Class<?> iface : bean.getClass().getInterfaces()) {
            if (iface.isAnnotationPresent(FeignClient.class)) {
                return iface;
            }
        }
        return null;
    }

    /**
     * 创建本地调用代理，懒加载解析本地实现Bean
     */
    @SuppressWarnings("unchecked")
    private <T> T createLocalProxy(Class<T> feignInterface, String feignBeanName) {
        return (T) Proxy.newProxyInstance(
                feignInterface.getClassLoader(),
                new Class[]{feignInterface},
                new LocalFeignInvocationHandler(applicationContext, feignInterface, feignBeanName)
        );
    }

    /**
     * 本地Feign调用处理器
     * <p>
     * 将Feign接口的方法调用直接委托给Spring容器中的本地实现Bean（通常是Controller），
     * 跳过HTTP序列化/反序列化和网络请求，实现零开销的本地方法调用。
     * </p>
     */
    private static class LocalFeignInvocationHandler implements InvocationHandler {

        private final ApplicationContext applicationContext;
        private final Class<?> feignInterface;
        private final String feignBeanName;
        private volatile Object localImpl;

        LocalFeignInvocationHandler(ApplicationContext applicationContext,
                                    Class<?> feignInterface, String feignBeanName) {
            this.applicationContext = applicationContext;
            this.feignInterface = feignInterface;
            this.feignBeanName = feignBeanName;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Object类方法直接处理
            if (method.getDeclaringClass() == Object.class) {
                return switch (method.getName()) {
                    case "toString" -> "BootLocalProxy[" + feignInterface.getSimpleName() + "]";
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> method.invoke(this, args);
                };
            }

            Object target = getLocalImpl();
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

        private Object getLocalImpl() {
            if (localImpl == null) {
                synchronized (this) {
                    if (localImpl == null) {
                        localImpl = resolveLocalImpl();
                    }
                }
            }
            return localImpl;
        }

        /**
         * 解析本地实现Bean，优先查找@RestController注解的实现类
         */
        private Object resolveLocalImpl() {
            String[] beanNames = applicationContext.getBeanNamesForType(feignInterface);

            // 优先查找带@RestController注解的实现（即Controller层实现）
            for (String name : beanNames) {
                if (name.equals(feignBeanName)) {
                    continue;
                }
                Class<?> type = applicationContext.getType(name);
                if (type != null && AnnotationUtils.findAnnotation(type, RestController.class) != null) {
                    log.debug("BOOT模式: Feign客户端[{}]解析到本地实现: {}",
                            feignInterface.getSimpleName(), type.getSimpleName());
                    return applicationContext.getBean(name);
                }
            }

            // 兜底：返回第一个非Feign代理的实现
            for (String name : beanNames) {
                if (!name.equals(feignBeanName)) {
                    return applicationContext.getBean(name);
                }
            }

            throw new IllegalStateException(
                    "BOOT模式: 未找到Feign客户端[" + feignInterface.getName() + "]的本地实现类");
        }
    }
}
