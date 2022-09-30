package com.wlcb.jpower.module.mp.support;

import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mr.g
 * @date 2022-09-27 17:00
 */
@AllArgsConstructor
public enum WrapperKeyword {

    EQ("eq"),
    NOT_EQ("noteq"),
    LIKE("like"),
    NOT_LIKE("notlike"),
    GT("gt"),
    LT("lt"),
    GE("ge"),
    LE("le"),
    DATE_GT("dategt"),
    DATE_EQ("dateeq"),
    DATE_LT("datelt"),
    DATE_GE("datege"),
    DATE_LE("datele"),
    IS_NULL("null"),
    NOT_NULL("notnull"),
    IGNORE("ignore");

    @Getter
    private final String keyword;

    public String getSuffixKeyword(){
        return StringPool.C_UNDERLINE+keyword;
    }

    public String getColumn(String param){
        return StringUtil.humpToUnderline(StringUtil.removeSuffix(param, getSuffixKeyword()));
    }
}
