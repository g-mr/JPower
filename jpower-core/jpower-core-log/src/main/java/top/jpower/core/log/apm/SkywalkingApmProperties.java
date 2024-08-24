package top.jpower.core.log.apm;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mr.g
 * @date 2023/11/21 0:22
 * @description
 */
@Data
@ConfigurationProperties("jpower.skywalking.apm")
public class SkywalkingApmProperties {

    private boolean enable = true;

    private List<String> excludes = new ArrayList<>();

}
