package top.jpower.core.asterisk.agi.strategy;

import cn.hutool.core.map.MapUtil;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.asteriskjava.fastagi.MappingStrategy;

import java.util.Map;

public class JpowerMappingStrategy implements MappingStrategy {
    private final Map<String, AgiScript> mappings;

    public JpowerMappingStrategy(Map<String, AgiScript> mappings) {
        this.mappings = mappings;
    }

    private String getKey(String script){
        if (script.contains(".")) {
            script = script.substring(0, script.indexOf("."));
        }

        if (script.startsWith("/")) {
            // this is specifically for the "FastAgiSimulator"
            script = script.substring(1);
        }

        return script;
    }

    @Override
    public AgiScript determineScript(AgiRequest request, AgiChannel channel) {
        if (MapUtil.isEmpty(mappings)) {
            return null;
        }

        AgiScript script = mappings.get(request.getScript());
        if (script == null){
            return mappings.get(getKey(request.getScript()));
        }
        return script;
    }
}
