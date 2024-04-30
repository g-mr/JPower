package top.jpower.jpower.module.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.jpower.core.utils.utils.Fc;
import top.jpower.core.utils.utils.MapUtil;
import top.jpower.jpower.module.annotation.Function;
import top.jpower.jpower.module.annotation.Menu;

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
@AllArgsConstructor
public class FunctionGenerate implements ApplicationRunner {

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public final static Map<String,List<Map<String,Object>>> functions = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) {

        Map<RequestMappingInfo, HandlerMethod> handlerMethodsMap = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethodsMap.entrySet()) {
            RequestMappingInfo info = item.getKey();
            HandlerMethod method = item.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            if (Fc.notNull(p) && Fc.isNotEmpty(p.getPatterns())){
                String url = p.getPatterns().stream().findFirst().get();

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
