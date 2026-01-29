package top.jpower.core.log.boolex;

import ch.qos.logback.core.boolex.PropertyConditionBase;
import cn.hutool.core.util.StrUtil;

/**
 * 是否包含判断器
 *
 * @author mr.g
 * @date 2025-12-22 22:08
 */
public class PropertyContainsCondition extends PropertyConditionBase {

    private String key;
    private String value;

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void start() {
        if (key == null) {
            addError("In PropertyContainsCondition 'key' parameter cannot be null");
            return;
        }
        if (value == null) {
            addError("In PropertyContainsCondition 'value' parameter cannot be null");
            return;
        }
        super.start();
    }

    @Override
    public boolean evaluate() {
        if (key == null) {
            addError("key cannot be null");
            return false;
        }

        String val = p(key);
        return StrUtil.contains(val, value);
    }
}