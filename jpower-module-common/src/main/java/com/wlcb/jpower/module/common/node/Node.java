package com.wlcb.jpower.module.common.node;

import java.io.Serializable;
import java.util.List;

/**
 * 树形BEAN
 *
 * @author gdz
 */
public interface Node extends Serializable {

    /**
     * 主键
     *
     * @return Integer
     */
    Long getId();

    /**
     * 父主键
     *
     * @return Integer
     */
    Long getParentId();

    /**
     * 子孙节点
     *
     * @return List
     */
    List<Node> getChildren();

    /**
     * 是否有子孙节点
     *
     * @return Boolean
     */
    default Boolean getHasChildren() {
        return false;
    }

}
