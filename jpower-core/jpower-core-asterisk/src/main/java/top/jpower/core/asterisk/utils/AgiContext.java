package top.jpower.core.asterisk.utils;

import org.asteriskjava.fastagi.AgiChannel;
import top.jpower.core.asterisk.utils.support.FutureConcurrentHashMap;
import top.jpower.core.util.utils.Fc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AgiContext<T> {

    private static final Map<String, AgiContext> MAP_UNIQUEID = new ConcurrentHashMap<>();
    private final Map<String, T> MAP_CHACE = new FutureConcurrentHashMap<>();

    private AgiContext(String uniqueId) {
        MAP_UNIQUEID.put(uniqueId, this);
    }

    public static <T> AgiContext<T> cache(AgiChannel channel){
        if (MAP_UNIQUEID.containsKey(channel.getUniqueId())){
            return MAP_UNIQUEID.get(channel.getUniqueId());
        }
        return new AgiContext<T>(channel.getUniqueId());
    }
    public static void clear(AgiChannel channel){
        MAP_UNIQUEID.remove(channel.getUniqueId());
    }

    public Map<String, T> all(){
        return MAP_CHACE;
    }

    public T get(String key){
        return MAP_CHACE.get(key);
    }

    public void put(String key, T obj){
        if (Fc.notNull(obj)){
            MAP_CHACE.put(key, obj);
        }
    }

    public void remove(String key) {
        MAP_CHACE.remove(key);
    }
}
