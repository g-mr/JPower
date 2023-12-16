package top.jpower.jpower.service.role.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.jpower.jpower.dbs.dao.client.TbCoreClientDao;
import top.jpower.jpower.dbs.dao.role.TbCoreDataScopeDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleDataDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreDataScopeMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreDataScope;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleData;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.StringUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.common.utils.constants.StringPool;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.role.CoreDataScopeService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ding
 * @description 数据权限业务
 * @date 2020-11-03 15:02
 */
@Service
@AllArgsConstructor
public class CoreDataScopeServiceImpl extends BaseServiceImpl<TbCoreDataScopeMapper, TbCoreDataScope> implements CoreDataScopeService {

    private TbCoreDataScopeDao dataScopeDao;
    private TbCoreRoleDataDao roleDataDao;
    private TbCoreClientDao clientDao;

    private final String sql = "select data_id from tb_core_role_data where role_id in ({})";

    @Override
    public boolean save(TbCoreDataScope dataScope){
        return dataScopeDao.save(dataScope);
    }

    @Override
    public boolean roleDataScope(String roleId, String dataIds) {

        roleDataDao.removeReal(Condition.<TbCoreRoleData>getQueryWrapper().lambda()
                                .eq(TbCoreRoleData::getRoleId,roleId));
        List<String> dataScopeIds = Fc.toStrList(dataIds);
        if (dataScopeIds.size() > 0){
            List<TbCoreRoleData> list = new ArrayList<>();
            dataScopeIds.forEach(dataId -> {
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
        return dataScopeDao.list(Condition.<TbCoreDataScope>getQueryWrapper().lambda().eq(TbCoreDataScope::getAllRole, ConstantsEnum.YN01.Y.getValue()));
    }

    @Override
    public List<TbCoreDataScope> getDataScopeByRole(List<String> roleIds,String clientCode) {
        String inSql = StringUtils.collectionToDelimitedString(roleIds, StringPool.COMMA, StringPool.SINGLE_QUOTE, StringPool.SINGLE_QUOTE);
        return dataScopeDao.list(Condition.<TbCoreDataScope>getQueryWrapper().lambda()
                .inSql(TbCoreDataScope::getMenuId,"select id from tb_core_function where client_id = '" + clientDao.queryIdByCode(clientCode) + "' and function_type = " + ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                .and(query-> query.inSql(TbCoreDataScope::getId, StringUtil.format(sql,inSql))
                        .or().eq(TbCoreDataScope::getAllRole,ConstantsEnum.YN01.Y.getValue())));
    }

}
