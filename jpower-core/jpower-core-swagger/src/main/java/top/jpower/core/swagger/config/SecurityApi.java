package top.jpower.core.swagger.config;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import top.jpower.core.swagger.property.SwaggerProperties;
import top.jpower.core.swagger.utils.PathMatch;
import top.jpower.core.util.utils.Fc;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 鉴权构建
 *
 * @author mr.g
 */
@Getter
@AllArgsConstructor
public class SecurityApi {

    private List<ApiKey> allSecurity;

    private List<SecurityContext> securityForApi;

    public static SecurityApi build(List<SwaggerProperties.Authorization> authorization) {
        return new SecurityApi(securitySchemes(authorization), securityContexts(authorization));
    }

    private static List<ApiKey> securitySchemes(List<SwaggerProperties.Authorization> authorizations) {
        return authorizations.stream()
                .map(authorization -> new ApiKey(authorization.getName(), authorization.getName(), "header"))
                .collect(Collectors.toList());
    }

    private static List<SecurityContext> securityContexts(List<SwaggerProperties.Authorization> authorizations) {
        return authorizations.stream()
                .filter(authorization -> Fc.isNotEmpty(authorization.getPath()) || Fc.isNotEmpty(authorization.getExcludePath()))
                .map(authorization -> SecurityContext.builder()
                        .securityReferences(ListUtil.of(new SecurityReference(authorization.getName(), new AuthorizationScope[0])))
                        .forPaths(PathMatch.ant(authorization.getPath()).and(PathMatch.ant(authorization.getExcludePath()).negate()))
                        .build())
                .collect(Collectors.toList());
    }
}
