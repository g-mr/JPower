package com.qidiangk.smart.aster.utils;

import cn.hutool.core.util.StrUtil;

public class IdcardUtil extends cn.hutool.core.util.IdcardUtil {

    public static boolean isValidCard15And18(String idCard) {
        if (StrUtil.contains(idCard, "*")){
            idCard = StrUtil.replace(idCard, "*", "X");
        }
        return isValidCard18(idCard) || isValidCard15(idCard);
    }

}
