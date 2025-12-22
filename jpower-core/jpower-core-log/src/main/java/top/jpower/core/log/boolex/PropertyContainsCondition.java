package top.jpower.core.log.boolex;

import ch.qos.logback.core.boolex.PropertyConditionBase;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 是否包含判断器
 *
 * @author mr.g
 * @date 2025-12-22 22:08
 * @description
 */
@Data
public class PropertyContainsCondition extends PropertyConditionBase {

    /**
     * The property name (key) to look up. Must be set before starting.
     */
    String key;

    /**
     * The expected value to compare the resolved property against.
     */
    String value;

    /**
     * Start the component and validate required parameters.
     * If either {@link #key} or {@link #value} is {@code null}, an error
     * is reported and the component does not start.
     */
    public void start() {
        if (key == null) {
            addError("In PropertyEqualsValue 'key' parameter cannot be null");
            return;
        }
        if (value == null) {
            addError("In PropertyEqualsValue 'value' parameter cannot be null");
            return;
        }
        super.start();
    }

    /**
     * Evaluate the condition: resolve the property named by {@link #key}
     * and compare it to {@link #value}.
     *
     * @return {@code true} if the resolved property equals the expected
     *         value; {@code false} otherwise
     */
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
