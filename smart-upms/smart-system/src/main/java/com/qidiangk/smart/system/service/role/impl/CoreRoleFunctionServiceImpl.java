package com.qidiangk.smart.system.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.qidiangk.smart.common.constants.CacheNames;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.system.dbs.dao.role.CoreFunctionDao;
import com.qidiangk.smart.system.dbs.dao.role.CoreRoleFunctionDao;
import com.qidiangk.smart.system.dbs.dao.role.mapper.CoreRoleFunctionMapper;
import com.qidiangk.smart.system.dbs.entity.role.CoreRole;
import com.qidiangk.smart.system.dbs.entity.role.CoreRoleFunction;
import com.qidiangk.smart.system.service.role.CoreRoleFunctionService;
import com.qidiangk.smart.system.service.role.CoreRoleService;
import com.qidiangk.smart.system.vo.RoleFunctionSaveVO;
import com.qidiangk.smart.system.vo.RoleFunctionVO;

import java.util.ArrayList;
import java.util.List;

import static com.qidiangk.smart.common.constants.ServiceCodeConstants.NOT_FOUND_DATA;
import static com.qidiangk.smart.common.constants.ServiceCodeConstants.SAVE_FAILURE;

/**
 * 角色功能服务实现
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CoreRoleFunctionServiceImpl extends BaseServiceImpl<CoreRoleFunctionMapper, CoreRoleFunction> implements CoreRoleFunctionService {

    public final CoreRoleFunctionDao coreRoleFunctionDao;
    public final CoreFunctionDao coreFunctionDao;
	public final CoreRoleService coreRoleService;

    @Override
    public List<RoleFunctionVO> selectRoleFunctionByRoleId(Long roleId) {
        return coreRoleFunctionDao.selectRoleFunctionByRoleId(roleId);
    }

    @Override
    public boolean addRoleFunctions(RoleFunctionSaveVO roleFunctionSaveVO) {

		JpowerAssert.notNull(coreRoleService.existsByField(CoreRole::getId, roleFunctionSaveVO.getRoleId()), JpowerError.NotFind, NOT_FOUND_DATA);

		//先删除角色原有权限
        coreRoleFunctionDao.removeRealByRoleId(roleFunctionSaveVO.getRoleId());

        //把下级的接口权限自动给
        if (roleFunctionSaveVO.getIsAutoSaveInterface() && Fc.isNotEmpty(roleFunctionSaveVO.getFunctionIds())){
            List<Long> fIds = coreFunctionDao.getIdByParentIdInterface(roleFunctionSaveVO.getFunctionIds());
            if (Fc.isNotEmpty(fIds)){
				roleFunctionSaveVO.getFunctionIds().addAll(fIds);
            }
        }

        List<CoreRoleFunction> roleFunctions = new ArrayList<>();
        if (Fc.isNotEmpty(roleFunctionSaveVO.getFunctionIds())){
            for (Long fId : roleFunctionSaveVO.getFunctionIds()) {
                CoreRoleFunction roleFunction = new CoreRoleFunction();
                roleFunction.setId(Fc.randomSnowFlakeId());
                roleFunction.setFunctionId(fId);
                roleFunction.setRoleId(roleFunctionSaveVO.getRoleId());
                roleFunctions.add(roleFunction);
            }
        }

        if (roleFunctions.size() > 0){
			coreRoleFunctionDao.saveBatch(roleFunctions);
        }


		if (coreRoleService.saveTopMenu(roleFunctionSaveVO.getRoleId(), roleFunctionSaveVO.getTopMenuIds())) {
			CacheUtil.clear(CacheNames.ROLE_KEY);
			CacheUtil.clear(CacheNames.FUNCTION_KEY);
			return true;
		}
		// 让事务进行回滚
		throw new JpowerException(SAVE_FAILURE);
    }

}
