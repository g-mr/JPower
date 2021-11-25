/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wlcb.jpower.module.mp.support;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;

import java.util.Map;

/**
 * 定义常用的 sql关键字
 *
 * @author Chill
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
    public static final String DATE_EQUAL = "_dateeq";
    public static final String DATE_LT = "_datelt";
    public static final String IS_NULL = "_null";
    public static final String NOT_NULL = "_notnull";
    public static final String IGNORE = "_ignore";

    /**
     * 条件构造器
     *
     * @param query 查询字段
     * @param wrapper    查询包装类
     */
    public static void buildCondition(Map<String, Object> query, AbstractWrapper<?,String,?> wrapper) {
        if (Fc.isEmpty(query)) {
            return;
        }
        query.forEach((k, v) -> {
            if (Fc.hasEmpty(k, v) || k.endsWith(IGNORE)) {
                return;
            }
            if (k.endsWith(EQUAL)) {
                wrapper.eq(getColumn(k, EQUAL), v);
            } else if (k.endsWith(NOT_EQUAL)) {
                wrapper.ne(getColumn(k, NOT_EQUAL), v);
            } else if (k.endsWith(NOT_LIKE)) {
                wrapper.notLike(getColumn(k, NOT_LIKE), v);
            } else if (k.endsWith(GT)) {
                wrapper.gt(getColumn(k, GT), v);
            } else if (k.endsWith(LT)) {
                wrapper.lt(getColumn(k, LT), v);
            } else if (k.endsWith(DATE_GT)) {
                wrapper.gt(getColumn(k, DATE_GT), v);
            } else if (k.endsWith(DATE_EQUAL)) {
                wrapper.eq(getColumn(k, DATE_EQUAL), v);
            } else if (k.endsWith(DATE_LT)) {
                wrapper.lt(getColumn(k, DATE_LT), v);
            } else if (k.endsWith(IS_NULL)) {
                wrapper.isNull(getColumn(k, IS_NULL));
            } else if (k.endsWith(NOT_NULL)) {
                wrapper.isNotNull(getColumn(k, NOT_NULL));
            } else {
                wrapper.like(getColumn(k, LIKE), v);
            }
        });
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
