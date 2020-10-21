package com.wlcb.jpower.service.role.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.dbs.dao.role.TbCoreFunctionDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreFunctionMapper;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.vo.FunctionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<TbCoreFunction> listByParent(Map<String,Object> coreFunction) {

        return coreFunctionDao.list(Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                .orderByAsc(TbCoreFunction::getSort));
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
    public Integer listByPids(String ids) {
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
        return coreFunctionDao.getOne(wrapper);
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
    public List<Node> lazyTreeByRole(String parentId, List<String> roleIds) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        return coreFunctionDao.tree(Condition.getTreeWrapper(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getFunctionName,TbCoreFunction::getUrl)
                .lazy(parentId).lambda()
                .inSql(TbCoreFunction::getId, StringUtil.format(sql,inSql))
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Object> getUrlsByRoleIds(String roleIds) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
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
    public List<TbCoreFunction> listBtnByRoleIdAndPcode(List<String> roleIds, String id) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.N.getValue())
                .and(consumer -> consumer.eq(TbCoreFunction::getParentId, id).or(c
                        -> c.eq(TbCoreFunction::getParentId, JpowerConstants.TOP_CODE)))
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<FunctionVo> listTreeByRoleId(List<String> roleIds) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        return coreFunctionDao.listTree(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .inSql(TbCoreFunction::getId, StringUtil.format(sql, inSql)).orderByAsc(TbCoreFunction::getSort),FunctionVo.class);
    }

    @Override
    public Integer queryRoleByUrl(String url) {
        TbCoreFunction function = selectFunctionByUrl(url);
        if (!Fc.isNull(function)){
            Integer roleCount = coreRoleFunctionDao.count(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda()
                    .eq(TbCoreRoleFunction::getRoleId, RoleConstant.ANONYMOUS_ID)
                    .eq(TbCoreRoleFunction::getFunctionId,function.getId()));
            return roleCount;
        }
        return 0;
    }

}
