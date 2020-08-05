package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.service.redis.RedisUtils;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author mr.gmac
 */
@Service("coreFunctionService")
public class CoreFunctionServiceImpl extends BaseServiceImpl<TbCoreFunctionMapper,TbCoreFunction> implements CoreFunctionService {

    private final String sql = "select function_id from tb_core_role_function where role_id in ({})";

    private final String SEL_CODE_IN_IDS_SQL = "select code from tb_core_function where id in ({})";


    @Autowired
    private TbCoreFunctionDao coreFunctionDao;
    @Autowired
    private TbCoreRoleFunctionDao coreRoleFunctionDao;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public List<TbCoreFunction> listByParent(TbCoreFunction coreFunction) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = new QueryWrapper<TbCoreFunction>().lambda();

        wrapper.eq(TbCoreFunction::getStatus,1);

        if (StringUtils.isNotBlank(coreFunction.getAlias())){
            wrapper.eq(TbCoreFunction::getAlias,coreFunction.getAlias());
        }

        if (StringUtils.isNotBlank(coreFunction.getCode())){
            wrapper.eq(TbCoreFunction::getCode,coreFunction.getCode());
        }

        if (StringUtils.isNotBlank(coreFunction.getParentCode())){
            wrapper.eq(TbCoreFunction::getParentCode,coreFunction.getParentCode());
        }

        if (coreFunction.getIsMenu() != null){
            wrapper.eq(TbCoreFunction::getIsMenu,coreFunction.getIsMenu());
        }

        if (StringUtils.isNotBlank(coreFunction.getFunctionName())){
            wrapper.like(TbCoreFunction::getFunctionName,coreFunction.getFunctionName());
        }

        if (StringUtils.isNotBlank(coreFunction.getUrl())){
            wrapper.like(TbCoreFunction::getUrl,coreFunction.getUrl());
        }

        wrapper.orderByAsc(TbCoreFunction::getSort);

        return coreFunctionDao.list(wrapper);
    }

    @Override
    public Boolean add(TbCoreFunction coreFunction) {
        return coreFunctionDao.save(coreFunction);
    }

    @Override
    public Boolean delete(String ids) {
        coreRoleFunctionDao.remove(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda().in(TbCoreRoleFunction::getFunctionId,Fc.toStrList(ids)));
        return coreFunctionDao.removeByIds(Fc.toStrList(ids));
    }

    @Override
    public Integer listByPids(String ids) {
        String inSql = "'"+ids.replaceAll(",","','")+"'";
        return coreFunctionDao.count(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .inSql(TbCoreFunction::getParentCode,StringUtil.format(SEL_CODE_IN_IDS_SQL,inSql)));
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
    public List<Node> lazyTree(String parentCode) {
        return coreFunctionDao.tree(Condition.getTreeWrapper(TbCoreFunction::getCode,TbCoreFunction::getParentCode,TbCoreFunction::getFunctionName,TbCoreFunction::getUrl).lazy(parentCode).lambda().orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Node> lazyTreeByRole(String parentCode, String roleIds) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
        return coreFunctionDao.tree(Condition.getTreeWrapper(TbCoreFunction::getCode,TbCoreFunction::getParentCode,TbCoreFunction::getFunctionName,TbCoreFunction::getUrl)
                .lazy(parentCode).lambda()
                .inSql(TbCoreFunction::getId, StringUtil.format(sql,inSql))
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public void putRedisAllFunctionByRoles(List<String> roleIds, Long expiresIn, String accessToken) {
        String inSql = "'"+Fc.join(roleIds).replaceAll(",","','")+"'";
        List<Object> listUrl = coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getUrl)
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)));
        redisUtils.set(CacheNames.TOKEN_URL_KEY+accessToken,listUrl,expiresIn, TimeUnit.SECONDS);
    }

    @Override
    public List<TbCoreFunction> listMenuByRoleId(String roleIds) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<TbCoreFunction> listBtnByRoleIdAndPcode(String roleIds, String code) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.N.getValue())
                .and(consumer -> consumer.eq(TbCoreFunction::getParentCode, code).or(c
                        -> c.eq(TbCoreFunction::getParentCode, JpowerConstants.TOP_CODE)))
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<FunctionVo> listTreeByRoleId(String roleIds) {
        String inSql = "'"+roleIds.replaceAll(",","','")+"'";
        return coreFunctionDao.listTree(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .inSql(TbCoreFunction::getId, StringUtil.format(sql, inSql)).orderByAsc(TbCoreFunction::getSort),FunctionVo.class);
    }

}
