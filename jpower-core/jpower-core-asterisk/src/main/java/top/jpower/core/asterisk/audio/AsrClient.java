package top.jpower.core.asterisk.audio;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.SneakyThrows;
import top.jpower.core.asterisk.properties.AsteriskAgiProperties;
import top.jpower.core.util.utils.Fc;

import java.io.PipedInputStream;

public interface AsrClient extends AutoCloseable {

    /**
     * 创建实例
     */
    @SneakyThrows(ClassNotFoundException.class)
    static AsrClient createInstance() {
        AsteriskAgiProperties asteriskAgiProperties = SpringUtil.getBean(AsteriskAgiProperties.class);
        if (Fc.isNull(asteriskAgiProperties.getAsrImpl())){
            throw new ClassNotFoundException("asterisk.agi.asr.impl配置缺失");
        }
        if (!ClassUtil.isAssignable(AsrClient.class, asteriskAgiProperties.getAsrImpl())){
            throw new ClassNotFoundException(asteriskAgiProperties.getAsrImpl()+"类必须是AsrClient的实现类");
        }
        try {
            return ReflectUtil.newInstance(asteriskAgiProperties.getAsrImpl());
        } catch (Exception e) {
            throw new IllegalArgumentException("实现类必须有无参构造函数", e);
        }
    }


    AsrResult process(PipedInputStream pipedInputStream) throws InterruptedException;

    void close();

}
