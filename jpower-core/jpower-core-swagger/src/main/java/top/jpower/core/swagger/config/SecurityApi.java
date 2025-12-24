package top.jpower.core.swagger.config;

import cn.hutool.core.collection.ListUtil;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.jpower.core.util.utils.Fc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 鉴权构建
 *
 * @author mr.g
 */
@Getter
@AllArgsConstructor
public class SecurityApi {

    private Map<String, SecurityScheme> allSecurity;

    private List<SecurityRequirement> securityForApi;

    public static SecurityApi build(List<SecurityScheme> authorization) {
        return new SecurityApi(securitySchemes(authorization), securityContexts(authorization));
    }

    private static Map<String, SecurityScheme> securitySchemes(List<SecurityScheme> authorizations) {
        return authorizations.stream()
                .filter(item -> Fc.isNotBlank(item.getName()))
                .collect(Collectors.toMap(
                        SecurityScheme::getName,
                        item -> item,
                        (first, second) -> first
                ));
    }

    private static List<SecurityRequirement> securityContexts(List<SecurityScheme> authorizations) {
        SecurityRequirement securityRequirement = new SecurityRequirement();
        authorizations.forEach(authorization -> securityRequirement.addList(authorization.getName()));
        return ListUtil.of(securityRequirement);
    }
}
