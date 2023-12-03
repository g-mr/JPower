package com.wlcb.jpower.dbs.dao.role;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreFunctionMapper;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wlcb.jpower.module.common.utils.constants.JpowerConstants.TOP_CODE;

/**
 * @ClassName TbCoreFunctionDao
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 14:39
 * @Version 1.0
 */
@Repository
public class TbCoreFunctionDao extends JpowerServiceImpl<TbCoreFunctionMapper, TbCoreFunction> {

    private static final String ROLE_SQL = "select function_id from tb_core_role_function where role_id in ({})";

    public List<Tree<String>> treeMenuTypeByClientId(List<String> roleIds, String clientId) {
        return super.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                        .select(TbCoreFunction::getFunctionName,TbCoreFunction::getFunctionType,TbCoreFunction::getSort)
                        .and(and->{
                            and.and(q-> q.in(TbCoreFunction::getFunctionType, ListUtil.of(ConstantsEnum.FUNCTION_TYPE.MENU.getValue(),ConstantsEnum.FUNCTION_TYPE.BTN.getValue())).ne(TbCoreFunction::getParentId, TOP_CODE))
                            .or(or-> or.eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue()).eq(TbCoreFunction::getParentId, TOP_CODE));
                        })
                        // 如果不是超级用户，则查出自己权限的菜单
                        .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId, StringUtil.format(ROLE_SQL, StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                        .eq(TbCoreFunction::getClientId,clientId)
                        .orderByAsc(TbCoreFunction::getSort));
    }
}


