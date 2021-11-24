/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wlcb.jpower.module.common.node;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;

import java.util.List;
import java.util.Map;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.SORT;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.SORTNUM;

/**
 * @author mr.g
 * @Desc TODO 森林节点归并类
 * @Date 2020-07-25 22:45
 */
public class ForestNodeMerger {

    /**
     * @Description 将节点数组归并为一个森林
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

    private static final TreeNodeConfig CONFIG = new TreeNodeConfig();

    static {
        CONFIG.setWeightKey(SORT);
    }

    public static <T> List<Tree<String>> mergeTree(List<T> list) {
        return TreeUtil.build( list, JpowerConstants.TOP_CODE, CONFIG,  (bean, tree) -> {
            Map<String, Object> extra;
            if (bean instanceof Map){
                extra = (Map<String, Object>) bean;
            }else {
                extra = BeanUtil.beanToMap(bean);
            }

            if (extra.containsKey(SORTNUM)){
                extra.put(SORT,extra.get(SORTNUM));
                extra.remove(SORTNUM);
            }

            if(MapUtil.isNotEmpty(extra)){
                extra.forEach((k,v) -> tree.putExtra(StringUtil.underlineToHump(k),v));
            }
        });
    }
}
