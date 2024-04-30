package top.jpower.jpower.service.role.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.common.enums.ConstantsEnum;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleFunctionMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.jpower.module.common.redis.RedisUtil;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.support.ChainMap;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.role.CoreFunctionService;
import top.jpower.jpower.service.role.CoreRoleFunctionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@AllArgsConstructor
@Service("coreRoleFunctionService")
public class CoreRoleFunctionServiceImpl extends BaseServiceImpl<TbCoreRoleFunctionMapper, TbCoreRoleFunction> implements CoreRoleFunctionService {

    public TbCoreRoleFunctionDao coreRoleFunctionDao;
    public CoreFunctionService coreFunctionService;
    public RedisUtil redisUtil;

    @Override
    public List<Map<String,Object>> selectRoleFunctionByRoleId(Long roleId) {
        return coreRoleFunctionDao.getBaseMapper().selectRoleFunctionByRoleId(roleId);
    }

    @Override
    public boolean addRoleFunctions(Long roleId, List<Long> funcIds, boolean isAutoSaveInterface) {

        //先删除角色原有权限
        coreRoleFunctionDao.removeRealByMap(ChainMap.<String,Object>create().put("role_id",roleId).build());

        //把下级的接口权限自动给
        if (isAutoSaveInterface){
            List<Long> fIds = coreFunctionService.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                    .select(TbCoreFunction::getId)
                    .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.INTERFACE.getValue())
                    .in(TbCoreFunction::getParentId, funcIds), Fc::toLong);
            if (Fc.isNotEmpty(fIds)){
                funcIds.addAll(fIds);
            }
        }

        List<TbCoreRoleFunction> roleFunctions = new ArrayList<>();
        if (Fc.isNotEmpty(funcIds)){
            for (Long fId : funcIds) {
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
