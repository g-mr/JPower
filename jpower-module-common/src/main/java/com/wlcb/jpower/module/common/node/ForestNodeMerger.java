package com.wlcb.jpower.module.common.node;

import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ForestNodeMerger
 * @Description TODO 森林节点归并类
 * @Author 郭丁志
 * @Date 2020-07-25 22:45
 * @Version 1.0
 */
public class ForestNodeMerger {

    /**
     * 将节点数组归并为一个森林（多棵树）（填充节点的children域）
     * 时间复杂度为O(n^2)
     *
     * @param items 节点域
     * @param <T>   T 泛型标记
     * @return 多棵树的根节点集合
     */
    public static <T extends Node> List<T> merge(List<T> items) {
        ForestNodeManager<T> forestNodeManager = new ForestNodeManager<>(items);
        items.forEach(forestNode -> {
            if (!StringUtil.equals(forestNode.getParentId(), JpowerConstants.TOP_CODE)) {
                Node node = forestNodeManager.getTreeNodeAT(forestNode.getParentId());
                if (node != null) {
                    node.getChildren().add(forestNode);
                } else {
                    forestNodeManager.addParentId(forestNode.getId());
                }
            }
        });
        return forestNodeManager.getRoot();
    }
}
