package top.jpower.core.deploy.service;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * 加载配置文件
 *
 * @author mr.g
 **/
@Slf4j
@AutoService(DeployService.class)
public class DeployServiceImpl implements DeployService {

    /**
     * 启动时 处理 SpringApplicationBuilder
     * @param builder SpringApplication 启动器
     * @param properties 配置
     * @param appName 项目名称
     * @param profile 环境变量
     */
    @Override
    public void deploy(SpringApplication builder, ConfigurableEnvironment properties, String appName, String profile) {
        Resource resource = new ClassPathResource("jpower-deploy.yml");
        try {
            List<PropertySource<?>> sources = new YamlPropertySourceLoader()
                    .load(resource.getFilename(), resource);

            if (!sources.isEmpty()) {
                sources.forEach(propertySource -> {
                    properties.getPropertySources().addLast(propertySource);
                });
            }
        } catch (IOException e) {
            log.error("加载jpower-deploy.yml文件出错", e);
        }
    }

    /**
     * 获取排列顺序
     * @return order
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
