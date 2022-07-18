package com.wlcb.jpower.module.common.utils;

/**
 * MD5加密算法
 *
 * @author mr.g
 **/
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class MD5 extends cn.hutool.crypto.digest.DigestUtil {

    /**
     * 把md5结果转为大写
     *
     * @author mr.g
     * @param data 数据
     * @return java.lang.String
     **/
    public static String md5HexToUpperCase(final String data){
        String md5 = md5Hex(data);
        if (Fc.isNotBlank(md5)){
            md5 = md5.toUpperCase();
        }
        return md5;
    }
}