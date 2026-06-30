package com.qidiangk.smart.aster.handler.nodes;


import jakarta.validation.constraints.NotNull;
import com.qidiangk.smart.aster.pojo.UserIntent;

public interface NodeGranter<T extends UserIntent.Node> {

	@NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, T node) ;

}
