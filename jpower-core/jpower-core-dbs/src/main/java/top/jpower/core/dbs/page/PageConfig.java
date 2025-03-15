package top.jpower.core.dbs.page;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class PageConfig {

    @Bean
    public PageFilter pageFilter(){
        return new PageFilter();
    }

}
