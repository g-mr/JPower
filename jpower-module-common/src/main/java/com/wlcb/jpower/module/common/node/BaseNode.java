package com.wlcb.jpower.module.common.node;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BaseNode
 * @Description TODO 节点基类
 * @Author 郭丁志
 * @Date 2020-07-25 22:58
 * @Version 1.0
 */
@Data
public class BaseNode implements Node {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    protected String id;

    /**
     * 父节点ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    protected String parentId;

    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<Node> children = new ArrayList<>();

    /**
     * 是否有子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

    /**
     * 是否有子孙节点
     */
    @Override
    public Boolean getHasChildren() {
//        if (children.size() > 0) {
//            return true;
//        } else {
//            return this.hasChildren;
//        }
        return this.hasChildren;
    }

}
