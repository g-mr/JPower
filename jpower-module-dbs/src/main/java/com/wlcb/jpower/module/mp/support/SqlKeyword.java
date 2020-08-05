package com.wlcb.jpower.module.mp.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

import java.util.Map;

/**
 * @ClassName SqlKeyword
 * @Description TODO 定义常用的 sql关键字 并拼接查询条件
 * @Author 郭丁志
 * @Date 2020-07-23 17:04
 * @Version 1.0
 */
public class SqlKeyword {

    private final static String SQL_REGEX = "'|%|--|insert|delete|select|count|group|union|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|sql";

    public static final String EQUAL = "_eq";
    public static final String NOT_EQUAL = "_noteq";
    public static final String LIKE = "_like";
    public static final String NOT_LIKE = "_notlike";
    public static final String GT = "_gt";
    public static final String LT = "_lt";
    public static final String DATE_GT = "_dategt";
    public static final String DATE_EQUAL = "_dateequal";
    public static final String DATE_LT = "_datelt";
    public static final String IS_NULL = "_null";
    public static final String NOT_NULL = "_notnull";
    public static final String IGNORE = "_ignore";

    /**
     * 条件构造器
     *
     * @param query 查询字段
     * @param qw    查询包装类
     */
    public static void buildCondition(Map<String, Object> query, QueryWrapper<?> qw) {
        if (Fc.isEmpty(query)) {
            return;
        }
        query.forEach((k, v) -> {
            if (Fc.hasEmpty(k, v) || k.endsWith(IGNORE)) {
                return;
            }
            if (k.endsWith(EQUAL)) {
                qw.eq(getColumn(k, EQUAL), v);
            } else if (k.endsWith(NOT_EQUAL)) {
                qw.ne(getColumn(k, NOT_EQUAL), v);
            } else if (k.endsWith(NOT_LIKE)) {
                qw.notLike(getColumn(k, NOT_LIKE), v);
            } else if (k.endsWith(GT)) {
                qw.gt(getColumn(k, GT), v);
            } else if (k.endsWith(LT)) {
                qw.lt(getColumn(k, LT), v);
            } else if (k.endsWith(DATE_GT)) {
                qw.gt(getColumn(k, DATE_GT), v);
            } else if (k.endsWith(DATE_EQUAL)) {
                qw.eq(getColumn(k, DATE_EQUAL), v);
            } else if (k.endsWith(DATE_LT)) {
                qw.lt(getColumn(k, DATE_LT), v);
            } else if (k.endsWith(IS_NULL)) {
                qw.isNull(getColumn(k, IS_NULL));
            } else if (k.endsWith(NOT_NULL)) {
                qw.isNotNull(getColumn(k, NOT_NULL));
            } else {
                qw.like(getColumn(k, LIKE), v);
            }
        });

        qw.eq("status",1);
    }

    /**
     * 获取数据库字段
     *
     * @param column  字段名
     * @param keyword 关键字
     * @return String
     */
    private static String getColumn(String column, String keyword) {
        return StringUtil.humpToUnderline(StringUtil.removeSuffix(column, keyword));
    }

    /**
     * 把SQL关键字替换为空字符串
     *
     * @param param 关键字
     * @return string
     */
    public static String filter(String param) {
        if (param == null) {
            return null;
        }
        return param.replaceAll("(?i)" + SQL_REGEX, StringPool.EMPTY);
    }

}
