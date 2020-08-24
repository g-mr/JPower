package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;

import java.util.Random;

/**
 * @ClassName RandomUtil
 * @Description TODO 随机数工具类
 * @Author 郭丁志
 * @Date 2020-03-28 22:58
 * @Version 1.0
 */
public class RandomUtil {

    /**
     * @Author 郭丁志
     * @Description //TODO 6位随机数
     * @Date 14:51 2020-07-31
     * @Param []
     * @return java.lang.String
     **/
    public static String random6Num(){
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 6位随机字符串
     * @Date 15:00 2020-07-31
     * @Param [length]
     * @return java.lang.String
     **/
    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

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

    public static void main(String[] args) {
        System.out.println(createCityCode(null,"11"));
    }

}
