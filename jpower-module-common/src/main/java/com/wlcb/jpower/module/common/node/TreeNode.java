package com.wlcb.jpower.module.common.node;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @ClassName TreeNode
 * @Description TODO 树型节点类
 * @Author 郭丁志
 * @Date 2020-07-25 22:57
 * @Version 1.0
 */
@Data
public class TreeNode extends BaseNode{

    private static final long serialVersionUID = 1L;

    private String title;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long key;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long value;

}
