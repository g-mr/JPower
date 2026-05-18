package top.jpower.core.ai.prompt;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.ai.properties.AiProperties;
import top.jpower.core.util.utils.Fc;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * Nacos提示词处理
 * <p>通过Nacos配置中心获取提示词内容，支持实时更新</p>
 *
 * @author mr.g
 */
@Slf4j
@RequiredArgsConstructor
public class NacosPromptProcessed implements PromptProcessed {

    private final AiProperties aiProperties;
    private final NacosConfigProperties nacosConfigProperties;

    private final ConcurrentHashMap<String, String> promptCache = new ConcurrentHashMap<>();
    private volatile ConfigService configService;

    @Override
    public String process(String contextKey) {
        if (Fc.isBlank(contextKey)) {
            log.warn("提示词KEY[contextKey]是空的");
            return "";
        }

        // 优先从缓存获取（缓存通过监听器实时更新）
        String cached = promptCache.get(contextKey);
        if (cached != null) {
            return cached;
        }

        try {
            ConfigService service = getConfigService();
            if (service == null) {
                log.error("Nacos ConfigService初始化失败，无法获取提示词配置");
                return "";
            }

            // 从Nacos获取配置
            String content = service.getConfig(contextKey, aiProperties.getPrompt().getNacos().getGroup(), aiProperties.getPrompt().getNacos().getTimeout());
            if (Fc.isBlank(content)) {
                log.warn("Nacos中未找到提示词配置，contextKey={}", contextKey);
                return "";
            }

            // 缓存并注册监听器
            promptCache.put(contextKey, content);
            addListener(service, contextKey);

            return content;
        } catch (NacosException e) {
            log.error("从Nacos获取提示词配置失败，contextKey={}", contextKey, e);
            return "";
        }
    }

    /**
     * 注册Nacos配置监听器，实现配置实时更新
     */
    private void addListener(ConfigService service, String contextKey) {
        try {
            service.addListener(contextKey, aiProperties.getPrompt().getNacos().getGroup(), new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("Nacos提示词配置更新，contextKey={}", contextKey);
                    if (Fc.isBlank(configInfo)) {
                        promptCache.remove(contextKey);
                    } else {
                        promptCache.put(contextKey, configInfo);
                    }
                }
            });
        } catch (NacosException e) {
            log.error("注册Nacos提示词配置监听器失败，contextKey={}", contextKey, e);
        }
    }

    /**
     * 获取或初始化ConfigService（双重检查锁）
     */
    private ConfigService getConfigService() {
        if (configService == null) {
            synchronized (this) {
                if (configService == null) {
                    try {
                        Properties properties = new Properties();
                        properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosConfigProperties.getServerAddr());
                        if (Fc.isNotBlank(nacosConfigProperties.getNamespace())) {
                            properties.setProperty(PropertyKeyConst.NAMESPACE, nacosConfigProperties.getNamespace());
                        }
                        if (Fc.isNotBlank(nacosConfigProperties.getUsername())) {
                            properties.setProperty(PropertyKeyConst.USERNAME, nacosConfigProperties.getUsername());
                        }
                        if (Fc.isNotBlank(nacosConfigProperties.getPassword())) {
                            properties.setProperty(PropertyKeyConst.PASSWORD, nacosConfigProperties.getPassword());
                        }
                        configService = NacosFactory.createConfigService(properties);
                    } catch (NacosException e) {
                        log.error("初始化Nacos ConfigService失败", e);
                    }
                }
            }
        }
        return configService;
    }

}
