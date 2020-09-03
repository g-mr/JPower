package com.wlcb.jpower.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.module.common.node.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OrgVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/22 0022 0:36
 * @Version 1.0
 */
@Data
public class OrgVo extends TbCoreOrg implements Node {

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
