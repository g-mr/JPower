package top.jpower.core.asterisk.dto;

import java.time.Duration;

/**
 * @author mr.g
 *
 * @param idleDuration 空闲时长
 * @param idleSay 空闲时说的话
 */
public record AsrIdle(Duration idleDuration, String idleSay) {
}
