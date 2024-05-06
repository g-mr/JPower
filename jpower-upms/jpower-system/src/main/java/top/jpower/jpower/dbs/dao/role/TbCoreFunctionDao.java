package top.jpower.jpower.dbs.dao.role;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import org.springframework.stereotype.Repository;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreFunctionMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<Tree<String>> treeMenuTypeByClientId(List<Long> roleIds, Long clientId) {
        return super.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                        .select(TbCoreFunction::getFunctionName,TbCoreFunction::getFunctionType,TbCoreFunction::getSort)
                        .in(TbCoreFunction::getFunctionType, ListUtil.of(FunctionTypeEnum.MENU.getValue(),FunctionTypeEnum.BTN.getValue()))
                        // 如果不是超级用户，则查出自己权限的菜单
                        .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId, StringUtil.format(ROLE_SQL, StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                        .eq(TbCoreFunction::getClientId,clientId)
                        .orderByAsc(TbCoreFunction::getSort));
    }

    /**
     * 获取功能的CODE和ID
     *
     * @author mr.g
     * @param codes CODE
     * @return code,id
     **/
    public Map<String, TbCoreFunction> selectIdByCode(Set<String> codes) {
        List<TbCoreFunction> functions = super.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getId,TbCoreFunction::getCode,TbCoreFunction::getAncestorId)
                .in(TbCoreFunction::getCode, codes));
        return functions.stream().collect(Collectors.toMap(TbCoreFunction::getCode, f->f));
    }

    public List<Map<String, Object>> listInterface(List<Long> roleIds, Long clientId) {
        List<Map<String, Object>> list = super.listMaps(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                        .select(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getCode,TbCoreFunction::getFunctionName,TbCoreFunction::getAlias,TbCoreFunction::getUrl,TbCoreFunction::getFunctionType)
                        .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.INTERFACE.getValue())
                        .eq(TbCoreFunction::getClientId,clientId)
                        .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId, StringUtil.format("select function_id from tb_core_role_function where role_id in ({})",StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE))));

        return list.stream().map(map-> MapUtil.edit(map, mp -> new Map.Entry<String, Object>() {
            @Override
            public String getKey() {
                return StringUtil.underlineToHump(mp.getKey());
            }

            @Override
            public Object getValue() {
                return mp.getValue();
            }

            @Override
            public Object setValue(Object value) {
                return mp.setValue(value);
            }

        })).collect(Collectors.toList());
    }

    public List<Long> queryIdByTopChild() {
        List<Long> functionIds = super.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda().select(TbCoreFunction::getId)
                .eq(TbCoreFunction::getParentId, Fc.toLong(JpowerConstants.TOP_CODE))
                .ne(TbCoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue()), Fc::toLong);

        String where = StringUtil.concat("ancestor_id REGEXP ",StringPool.SINGLE_QUOTE,StringPool.LEFT_BRACKET,StringUtil.join(functionIds, StringPool.SPILT),StringPool.RIGHT_BRACKET,StringPool.SINGLE_QUOTE);
        functionIds.addAll(super.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getId)
                .apply(where)));
        return functionIds;
    }
}


