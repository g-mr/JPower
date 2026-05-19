package top.jpower.core.asterisk.audio;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.SneakyThrows;
import top.jpower.core.asterisk.properties.AsteriskAgiProperties;
import top.jpower.core.util.utils.Fc;

import java.io.File;

public interface TtsClient extends AutoCloseable {

    @SneakyThrows(ClassNotFoundException.class)
    static TtsClient createInstance() {
        AsteriskAgiProperties asteriskAgiProperties = SpringUtil.getBean(AsteriskAgiProperties.class);
        if (Fc.isNull(asteriskAgiProperties.getTtsImpl())){
            throw new ClassNotFoundException("asterisk.agi.tts-impl配置缺失");
        }
        if (!ClassUtil.isAssignable(TtsClient.class, asteriskAgiProperties.getTtsImpl())){
            throw new ClassNotFoundException(asteriskAgiProperties.getTtsImpl()+"类必须是TtsClient的实现类");
        }
        try {
            return ReflectUtil.newInstance(asteriskAgiProperties.getTtsImpl());
        } catch (Exception e) {
            throw new IllegalArgumentException("实现类必须有无参构造函数", e);
        }
    }

    TtsResult process(String say, File file);

    void close();

}
