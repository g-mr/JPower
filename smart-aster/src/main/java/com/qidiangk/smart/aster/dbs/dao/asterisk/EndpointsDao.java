package com.qidiangk.smart.aster.dbs.dao.asterisk;

import com.mybatisflex.core.tenant.TenantManager;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import com.qidiangk.smart.aster.constants.EndpointsTypeEnum;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.EndpointsMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.pojo.vo.home.NameSelectVO;

import java.util.List;

import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.EndpointsDOTableDef.ENDPOINTS_DO;

/**
 *  Dao
 *
 * @author mr.g
 */
@Repository
public class EndpointsDao extends JpowerServiceImpl<EndpointsMapper, EndpointsDO> {
    public List<NameSelectVO> listName() {
        return super.listAs(Wrappers.getQueryWrapper()
                .select(ENDPOINTS_DO.ID.as(NameSelectVO::getName), ENDPOINTS_DO.CALLERID.as(NameSelectVO::getShowName))
                .eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.LINE.getName()), NameSelectVO.class);
    }

    public boolean existEndpoint(String endpointId) {
        return TenantManager.withoutTenantCondition(() -> super.exists(Wrappers.getQueryWrapper().eq(EndpointsDO::getId, endpointId)));
    }

    public String getLimit1Line() {
        return super.getObjAs(Wrappers.getQueryWrapper()
                .from(ENDPOINTS_DO)
                .select(EndpointsDO::getId)
                .eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.LINE.getName())
                .orderBy(EndpointsDO::getCreateTime)
                .asc()
                .limit(1), String.class);
    }
}
