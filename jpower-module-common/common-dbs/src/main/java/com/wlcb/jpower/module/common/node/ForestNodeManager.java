package com.wlcb.jpower.module.common.node;

import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ForestNodeManager
 * @Description TODO 森林管理类
 * @Author 郭丁志
 * @Date 2020-07-25 22:47
 * @Version 1.0
 */
public class ForestNodeManager<T extends Node> {

    /**
     * 森林的所有节点
     */
    private List<T> list;

    /**
     * 森林的父节点ID
     */
    private List<String> parentIds = new ArrayList<>();

    public ForestNodeManager(List<T> items) {
        list = items;
    }

    /**
     * 根据节点ID获取一个节点
     * @param id 节点ID
     * @return 对应的节点对象
     */
    public Node getTreeNodeAT(String id) {
        for (Node forestNode : list) {
            if (StringUtil.equals(forestNode.getId(),id)) {
                return forestNode;
            }
        }
        return null;
    }

    /**
     * 增加父节点ID
     * @param parentId 父节点ID
     */
    public void addParentId(String parentId) {
        parentIds.add(parentId);
    }

    /**
     * 获取树的根节点(一个森林对应多颗树)
     * @return 树的根节点集合
     */
    public List<T> getRoot() {
        List<T> roots = new ArrayList<>();
        for (T forestNode : list) {
            if (StringUtil.equals(forestNode.getParentId(), JpowerConstants.TOP_CODE) || parentIds.contains(forestNode.getId())) {
                roots.add(forestNode);
            }
        }
        return roots;
    }

}
