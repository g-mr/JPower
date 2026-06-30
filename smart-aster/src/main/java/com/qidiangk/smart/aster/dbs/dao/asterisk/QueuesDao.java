package com.qidiangk.smart.aster.dbs.dao.asterisk;

import com.mybatisflex.core.query.QueryMethods;
import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.QueuesMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.QueueMembersDO;
import com.qidiangk.smart.aster.dbs.entity.asterisk.QueuesDO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupPageVO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupQueryVO;

import static com.mybatisflex.core.query.QueryMethods.*;
import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.QueueMembersDOTableDef.QUEUE_MEMBERS_DO;
import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.QueuesDOTableDef.QUEUES_DO;

@Repository
public class QueuesDao extends JpowerServiceImpl<QueuesMapper, QueuesDO> {

    public Pg<GroupPageVO> page(GroupQueryVO groupQueryVO) {
        return getMapper().pageAs(PaginationContext.page(), Wrappers.getQueryWrapper()
                .select(QUEUES_DO.DEFAULT_COLUMNS)
                .select(column("q.member_num").as(GroupPageVO::getMemberNum))
                .leftJoin(select(QUEUE_MEMBERS_DO.QUEUE_NAME,
                                QueryMethods.count(QUEUE_MEMBERS_DO.INTERFACE_NAME).as(GroupPageVO::getMemberNum))
                        .from(QUEUE_MEMBERS_DO)
                        .groupBy(QueueMembersDO::getQueueName))
                    .as("q")
                    .on(QUEUE_MEMBERS_DO.as("q").QUEUE_NAME.eq(QUEUES_DO.NAME))
                .eq(QueuesDO::getStrategy, groupQueryVO.getStrategy(), Fc.isNotBlank(groupQueryVO.getStrategy()))
                .like(QueuesDO::getName, groupQueryVO.getName(), Fc.isNotBlank(groupQueryVO.getName()))
                .like(QueuesDO::getShowName, groupQueryVO.getShowName(), Fc.isNotBlank(groupQueryVO.getShowName())), GroupPageVO.class);
    }

}
