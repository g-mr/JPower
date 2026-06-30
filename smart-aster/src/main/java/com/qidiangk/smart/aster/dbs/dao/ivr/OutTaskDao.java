package com.qidiangk.smart.aster.dbs.dao.ivr;

import com.mybatisflex.core.query.QueryMethods;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.OutCallStatusEnum;
import com.qidiangk.smart.aster.constants.TaskStatusEnum;
import com.qidiangk.smart.aster.dbs.entity.ivr.OutTaskCallDO;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.aster.dbs.dao.ivr.mapper.OutTaskMapper;
import com.qidiangk.smart.aster.dbs.entity.ivr.OutTaskDO;
import top.jpower.core.dbs.support.Wrappers;

import java.util.List;

/**
 * @author mr.g
 */
@Repository
public class OutTaskDao extends JpowerServiceImpl<OutTaskMapper, OutTaskDO> {

    public List<OutTaskDO> listIdTime(){
        return getMapper().selectListIdTimes(Wrappers.getQueryWrapper()
                .select(OutTaskDO::getId, OutTaskDO::getTimes, OutTaskDO::getWeeks)
                .eq(OutTaskDO::getStatus, TaskStatusEnum.ENABLE.getStatus())
                .and(QueryMethods.exists(QueryMethods.selectOne().from(OutTaskCallDO.class).where(OutTaskCallDO::getTaskId).eq(OutTaskDO::getId)
                        .and(and -> {
                            and.eq(OutTaskCallDO::getStatus, OutCallStatusEnum.UN_PLAYING.getValue())
                                    .or(or -> {
                                        or.eq(OutTaskDO::getIsReCall, 1)
                                                .eq(OutTaskCallDO::getStatus, OutCallStatusEnum.PLAYED.getValue())
                                                .and(OutTaskCallDO::getCount).lt(OutTaskDO::getReCallNum)
                                                .ne(OutTaskCallDO::getCallResult, CallHangStateEnum.NORMALLY.getValue());
                                    });
                        })))
                .orderBy(OutTaskDO::getPriorities).asc());
    }

}
