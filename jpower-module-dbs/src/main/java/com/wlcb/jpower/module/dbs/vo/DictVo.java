package com.wlcb.jpower.module.dbs.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DictVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/21 0021 22:05
 * @Version 1.0
 */
@Data
public class DictVo extends TbCoreDict implements Node {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Node> children;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

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
            return this.hasChildren;
        }
    }

}
