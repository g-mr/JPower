package top.jpower.system.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.enums.DataScopeTypeEnum;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.api.dto.DataScopeDTO;
import top.jpower.system.dbs.dao.client.CoreClientDao;
import top.jpower.system.dbs.dao.role.CoreDataScopeDao;
import top.jpower.system.dbs.dao.role.CoreRoleDao;
import top.jpower.system.dbs.dao.role.CoreRoleDataDao;
import top.jpower.system.dbs.dao.role.mapper.CoreDataScopeMapper;
import top.jpower.system.dbs.entity.function.CoreDataScope;
import top.jpower.system.dbs.entity.role.CoreRole;
import top.jpower.system.dbs.entity.role.CoreRoleData;
import top.jpower.system.service.role.CoreDataScopeService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static top.jpower.common.constants.ServiceCodeConstants.DATA_SCOPE_NOT_NULL;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_CLIENT;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_DATA;

/**
 * 数据权限业务实现
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CoreDataScopeServiceImpl extends BaseServiceImpl<CoreDataScopeMapper, CoreDataScope> implements CoreDataScopeService {

	private final CoreDataScopeDao dataScopeDao;
    private final CoreRoleDataDao roleDataDao;
    private final CoreClientDao clientDao;
	private final CoreRoleDao coreRoleDao;

    @Override
    public Long create(CoreDataScope dataScope) {
		if (Fc.isBlank(dataScope.getScopeColumn())){
			dataScope.setScopeColumn(StringPool.ASTERISK);
		}
		if(Fc.isEmpty(dataScope.getScopeType())){
			dataScope.setScopeType(DataScopeTypeEnum.ALL.getValue());
		}
		if(Fc.isEmpty(dataScope.getAllRole())){
			dataScope.setScopeType(YN01Enum.N.getValue());
		}
		if (Fc.equals(dataScope.getScopeType(), DataScopeTypeEnum.CUSTOM.getValue())){
			JpowerAssert.notEmpty(dataScope.getScopeValue(), JpowerError.Arg,DATA_SCOPE_NOT_NULL);
		}

		CacheUtil.clear(CacheNames.DATASCOPE_KEY);

		dataScopeDao.save(dataScope);
        return dataScope.getId();
    }

    @Override
    public boolean roleDataScope(Long roleId, List<Long> dataIds) {
		JpowerAssert.isTrue(coreRoleDao.existsByField(CoreRole::getId, roleId), JpowerError.NotFind,NOT_FOUND_DATA);

		CacheUtil.clear(CacheNames.DATASCOPE_KEY);
		CacheUtil.clear(CacheNames.ROLE_KEY);

        roleDataDao.removeRealByRoleId(roleId);
        if (dataIds.size() > 0) {
            List<CoreRoleData> list = new ArrayList<>();
            dataIds.forEach(dataId -> {
				CoreRoleData roleData = new CoreRoleData();
                roleData.setDataId(dataId);
                roleData.setRoleId(roleId);
                list.add(roleData);
            });
            return roleDataDao.saveBatch(list);
        }

        return true;
    }

    @Override
    public List<DataScopeDTO> getDataScopeByRole(List<Long> roleIds, String clientCode) {
		Long clientId = clientDao.queryIdByCode(clientCode).orElseThrow(()->new JpowerException(JpowerError.NotFind.getCode(), NOT_FOUND_CLIENT));
        return dataScopeDao.getDataScopeByRole(roleIds, clientId, null);
    }

    @Override
    public List<DataScopeDTO> getDataScopeByRoleAndMenu(List<Long> roleIds, String menuCode) {
        List<DataScopeDTO> list = dataScopeDao.getDataScopeByRole(roleIds, null, menuCode);
        return list.stream().sorted(Comparator.comparingInt(DataScopeDTO::getAllRole)).collect(Collectors.toList());
    }

}
