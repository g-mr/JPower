package com.qidiangk.smart.aster.service.asterisk.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.qidiangk.smart.common.enums.YN01Enum;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.dbs.dao.asterisk.QueueMembersDao;
import com.qidiangk.smart.aster.dbs.dao.asterisk.QueuesDao;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.QueuesMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.QueueMembersDO;
import com.qidiangk.smart.aster.dbs.entity.asterisk.QueuesDO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupAttendStatusVO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupAttendVO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupPageVO;
import com.qidiangk.smart.aster.pojo.vo.attend.GroupQueryVO;
import com.qidiangk.smart.aster.pojo.vo.home.NameSelectVO;
import com.qidiangk.smart.aster.service.asterisk.IQueueService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl extends BaseServiceImpl<QueuesMapper, QueuesDO> implements IQueueService {

    private final QueuesDao queuesDao;
    private final QueueMembersDao queueMembersDao;

    @Override
    public Pg<GroupPageVO> page(GroupQueryVO groupQueryVO) {
        return queuesDao.page(groupQueryVO);
    }

    @Override
    public Boolean addAttend(List<QueueMembersDO> list) {
        List<String> memberExists = queueMembersDao.objListAs(Wrappers.getQueryWrapper()
                .select(QueueMembersDO::getInterfaceName)
                .or(f->{
                    list.forEach(m->{
                        f.eq(QueueMembersDO::getQueueName, m.getQueueName()).eq(QueueMembersDO::getInterfaceName, m.getInterfaceName());
                    });
                }), String.class);
        // 过滤掉已经保存的
        List<QueueMembersDO> membersDOS = list.stream().filter(member -> !memberExists.contains(member.getInterfaceName())).toList();
        if (Fc.isNotEmpty(membersDOS)){
            return queueMembersDao.saveBatch(membersDOS);
        }
        return true;
    }

    @Override
    public Boolean delAttend(String queueName, String membername) {
        return queueMembersDao.remove(Wrappers.getQueryWrapper()
                .eq(QueueMembersDO::getQueueName, queueName)
                .eq(QueueMembersDO::getMembername, membername));
    }

    @Override
    public List<GroupAttendVO> attendList(String queueName) {
        List<QueueMembersDO> list = queueMembersDao.listByField(QueueMembersDO::getQueueName, queueName);
        return BeanUtil.copyToList(list, GroupAttendVO.class);
    }

    @Override
    public Boolean updatePaused(GroupAttendStatusVO groupAttendStatusVO) {
        return queueMembersDao.update(UpdateWrapper.of(QueueMembersDO.class).set(QueueMembersDO::getPaused, groupAttendStatusVO.getPaused()).toEntity(),
                Wrappers.getQueryWrapper()
                .eq(QueueMembersDO::getQueueName, groupAttendStatusVO.getGroupName(), Fc.isNotBlank(groupAttendStatusVO.getGroupName()))
                .eq(QueueMembersDO::getMembername, groupAttendStatusVO.getMembername()));
    }


    @Override
    public void deleteMember(String interfaceName) {
        queueMembersDao.remove(Wrappers.getQueryWrapper()
                .eq(QueueMembersDO::getInterfaceName, interfaceName));
    }

    @Override
    public void deleteMemberByQueue(String name) {
        queueMembersDao.remove(Wrappers.getQueryWrapper().eq(QueueMembersDO::getQueueName, name));
    }

    @Override
    public boolean isAllPaused(String memberId) {
        // 只要存在一个未暂停的，则说明不是所有暂停
        return !queueMembersDao.exists(Wrappers.getQueryWrapper()
                        .eq(QueueMembersDO::getPaused, YN01Enum.N.getValue())
                        .eq(QueueMembersDO::getInterfaceName, StrUtil.prependIfMissing(memberId, "PJSIP/")));
    }

    /**
     * 是否暂停
     * @param queueName
     * @param memberName
     * @return true:暂停 false:不暂停, 没查到就是暂停（代表是别的租户的数据）
     */
    @Override
    public boolean isPaused(String queueName, String memberName) {
        QueueMembersDO queueMembersDO = queueMembersDao.getOne(Wrappers.getQueryWrapper()
                .select(QueueMembersDO::getPaused)
                .eq(QueueMembersDO::getInterfaceName, memberName)
                .eq(QueueMembersDO::getQueueName, queueName));
        return queueMembersDO != null ? queueMembersDO.getPaused() : true;
    }

    @Override
    public List<NameSelectVO> select() {
        List<QueuesDO> list = super.list(Wrappers.getQueryWrapper().select(QueuesDO::getName, QueuesDO::getShowName));
        return list.stream().map(q -> new NameSelectVO(q.getName(), q.getShowName())).toList();
    }

}
