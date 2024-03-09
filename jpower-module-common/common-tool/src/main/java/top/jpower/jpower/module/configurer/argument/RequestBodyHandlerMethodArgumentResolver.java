package top.jpower.jpower.module.configurer.argument;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * RequestSingleBody参数解析器
 *  解决获取body（JSON）下指定名称的参数值
 *
 * @author mr.g
 */
public class RequestBodyHandlerMethodArgumentResolver extends RequestResponseBodyMethodProcessor {

    /**
     * Basic constructor with converters only. Suitable for resolving
     * {@code @RequestBody}. For handling {@code @ResponseBody} consider also
     * providing a {@code ContentNegotiationManager}.
     */
    public RequestBodyHandlerMethodArgumentResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    /**
     * Basic constructor with converters and {@code ContentNegotiationManager}.
     * Suitable for resolving {@code @RequestBody} and handling
     * {@code @ResponseBody} without {@code Request~} or
     * {@code ResponseBodyAdvice}.
     */
    public RequestBodyHandlerMethodArgumentResolver(List<HttpMessageConverter<?>> converters,
                                              @Nullable ContentNegotiationManager manager) {

        super(converters, manager);
    }

    /**
     * Complete constructor for resolving {@code @RequestBody} method arguments.
     * For handling {@code @ResponseBody} consider also providing a
     * {@code ContentNegotiationManager}.
     * @since 4.2
     */
    public RequestBodyHandlerMethodArgumentResolver(List<HttpMessageConverter<?>> converters,
                                              @Nullable List<Object> requestResponseBodyAdvice) {

        super(converters, null, requestResponseBodyAdvice);
    }

    /**
     * Complete constructor for resolving {@code @RequestBody} and handling
     * {@code @ResponseBody}.
     */
    public RequestBodyHandlerMethodArgumentResolver(List<HttpMessageConverter<?>> converters,
                                              @Nullable ContentNegotiationManager manager, @Nullable List<Object> requestResponseBodyAdvice) {

        super(converters, manager, requestResponseBodyAdvice);
    }


    /**
     * Whether the given {@linkplain MethodParameter method parameter} is
     * supported by this resolver.
     *
     * @param parameter the method parameter to check
     * @return {@code true} if this resolver supports the supplied parameter;
     * {@code false} otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        RequestSingleBody singleBody = parameter.getParameterAnnotation(RequestSingleBody.class);
        return parameter.hasParameterAnnotation(RequestSingleBody.class) && Fc.notNull(singleBody);
    }

    /**
     * Resolves a method parameter into an argument value from a given request.
     * A {@link ModelAndViewContainer} provides access to the model for the
     * request. A {@link WebDataBinderFactory} provides a way to create
     * a {@link WebDataBinder} instance when needed for data binding and
     * type conversion purposes.
     *
     * @param parameter     the method parameter to resolve. This parameter must
     *                      have previously been passed to {@link #supportsParameter} which must
     *                      have returned {@code true}.
     * @param mavContainer  the ModelAndViewContainer for the current request
     * @param webRequest    the current request
     * @param binderFactory a factory for creating {@link WebDataBinder} instances
     * @return the resolved argument value, or {@code null} if not resolvable
     * @throws Exception in case of errors with the preparation of argument values
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        RequestSingleBody singleBody = parameter.getParameterAnnotation(RequestSingleBody.class);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (Fc.isNull(request)){
            return null;
        }

        String name = (singleBody != null && StringUtils.hasLength(singleBody.name()) ?
                singleBody.name() : parameter.getParameterName());
        Assert.state(name != null, "Unresolvable parameter name");

        Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
        if (Fc.notNull(arg)){
            String body = Fc.toStr(arg);
            if (JsonUtil.isJsonObject(body)){
                JSONObject jsonObject = JSONObject.parseObject(body);
                if (!jsonObject.containsKey(name) && checkRequired(parameter)) {
                    throw new HttpMessageNotReadableException("Required request body["+name+"] is missing: " +
                            parameter.getExecutable().toGenericString(), new ServletServerHttpRequest(request));
                }
                return jsonObject.get(name);
            }
        }
        return arg;
    }

    @Override
    protected boolean checkRequired(MethodParameter parameter) {
        RequestSingleBody requestSingleBody = parameter.getParameterAnnotation(RequestSingleBody.class);
        return (requestSingleBody != null && requestSingleBody.required() && !parameter.isOptional());
    }


}
