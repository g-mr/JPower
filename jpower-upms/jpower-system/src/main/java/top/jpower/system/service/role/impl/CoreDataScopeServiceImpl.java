package top.jpower.system.service.role.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.system.dbs.dao.client.TbCoreClientDao;
import top.jpower.system.dbs.dao.role.TbCoreDataScopeDao;
import top.jpower.system.dbs.dao.role.TbCoreRoleDataDao;
import top.jpower.system.dbs.dao.role.mapper.CoreDataScopeMapper;
import top.jpower.system.dbs.entity.function.CoreDataScope;
import top.jpower.system.dbs.entity.function.TbCoreDataScope;
import top.jpower.system.dbs.entity.role.TbCoreRoleData;
import top.jpower.system.service.role.CoreDataScopeService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据权限业务实现
 * 
 * @author mr.g
 */
@Service
@AllArgsConstructor
public class CoreDataScopeServiceImpl extends BaseServiceImpl<CoreDataScopeMapper, CoreDataScope> implements CoreDataScopeService {

    private TbCoreDataScopeDao dataScopeDao;
    private TbCoreRoleDataDao roleDataDao;
    private TbCoreClientDao clientDao;

    private final String sql = "select data_id from tb_core_role_data where role_id in ({})";

    @Override
    public boolean save(TbCoreDataScope dataScope){
        return dataScopeDao.save(dataScope);
    }

    @Override
    public boolean roleDataScope(Long roleId, List<Long> dataIds) {

        roleDataDao.removeReal(Condition.<TbCoreRoleData>getQueryWrapper().lambda()
                                .eq(TbCoreRoleData::getRoleId,roleId));
        if (dataIds.size() > 0){
            List<TbCoreRoleData> list = new ArrayList<>();
            dataIds.forEach(dataId -> {
                TbCoreRoleData roleData = new TbCoreRoleData();
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
