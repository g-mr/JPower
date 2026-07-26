package top.jpower.core.asterisk.dto;

/**
 * @author mr.g
 *
 * @param say 不打断得话语
 * @param isPrecise 是否精确识别
 */
public record AsrNoInterrupt(String say, Boolean isPrecise) {
}
