package com.qidiangk.smart.maxkb.client.po;

import cn.hutool.core.bean.BeanUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共转换类
 *
 * @author mr.g
 */
public class SearchBO implements Serializable {

    public Map<String, Object> toMap() {
        return BeanUtil.beanToMap(this, true, true);
    }

}
