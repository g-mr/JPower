package top.jpower.system.service.role.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.system.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleFunctionMapper;
import top.jpower.system.dbs.entity.role.CoreRoleFunction;
import top.jpower.system.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.system.service.role.CoreFunctionService;
import top.jpower.system.service.role.CoreRoleFunctionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色功能服务实现
 * 
 * @author mr.g
 */
@AllArgsConstructor
@Service
public class CoreRoleFunctionServiceImpl extends BaseServiceImpl<CoreRoleFunctionMapper, CoreRoleFunction> implements CoreRoleFunctionService {

    public TbCoreRoleFunctionDao coreRoleFunctionDao;
    public CoreFunctionService coreFunctionService;

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
                    .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.INTERFACE.getValue())
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
