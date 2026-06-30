package com.qidiangk.smart.aster.handler.nodes;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * @author mr.gmac
 */
@Builder
@Jacksonized
public record NodeResult(String nextId, Object result) {

}
