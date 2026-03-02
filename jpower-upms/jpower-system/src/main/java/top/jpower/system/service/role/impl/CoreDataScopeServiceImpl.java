package top.jpower.system.service.role.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.enums.DataScopeTypeEnum;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.system.dbs.dao.client.CoreClientDao;
import top.jpower.system.dbs.dao.role.CoreDataScopeDao;
import top.jpower.system.dbs.dao.role.CoreRoleDao;
import top.jpower.system.dbs.dao.role.CoreRoleDataDao;
import top.jpower.system.dbs.dao.role.mapper.CoreDataScopeMapper;
import top.jpower.system.dbs.entity.function.CoreDataScope;
import top.jpower.system.dbs.entity.function.TbCoreDataScope;
import top.jpower.system.dbs.entity.role.CoreRole;
import top.jpower.system.dbs.entity.role.CoreRoleData;
import top.jpower.system.dbs.entity.role.TbCoreRoleData;
import top.jpower.system.service.role.CoreDataScopeService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static top.jpower.common.constants.ServiceCodeConstants.DATA_SCOPE_NOT_NULL;
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

    private final String sql = "select data_id from tb_core_role_data where role_id in ({})";

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
		JpowerAssert.notTrue(coreRoleDao.existsByField(CoreRole::getId, roleId), JpowerError.NotFind,NOT_FOUND_DATA);

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
    public List<TbCoreDataScope> getAllRoleDataScope() {
        return dataScopeDao.list(Condition.<TbCoreDataScope>getQueryWrapper().lambda().eq(TbCoreDataScope::getAllRole, YN01Enum.Y.getValue()));
    }

    @Override
    public List<TbCoreDataScope> getDataScopeByRole(List<Long> roleIds,String clientCode) {
        String inSql = StringUtils.collectionToCommaDelimitedString(roleIds);
        return dataScopeDao.list(Condition.<TbCoreDataScope>getQueryWrapper().lambda()
                .inSql(TbCoreDataScope::getMenuId,"select id from tb_core_function where client_id = " + clientDao.queryIdByCode(clientCode) + " and function_type = " + FunctionTypeEnum.MENU.getValue())
                .and(query-> query.inSql(TbCoreDataScope::getId, StringUtil.format(sql,inSql))
                        .or().eq(TbCoreDataScope::getAllRole,YN01Enum.Y.getValue())));
    }

    @Override
    public List<TbCoreDataScope> getDataScopeByRoleAndMenu(List<Long> roleIds,String menuCode) {
        String inSql = Fc.join(roleIds);
        List<TbCoreDataScope> list = dataScopeDao.list(Condition.<TbCoreDataScope>getQueryWrapper().lambda()
                .inSql(TbCoreDataScope::getMenuId,StringUtil.format("select id from tb_core_function where code = '{}'",menuCode))
                .and(query-> query.inSql(TbCoreDataScope::getId, StringUtil.format(sql,inSql)).or().eq(TbCoreDataScope::getAllRole, YN01Enum.Y.getValue())));
        return list.stream().sorted(Comparator.comparingInt(TbCoreDataScope::getAllRole)).collect(Collectors.toList());
    }

}
