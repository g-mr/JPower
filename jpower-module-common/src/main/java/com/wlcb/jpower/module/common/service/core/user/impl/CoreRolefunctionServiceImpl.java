package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wlcb.jpower.module.common.service.core.user.CoreRolefunctionService;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleFunctionMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreUserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("coreRolefunctionService")
public class CoreRolefunctionServiceImpl implements CoreRolefunctionService {

    public TbCoreRoleFunctionMapper coreRoleFunctionMapper;

    @Override
    public TbCoreRoleFunction selectRoleFunctionByRoleId(String roleId) {
        return coreRoleFunctionMapper.selectRoleFunctionByRoleId(roleId);
    }

    @Override
    public Integer addRolefunctions(String roleId, String functionIds) {

        String[] fIds = functionIds.split(",");
        List<TbCoreRoleFunction> roleFunctions = new ArrayList<>();
        for (String fId : fIds) {
                TbCoreRoleFunction roleFunction = new TbCoreRoleFunction();
                roleFunction.setId(UUIDUtil.getUUID());
                roleFunction.setFunctionId(fId);
                roleFunction.setRoleId(roleId);
                roleFunctions.add(roleFunction);
        }

        //先删除角色原有权限
        Map<String,Object> map = new HashMap<>();
        map.put("role_id",roleId);
        coreRoleFunctionMapper.deleteByMap(map);

        if (roleFunctions.size() > 0){
            Integer count = coreRoleFunctionMapper.insertList(roleFunctions);
            return count;
        }
        return 1;
    }
}
