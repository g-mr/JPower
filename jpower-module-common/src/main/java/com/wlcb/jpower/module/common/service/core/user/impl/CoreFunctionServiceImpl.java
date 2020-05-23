package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wlcb.jpower.module.common.service.core.user.CoreFunctionService;
import com.wlcb.jpower.module.common.service.core.user.CoreRoleService;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreFunctionMapper;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleMapper;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRole;
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
public class CoreFunctionServiceImpl implements CoreFunctionService {

    @Autowired
    private TbCoreFunctionMapper coreFunctionMapper;

    @Override
    public List<TbCoreFunction> listByParent(TbCoreFunction coreFunction) {
        EntityWrapper wrapper = new EntityWrapper<TbCoreFunction>();

        wrapper.eq("status",1);

        if (StringUtils.isNotBlank(coreFunction.getAlias())){
            wrapper.eq("alias",coreFunction.getAlias());
        }

        if (StringUtils.isNotBlank(coreFunction.getParentId())){
            wrapper.eq("parent_id",coreFunction.getParentId());
        }else {
            wrapper.isNull("parent_id");
        }

        if (StringUtils.isNotBlank(coreFunction.getCode())){
            wrapper.eq("code",coreFunction.getCode());
        }

        if (StringUtils.isNotBlank(coreFunction.getParentCode())){
            wrapper.eq("parent_code",coreFunction.getParentCode());
        }else {
            wrapper.isNull("parent_code");
        }

        if (coreFunction.getIsMenu() != null){
            wrapper.eq("is_menu",coreFunction.getIsMenu());
        }

        if (StringUtils.isNotBlank(coreFunction.getFunctionName())){
            wrapper.like("function_name",coreFunction.getFunctionName());
        }

        if (StringUtils.isNotBlank(coreFunction.getUrl())){
            wrapper.like("url",coreFunction.getUrl());
        }

        wrapper.orderBy("sort",true);

        return coreFunctionMapper.selectList(wrapper);
    }

    @Override
    public Integer add(TbCoreFunction coreFunction) {
        coreFunction.setUpdateUser(coreFunction.getCreateUser());
        return coreFunctionMapper.insert(coreFunction);
    }

    @Override
    public Integer delete(String ids) {

        //这里还要删除和角色的关联信息



        return coreFunctionMapper.deleteBatchIds(new ArrayList<>(Arrays.asList(ids.split(","))));
    }

    @Override
    public Integer listByPids(String ids) {
        EntityWrapper wrapper = new EntityWrapper<TbCoreFunction>();

        wrapper.in("parent_id",ids);

        return coreFunctionMapper.selectCount(wrapper);
    }

    @Override
    public TbCoreFunction selectFunctionByCode(String code) {
        TbCoreFunction coreFunction = new TbCoreFunction();
        coreFunction.setCode(code);
        return coreFunctionMapper.selectOne(coreFunction);
    }

    @Override
    public TbCoreFunction selectFunctionByUrl(String url) {
        TbCoreFunction coreFunction = new TbCoreFunction();
        coreFunction.setUrl(url);
        return coreFunctionMapper.selectOne(coreFunction);
    }

    @Override
    public Integer update(TbCoreFunction coreFunction) {
        return coreFunctionMapper.updateById(coreFunction);
    }
}
