package com.qidiangk.smart.aster.service;

import com.qidiangk.smart.aster.pojo.vo.attend.*;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import top.jpower.core.util.rsp.Pg;
import com.qidiangk.smart.aster.pojo.vo.attend.*;
import com.qidiangk.smart.aster.pojo.vo.home.NameSelectVO;
import com.qidiangk.smart.aster.pojo.vo.ivr.QueueSelectVo;

import java.util.List;

public interface AttendService {
    boolean online(String memberName);

    boolean offline(String interfaceName);

    String create(AttendVO attendVO);

    String update(AttendVO attendVO);

    boolean delete(String id);

    Pg<AttendPageVO> page(AttendQueryVO attendQueryVO);

    String createGroup(GroupVO groupVO);

    Boolean updateGroup(GroupVO groupVO);

    Boolean deleteGroup(String name);

    Pg<GroupPageVO> pageGroup(GroupQueryVO groupQueryVO);

    Boolean addAttend(AttendGroupSaveVO attendGroupSaveVO);

    Boolean delAttend(String groupName, String membername);

    List<GroupAttendVO> attendList(String groupName);

    Boolean updateAttendStatus(GroupAttendStatusVO groupAttendStatusVO);

    List<String> attendOther(String groupName);

    List<NameSelectVO> getContactAttend();

    Flux<ServerSentEvent<String>> getAttendStatus(String memberId);

    List<String> selectAttend();

    boolean existsAttend(String id);

}
