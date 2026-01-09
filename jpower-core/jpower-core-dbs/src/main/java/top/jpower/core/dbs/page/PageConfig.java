package top.jpower.core.dbs.page;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import top.jpower.core.dbs.config.properties.MybatisProperties;

@AutoConfiguration
public class PageConfig {

    @Bean
    public PageFilter pageFilter(MybatisProperties mybatisProperties){
        return new PageFilter(mybatisProperties.getPage());
    }

}
