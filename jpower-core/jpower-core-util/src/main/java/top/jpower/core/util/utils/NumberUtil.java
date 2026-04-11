package top.jpower.core.util.utils;

/**
 * @author mr.g
 */
public class NumberUtil extends cn.hutool.core.util.NumberUtil {

	/**
	 * 判断字符串是否为合法的整数格式
	 * <p>
	 * 不允许前导零（"0"本身除外），避免将字典编码如 "01"、"001" 误判为数字
	 * </p>
	 *
	 * @param value 字符串值
	 * @return 是否为整数
	 */
	public static boolean isCompleteInteger(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		int len = value.length();
		// 不允许前导零（"0"本身除外），如 "01"、"007" 保持为字符串
		if (len > 1 && value.charAt(0) == '0') {
			return false;
		}
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);
			if (i == 0 && c == '-') {
				continue;
			}
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
