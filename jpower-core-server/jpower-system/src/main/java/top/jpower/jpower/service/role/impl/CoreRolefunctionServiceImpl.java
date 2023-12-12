package top.jpower.jpower.service.role.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleFunctionMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.jpower.module.common.redis.RedisUtil;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.support.ChainMap;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.role.CoreFunctionService;
import top.jpower.jpower.service.role.CoreRolefunctionService;

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

        List<String> funcIds = Fc.toStrList(functionIds);

        //把下级的接口权限自动给
        List<String> fIds = coreFunctionService.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                        .select(TbCoreFunction::getId)
                        .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.INTERFACE.getValue())
                        .in(TbCoreFunction::getParentId, Fc.toStrList(functionIds)), Fc::toStr);
        if (Fc.isNotEmpty(fIds)){
            funcIds.addAll(fIds);
        }

        List<TbCoreRoleFunction> roleFunctions = new ArrayList<>();
        if (Fc.isNotBlank(functionIds)){
            for (String fId : funcIds) {
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
