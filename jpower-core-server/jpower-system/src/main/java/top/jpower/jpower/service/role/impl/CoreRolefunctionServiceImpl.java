package top.jpower.jpower.service.role.impl;

import top.jpower.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleFunctionMapper;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.jpower.module.common.redis.RedisUtil;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.support.ChainMap;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.constants.StringPool;
import top.jpower.jpower.service.role.CoreFunctionService;
import top.jpower.jpower.service.role.CoreRolefunctionService;
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
    public List<Map<String,Object>> selectRoleFunctionByRoleId(String roleId) {
        return coreRoleFunctionDao.getBaseMapper().selectRoleFunctionByRoleId(roleId);
    }

    @Override
    public boolean addRolefunctions(String roleId, String functionIds) {

        //先删除角色原有权限
        coreRoleFunctionDao.removeRealByMap(ChainMap.<String,Object>create().put("role_id",roleId).build());

        List<TbCoreRoleFunction> roleFunctions = new ArrayList<>();
        if (Fc.isNotBlank(functionIds)){
            for (String fId : functionIds.split(StringPool.COMMA)) {
                TbCoreRoleFunction roleFunction = new TbCoreRoleFunction();
                roleFunction.setId(Fc.randomUUID());
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
