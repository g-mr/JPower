package com.wlcb.jpower.service.role.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.dbs.dao.role.TbCoreFunctionDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreFunctionMapper;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.mp.support.LambdaTreeWrapper;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.vo.FunctionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.wlcb.jpower.module.common.utils.constants.JpowerConstants.TOP_CODE;

/**
 * @author mr.gmac
 */
@Service("coreFunctionService")
public class CoreFunctionServiceImpl extends BaseServiceImpl<TbCoreFunctionMapper, TbCoreFunction> implements CoreFunctionService {

    private final String sql = "select function_id from tb_core_role_function where role_id in ({})";

    @Autowired
    private TbCoreFunctionDao coreFunctionDao;
    @Autowired
    private TbCoreRoleFunctionDao coreRoleFunctionDao;

    @Override
    public List<FunctionVo> listFunction(Map<String,Object> coreFunction) {

        String isMenu = null;
        if (coreFunction.containsKey("isMenu_eq")){
            isMenu = Fc.toStr(coreFunction.get("isMenu_eq"));
        }

        LambdaQueryWrapper<TbCoreFunction> wrapper = SecureUtil.isRoot() ?
                Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                        .orderByAsc(TbCoreFunction::getSort) :
                Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                        .inSql(TbCoreFunction::getId,StringUtil.format(sql,StringPool.SINGLE_QUOTE.concat(Fc.join(SecureUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                        .orderByAsc(TbCoreFunction::getSort);

        return coreFunctionDao.getBaseMapper().listFunction(wrapper, isMenu);
    }

    @Override
    public Boolean add(TbCoreFunction coreFunction) {
        return coreFunctionDao.save(coreFunction);
    }

    @Override
    public Boolean delete(String ids) {
        coreRoleFunctionDao.removeReal(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda().in(TbCoreRoleFunction::getFunctionId,Fc.toStrList(ids)));
        return coreFunctionDao.removeRealByIds(Fc.toStrList(ids));
    }

    @Override
    public long listByPids(String ids) {
        return coreFunctionDao.count(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .in(TbCoreFunction::getParentId,Fc.toStrList(ids)));
    }

    @Override
    public TbCoreFunction selectFunctionByCode(String code) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = new QueryWrapper<TbCoreFunction>().lambda();
        wrapper.eq(TbCoreFunction::getCode,code);
        return coreFunctionDao.getOne(wrapper);
    }

    @Override
    public TbCoreFunction selectFunctionByUrl(String url) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = new QueryWrapper<TbCoreFunction>().lambda();
        wrapper.eq(TbCoreFunction::getUrl,url);
        return coreFunctionDao.getOne(wrapper,false);
    }

    @Override
    public Boolean update(TbCoreFunction coreFunction) {
        return coreFunctionDao.updateById(coreFunction);
    }

    @Override
    public List<String> queryUrlIdByRole(String roleIds) {
        return coreRoleFunctionDao.listObjs(Condition.<TbCoreRoleFunction>getQueryWrapper()
                .lambda()
                .select(TbCoreRoleFunction::getFunctionId)
                .in(TbCoreRoleFunction::getRoleId,Fc.toStrList(roleIds)),Fc::toStr);
    }

    @Override
    public List<Tree<String>> lazyTreeByRole(String parentId, List<String> roleIds) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
//        return coreFunctionDao.tree(Condition.getTreeWrapper(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getFunctionName,TbCoreFunction::getUrl)
//                .lazy(parentId).lambda()
//                .inSql(TbCoreFunction::getId, StringUtil.format(sql,inSql))
//                .orderByAsc(TbCoreFunction::getSort));
        return coreFunctionDao.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .lazy(parentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getUrl,TbCoreFunction::getSort)
                .inSql(TbCoreFunction::getId, StringUtil.format(sql,inSql)));
    }

    @Override
    public List<Object> getUrlsByRoleIds(List<String> roleIds) {
        String inSql = StringUtils.collectionToDelimitedString(roleIds, StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE);
        return coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getUrl)
                .isNotNull(TbCoreFunction::getUrl)
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)));
    }

    @Override
    public List<TbCoreFunction> listMenuByRoleId(List<String> roleIds) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Tree<String>> menuTreeByRoleIds(List<String> roleIds) {
//        LambdaQueryWrapper<TbCoreFunction> wrapper = Condition.getTreeWrapper(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getFunctionName,TbCoreFunction::getCode,TbCoreFunction::getUrl)
//                .lambda()
//                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue());
//
//        if (!SecureUtil.isRoot()){
//            // 如果不是超级用户，则查出自己权限的菜单
//            wrapper.inSql(TbCoreFunction::getId,StringUtil.format(sql,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));
//        }
//        return coreFunctionDao.tree(wrapper.orderByAsc(TbCoreFunction::getSort));
        LambdaTreeWrapper<TbCoreFunction> wrapper = Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getCode,TbCoreFunction::getUrl)
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue());

        if (!SecureUtil.isRoot()){
            // 如果不是超级用户，则查出自己权限的菜单
            wrapper.inSql(TbCoreFunction::getId,StringUtil.format(sql,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));
        }
        return coreFunctionDao.tree(wrapper.orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<TbCoreFunction> listBtnByRoleIdAndPcode(List<String> roleIds, String id) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.N.getValue())
                .and(consumer -> consumer.eq(TbCoreFunction::getParentId, id).or(c
                        -> c.eq(TbCoreFunction::getParentId, TOP_CODE)))
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)));
    }

    @Override
    public List<TbCoreFunction> listButByMenu(List<String> roleIds, String id) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .eq(TbCoreFunction::getParentId,id)
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.N.getValue());

        if (!SecureUtil.isRoot()){
            // 如果不是超级用户，则查出自己权限的资源
            wrapper.inSql(TbCoreFunction::getId,StringUtil.format(sql,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));
        }

        return coreFunctionDao.list(wrapper.orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Tree<String>> listTreeByRoleId(List<String> roleIds) {
//        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
//        List<FunctionVo> list = coreFunctionDao.listTree(Condition.<TbCoreFunction>getQueryWrapper().lambda()
//                .inSql(TbCoreFunction::getId, StringUtil.format(sql, inSql)).orderByAsc(TbCoreFunction::getSort),FunctionVo.class);
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        List<Tree<String>> list = coreFunctionDao.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .inSql(TbCoreFunction::getId, StringUtil.format(sql, inSql)).orderByAsc(TbCoreFunction::getSort));
        return list;
    }

    @Override
    public long queryRoleByUrl(String url) {
        TbCoreFunction function = selectFunctionByUrl(url);
        if (!Fc.isNull(function)){
            long roleCount = coreRoleFunctionDao.count(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda()
                    .eq(TbCoreRoleFunction::getRoleId, RoleConstant.ANONYMOUS_ID)
                    .eq(TbCoreRoleFunction::getFunctionId,function.getId()));
            return roleCount;
        }
        return 0;
    }

}
