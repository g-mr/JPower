package com.qidiangk.smart.aster.service;

import com.qidiangk.smart.aster.pojo.vo.cdr.*;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallInfoDO;
import com.qidiangk.smart.aster.pojo.vo.cdr.*;

import java.util.List;

public interface ICallInfoService extends BaseService<CallInfoDO> {
    Pg<CallInfoVO> page(CallInfoQueryVO callInfoQueryVO);

    Pg<CallInfoAttendVO> callInfoAttend(CallInfoAttendQueryVO callInfoAttendQueryVO);

    Pg<TransferVO> transfer(TransferQueryVO transferQueryVO);

    /**
     * 查询指定坐席与指定电话号码的通话记录
     *
     * @param attendId  坐席ID
     * @param phone     电话号码
     * @return 通话记录列表
     */
    List<CallInfoByAttendPhoneVO> callInfoByAttendAndPhone(String attendId, String phone);

    /**
     * 查询机器人正在通话的数量
     *
     * @return 数量
     */
    Flux<ServerSentEvent<Long>> botNum();
}
