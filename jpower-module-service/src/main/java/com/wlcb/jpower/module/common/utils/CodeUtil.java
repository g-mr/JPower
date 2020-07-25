package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;

/**
 * @ClassName CodeUtil
 * @Description TODO 编号生成器
 * @Author 郭丁志
 * @Date 2020/7/25 0025 19:08
 * @Version 1.0
 */
public class CodeUtil {

    /**
     * @author 郭丁志
     * @Description //TODO 根据给定的编号生成新的城市编号
     * @date 19:09 2020/7/25 0025
     * @param pcode 父亲编号
     * @param code 当前编号
     * @return java.lang.String
     */
    public static String createCityCode(String pcode,String code){
        if (!StringUtil.equals(pcode, JpowerConstants.TOP_CODE)){
            // TODO: 2020/7/25 0025 如果不是顶级节点，则code需要拼接父级的code生成新的code
            String startSuff = StringUtil.removeAllSuffix(pcode,"0");
            // TODO: 2020/7/25 0025 通过获取特定的前缀判断是否是指定的开头
            if(!code.startsWith(startSuff)){
                code = startSuff+code;
            }
        }
        return code;
    }

}
