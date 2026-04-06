package top.jpower.core.auth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.jpower.core.auth.annotation.Function;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成功能节点
 *
 * @author mr.g
 * @date 2022-09-30 17:19
 */
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class FunctionGenerate implements ApplicationRunner {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public final static Map<String,List<Map<String,Object>>> functions = new HashMap<>();

    public FunctionGenerate(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public void run(ApplicationArguments args) {

        Map<RequestMappingInfo, HandlerMethod> handlerMethodsMap = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethodsMap.entrySet()) {
            RequestMappingInfo info = item.getKey();
            HandlerMethod method = item.getValue();
			String url = getUrl(info.getPatternsCondition(), info.getPathPatternsCondition());
            if (Fc.isNotBlank(url)){
                Function function = method.getMethodAnnotation(Function.class);

                if (Fc.notNull(function)){
                    for (Menu menu : function.menus()){
                        if (Fc.isNoneBlank(menu.menuCode(),menu.code())){
                            List<Map<String,Object>> list = functions.getOrDefault(menu.menuCode(),new ArrayList<>());
                            Map<String,Object> map = MapUtil.newHashMap(4);
                            map.put("name", Fc.blankDefault(menu.name(),function.value()));
                            //判断code是否重复
                            if (isExist(menu.code())){
                                throw new IllegalArgumentException("@Function[code] ["+menu.code()+"] exist repeat value");
                            }
                            map.put("code", menu.code());
                            map.put("btnCode", menu.btnCode());
                            map.put("type", menu.type());
                            map.put("alias", Fc.blankDefault(function.alias(), MapUtil.getStr(map, "name")));
                            map.put("url",url);
                            list.add(map);
                            functions.put(menu.menuCode(),list);
                        }
                    }
                }
            }
        }

    }

	private String getUrl(PatternsRequestCondition patternsCondition, PathPatternsRequestCondition pathPatternsCondition){
		if (Fc.notNull(patternsCondition) && Fc.isNotEmpty(patternsCondition.getPatterns())) {
			return patternsCondition.getPatterns().stream().findFirst().orElse(null);
		}
		if (Fc.notNull(pathPatternsCondition) && Fc.isNotEmpty(pathPatternsCondition.getPatterns())) {
			return pathPatternsCondition.getFirstPattern().getPatternString();
		}
		return null;
	}

    private boolean isExist(String code){

        boolean is = functions.containsKey(code);
        if (is){
            return true;
        }
        for (String key :functions.keySet()){
            is = functions.get(key).stream().anyMatch(m->Fc.equalsValue(m.get("code"),code));
            if (is){
                return true;
            }
        }
        return false;
    }
}
