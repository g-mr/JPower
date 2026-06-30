package com.qidiangk.smart.aster.service.asterisk;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import com.qidiangk.smart.aster.dbs.entity.asterisk.QueueMembersDO;
import com.qidiangk.smart.aster.dbs.entity.asterisk.QueuesDO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupAttendStatusVO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupAttendVO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupPageVO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupQueryVO;
import com.qidiangk.smart.aster.pojo.vo.home.NameSelectVO;

import java.util.List;

public interface IQueueService extends BaseService<QueuesDO> {

    Pg<GroupPageVO> page(GroupQueryVO groupQueryVO);

    Boolean addAttend(List<QueueMembersDO> list);

    Boolean delAttend(String queueName, String membername);

    List<GroupAttendVO> attendList(String queueName);

    Boolean updatePaused(GroupAttendStatusVO groupAttendStatusVO);

    void deleteMember(String interfaceName);

    void deleteMemberByQueue(String name);

    boolean isAllPaused(String memberId);

    List<NameSelectVO> select();

    /**
     * 判断队列成员是否暂停
     *
     * @param queueName 队列名称
     * @param memberName 成员名称
     * @return 是否暂停
     */
    boolean isPaused(String queueName, String memberName);

}
