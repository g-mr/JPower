package com.wlcb.jpower.service.role.impl;

import com.wlcb.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreRoleFunctionMapper;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.service.role.CoreRolefunctionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@AllArgsConstructor
@Service("coreRolefunctionService")
public class CoreRolefunctionServiceImpl extends BaseServiceImpl<TbCoreRoleFunctionMapper, TbCoreRoleFunction> implements CoreRolefunctionService {

    public TbCoreRoleFunctionDao coreRoleFunctionDao;
    public CoreFunctionService coreFunctionService;
    public RedisUtil redisUtil;

    @Override
    public List<Map<String,Object>> selectRoleFunctionByRoleId(Long roleId) {
        return coreRoleFunctionDao.getBaseMapper().selectRoleFunctionByRoleId(roleId);
    }

    @Override
    public boolean addRolefunctions(Long roleId, List<Long> functionIds) {

        //先删除角色原有权限
        coreRoleFunctionDao.removeRealByMap(ChainMap.<String,Object>create().put("role_id",roleId).build());

        List<TbCoreRoleFunction> roleFunctions = new ArrayList<>();
        if (Fc.isNotEmpty(functionIds)){
            for (Long fId : functionIds) {
                TbCoreRoleFunction roleFunction = new TbCoreRoleFunction();
                roleFunction.setId(Fc.randomSnowFlakeId());
                roleFunction.setFunctionId(fId);
                roleFunction.setRoleId(roleId);
                roleFunctions.add(roleFunction);
            }
        }

        if (roleFunctions.size() > 0){
            return coreRoleFunctionDao.saveBatch(roleFunctions);
        }
        return true;
    }

}
