package com.wlcb.jpower.module.dbs.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Function
 * @Description TODO 菜单返回试图
 * @Author 郭丁志
 * @Date 2020-07-30 10:49
 * @Version 1.0
 */
@Data
public class FunctionVo extends TbCoreFunction implements Node {

    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Node> children;


    @Override
    public List<Node> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }

    /**
     * 是否有子孙节点
     */
    @Override
    public Boolean getHasChildren() {
        if (children.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

}
