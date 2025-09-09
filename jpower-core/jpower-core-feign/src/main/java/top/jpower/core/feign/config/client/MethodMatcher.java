//package top.jpower.core.feign.config.client;
//
//import feign.RequestLine;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.util.Arrays;
//
//public class MethodMatcher {
//
//    /**
//     * 更精确的方法匹配实现
//     */
//    public static boolean isMethodMatch(Method feignMethod, Method controllerMethod,
//                                        Class<?> feignClientInterface) {
//        // 方法名相同
//        if (!feignMethod.getName().equals(controllerMethod.getName())) {
//            return false;
//        }
//
//        // 参数类型和数量相同
//        if (!Arrays.equals(feignMethod.getParameterTypes(), controllerMethod.getParameterTypes())) {
//            return false;
//        }
//
//        // 检查Feign方法注解与Controller方法注解的匹配
//        if (!isRequestMappingMatch(feignMethod, controllerMethod, feignClientInterface)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * 检查RequestMapping注解是否匹配
//     */
//    private static boolean isRequestMappingMatch(Method feignMethod, Method controllerMethod,
//                                                 Class<?> feignClientInterface) {
//        // 获取Feign方法上的RequestMapping信息
//        RequestMapping feignRequestMapping = getFeignRequestMapping(feignMethod, feignClientInterface);
//
//        // 获取Controller方法上的RequestMapping
//        RequestMapping controllerRequestMapping = controllerMethod.getAnnotation(RequestMapping.class);
//        if (controllerRequestMapping == null) {
//            // 查找组合注解如@GetMapping, @PostMapping等
//            for (Annotation annotation : controllerMethod.getAnnotations()) {
//                if (annotation.annotationType().getName().contains("Mapping")) {
//                    // 简化处理，实际应解析组合注解
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        // 比较路径和方法类型
//        return isRequestMappingEquals(feignRequestMapping, controllerRequestMapping);
//    }
//
//    /**
//     * 获取Feign方法上的RequestMapping等效信息
//     */
//    private static RequestMapping getFeignRequestMapping(Method method, Class<?> feignClientInterface) {
//        // 实际应根据Feign客户端接口上的注解解析
//        // 这里简化实现，返回一个模拟的RequestMapping
//
//        return new RequestMapping() {
//            @Override
//            public String name() {
//                return "";
//            }
//
//            @Override
//            public String[] value() {
//                // 从方法注解中解析路径
//                for (Annotation annotation : method.getAnnotations()) {
//                    if (annotation.annotationType() == RequestLine.class) {
//                        RequestLine requestLine = (RequestLine) annotation;
//                        return new String[]{extractPath(requestLine.value())};
//                    } else if (annotation.annotationType().getName().contains("Mapping")) {
//                        // 处理Spring MVC注解
//                        try {
//                            Method valueMethod = annotation.annotationType().getMethod("value");
//                            String[] values = (String[]) valueMethod.invoke(annotation);
//                            return values;
//                        } catch (Exception e) {
//                            return new String[]{""};
//                        }
//                    }
//                }
//                return new String[]{""};
//            }
//
//            @Override
//            public RequestMethod[] method() {
//                // 解析HTTP方法
//                for (Annotation annotation : method.getAnnotations()) {
//                    if (annotation.annotationType() == RequestLine.class) {
//                        RequestLine requestLine = (RequestLine) annotation;
//                        return new RequestMethod[]{parseHttpMethod(requestLine.value())};
//                    } else if (annotation.annotationType() == GetMapping.class) {
//                        return new RequestMethod[]{RequestMethod.GET};
//                    } else if (annotation.annotationType() == PostMapping.class) {
//                        return new RequestMethod[]{RequestMethod.POST};
//                    }
//                    // 其他HTTP方法注解...
//                }
//                return new RequestMethod[]{RequestMethod.GET};
//            }
//
//            // 其他方法实现...
//        };
//    }
//
//    // 辅助方法：从RequestLine中提取路径
//    private static String extractPath(String requestLine) {
//        // 简化实现，实际应正确解析RequestLine格式
//        if (requestLine.startsWith("GET ")) {
//            return requestLine.substring(4);
//        } else if (requestLine.startsWith("POST ")) {
//            return requestLine.substring(5);
//        }
//        return requestLine;
//    }
//
//    // 辅助方法：从RequestLine中解析HTTP方法
//    private static RequestMethod parseHttpMethod(String requestLine) {
//        if (requestLine.startsWith("GET")) {
//            return RequestMethod.GET;
//        } else if (requestLine.startsWith("POST")) {
//            return RequestMethod.POST;
//        } else if (requestLine.startsWith("PUT")) {
//            return RequestMethod.PUT;
//        } else if (requestLine.startsWith("DELETE")) {
//            return RequestMethod.DELETE;
//        }
//        return RequestMethod.GET;
//    }
//}