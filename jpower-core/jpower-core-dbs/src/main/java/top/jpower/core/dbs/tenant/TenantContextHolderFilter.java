package top.jpower.core.dbs.tenant;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.user.model.UserDto;

import java.util.Optional;

/**
 * 租户上下文过滤器
 *
 * @author mr.g
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantContextHolderFilter extends GenericFilterBean {

    private final UserConfig userConfig;

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        UserDto userDto = Optional.ofNullable(userConfig.queryUser()).orElse(new UserDto());

        String tenantCode = userDto.getTenantCode();

        log.debug("获取的租户ID为:{}", tenantCode);

        // 超级用户不需要租户过滤
        if (StrUtil.isNotBlank(tenantCode) && !userDto.isRoot() ) {
            TenantContextHolder.setTenantCode(tenantCode);
        } else {
            TenantContextHolder.setTenantCode(null);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }

}
