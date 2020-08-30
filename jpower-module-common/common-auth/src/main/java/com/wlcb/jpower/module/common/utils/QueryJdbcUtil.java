package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.auth.ClientDetails;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @ClassName QueryJdbcUtil
 * @Description TODO JDBC查询工具
 * @Author 郭丁志
 * @Date 2020/8/30 0030 21:58
 * @Version 1.0
 */
public class QueryJdbcUtil {

    private final static String SELECT_CLIENT_SQL = "select client_code,client_secret,access_token_validity,refresh_token_validity from tb_core_client where client_code = ?";

    private final static JdbcTemplate jdbcTemplate;
    static {
        jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
    }


    public static ClientDetails loadClientByClientCode(String clientCode) {
        try {
            return jdbcTemplate.queryForObject(SELECT_CLIENT_SQL, new String[]{clientCode}, new BeanPropertyRowMapper<>(ClientDetails.class));
        } catch (Exception ex) {
            return null;
        }
    }
}
