package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreFunctionDao;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleFunctionDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreFunctionMapper;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.mp.support.Condition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mr.gmac
 */
@Service("coreFunctionService")
public class CoreFunctionServiceImpl extends JpowerServiceImpl<TbCoreFunctionMapper,TbCoreFunction> implements CoreFunctionService {

    private final String sql = "(select function_id from tb_core_role_function where role_id in ({}))";

    @Autowired
    private TbCoreFunctionDao coreFunctionDao;
    @Autowired
    private TbCoreRoleFunctionDao coreRoleFunctionDao;

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

        if (StringUtils.isNotBlank(coreFunction.getParentId())){
            wrapper.eq(TbCoreFunction::getParentId,coreFunction.getParentId());
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
    public Integer delete(String ids) {

        // TODO: 2020-07-03  这里还要删除和角色的关联信息



        return coreFunctionDao.removeByIds(new ArrayList<>(Arrays.asList(ids.split(","))))?1:0;
    }

    @Override
    public Integer listByPids(String ids) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = new QueryWrapper<TbCoreFunction>().lambda();

        wrapper.in(TbCoreFunction::getParentId,ids);

        return coreFunctionDao.count(wrapper);
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

}
