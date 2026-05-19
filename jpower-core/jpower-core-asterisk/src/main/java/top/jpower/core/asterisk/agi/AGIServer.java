package top.jpower.core.asterisk.agi;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.asteriskjava.fastagi.AgiScript;
import org.asteriskjava.fastagi.DefaultAgiServer;
import top.jpower.core.asterisk.agi.annotation.Agi;
import top.jpower.core.asterisk.agi.strategy.JpowerMappingStrategy;
import top.jpower.core.asterisk.properties.AsteriskAgiProperties;

import java.util.*;

/**
 * AGI启动、路由管理
 *
 * @author mr.g
 */
public class AGIServer  extends DefaultAgiServer implements Runnable {

    public AGIServer(List<? extends AgiScript> scriptList, AsteriskAgiProperties asteriskProperties) {
        JpowerMappingStrategy mappingStrategy = new JpowerMappingStrategy(loadAgiScript(scriptList));

        if (asteriskProperties.getPort() != null){
            setPort(asteriskProperties.getPort());
        }
        if (asteriskProperties.getPoolSize() != null){
            setPoolSize(asteriskProperties.getPoolSize());
        }
        if (asteriskProperties.getMaxPoolSize() != null){
            setMaximumPoolSize(asteriskProperties.getMaxPoolSize());
        }

        this.setMappingStrategy(mappingStrategy);
    }

    public void start(){
        new Thread(this, "AGIServer:" + this.getPort()).start();
    }

    private Map<String, AgiScript> loadAgiScript(List<? extends AgiScript> scripts) {
        Map<String, AgiScript> map = new HashMap<>();
        scripts.forEach(agiScript -> {
            Agi agi = AnnotationUtil.getAnnotation(agiScript.getClass(), Agi.class);
            if (agi != null){
                map.put(agi.value(), agiScript);
            } else {
                String[] beanNames = SpringUtil.getBeanNamesForType(agiScript.getClass());
                Optional<String> optional = Arrays.stream(beanNames).filter(name-> StrUtil.equals(SpringUtil.getBean(name).getClass().getName(), agiScript.getClass().getName())).findFirst();
                optional.ifPresent(name -> map.put(name, agiScript));
            }
        });
        return map;
    }

}
