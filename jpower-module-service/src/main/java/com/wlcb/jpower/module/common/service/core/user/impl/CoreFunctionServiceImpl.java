package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreFunctionDao;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleFunctionDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreFunctionMapper;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.dbs.vo.FunctionVo;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author mr.gmac
 */
@Service("coreFunctionService")
public class CoreFunctionServiceImpl extends BaseServiceImpl<TbCoreFunctionMapper,TbCoreFunction> implements CoreFunctionService {

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
    public List<Node> lazyTree(String parentId) {
        return coreFunctionDao.tree(Condition.getTreeWrapper(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getFunctionName,TbCoreFunction::getUrl).lazy(parentId).lambda().orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Node> lazyTreeByRole(String parentId, String roleIds) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
        return coreFunctionDao.tree(Condition.getTreeWrapper(TbCoreFunction::getId,TbCoreFunction::getParentId,TbCoreFunction::getFunctionName,TbCoreFunction::getUrl)
                .lazy(parentId).lambda()
                .inSql(TbCoreFunction::getId, StringUtil.format(sql,inSql))
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public void putRedisAllFunctionByRoles(List<String> roleIds, Long expiresIn, String accessToken) {
        String inSql = "'"+Fc.join(roleIds).replaceAll(",","','")+"'";
        List<Object> listUrl = coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getUrl)
                .isNotNull(TbCoreFunction::getUrl)
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)));
        redisUtil.set(CacheNames.TOKEN_URL_KEY+accessToken,listUrl,expiresIn, TimeUnit.SECONDS);
    }

    @Override
    public List<TbCoreFunction> listMenuByRoleId(String roleIds) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<TbCoreFunction> listBtnByRoleIdAndPcode(String roleIds, String id) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.N.getValue())
                .and(consumer -> consumer.eq(TbCoreFunction::getParentId, id).or(c
                        -> c.eq(TbCoreFunction::getParentId, JpowerConstants.TOP_CODE)))
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<FunctionVo> listTreeByRoleId(String roleIds) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
        return coreFunctionDao.listTree(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .inSql(TbCoreFunction::getId, StringUtil.format(sql, inSql)).orderByAsc(TbCoreFunction::getSort),FunctionVo.class);
    }

}
