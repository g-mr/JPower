package com.wlcb.jpower.module.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName QuxianUtils
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-03-20 23:03
 * @Version 1.0
 */
public class QuxianUtils {

    public static String simplifyQuxian(String quxian){
        if (StringUtils.isNotBlank(quxian)){
            switch (quxian){
                case "集宁区" :
                    quxian = "集宁";
                    break;
                case "察右前旗" :
                    quxian = "前旗";
                    break;
                case "察右中旗" :
                    quxian = "中旗";
                    break;
                case "丰镇市" :
                    quxian = "丰镇";
                    break;
                case "察右后旗" :
                    quxian = "后旗";
                    break;
                case "化德县" :
                    quxian = "化德";
                    break;
                case "商都县" :
                    quxian = "商都";
                    break;
                case "兴和县" :
                    quxian = "兴和";
                    break;
                case "卓资县" :
                    quxian = "卓资";
                    break;
                case "凉城县" :
                    quxian = "凉城";
                    break;
                default : //可选
                    quxian = quxian;
                    break;
            }
        }

        return quxian;
    }

}
