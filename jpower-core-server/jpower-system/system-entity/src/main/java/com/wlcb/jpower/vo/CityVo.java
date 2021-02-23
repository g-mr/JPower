package com.wlcb.jpower.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wlcb.jpower.dbs.entity.city.TbCoreCity;
import com.wlcb.jpower.module.common.node.Node;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CityVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/22 0022 0:36
 * @Version 1.0
 */
@Data
public class CityVo extends TbCoreCity implements Node {

    private static final long serialVersionUID = 3438947425188438375L;

    @ApiModelProperty("城市类型")
    private String cityTypeStr;

    @ApiModelProperty("上级地区")
    private String pname;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Node> children;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

    @Override
    public String getParentId() {
        return getPcode();
    }

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
