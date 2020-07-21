package com.wlcb.jpower.module.common.service.core.user.impl;

import com.wlcb.jpower.module.common.service.core.user.CoreRolefunctionService;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreRoleFunctionDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreRoleFunctionMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("coreRolefunctionService")
public class CoreRolefunctionServiceImpl implements CoreRolefunctionService {

    @Autowired
    public TbCoreRoleFunctionDao coreRoleFunctionDao;

    @Override
    public List<Map<String,Object>> selectRoleFunctionByRoleId(String roleId) {
        return coreRoleFunctionDao.getBaseMapper().selectRoleFunctionByRoleId(roleId);
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
        coreRoleFunctionDao.removeByMap(map);

        if (roleFunctions.size() > 0){
            boolean is = coreRoleFunctionDao.saveBatch(roleFunctions);
            return is?roleFunctions.size():0;
        }
        return 1;
    }
}
