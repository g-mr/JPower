package com.qidiangk.smart.aster.service.asterisk;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import com.qidiangk.smart.aster.dbs.entity.asterisk.AuthsDO;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.pojo.vo.attend.AttendPageVO;
import com.qidiangk.smart.aster.pojo.vo.attend.AttendQueryVO;
import com.qidiangk.smart.aster.pojo.vo.attend.AttendVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineBaseVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineQueryVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineUpdateVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineVO;

import java.util.List;

public interface IPjSipService extends BaseService<EndpointsDO> {

    /**
     * 新建一个坐席
     * @param attendVO
     * @return
     */
    String createSip(AttendVO attendVO);

    /**
     * 修改坐席
     * @param attendVO
     * @return
     */
    String updateSip(AttendVO attendVO);

    /**
     * 删除坐席
     * @param id
     * @return
     */
    boolean delete(String id);

    Pg<AttendPageVO> page(AttendQueryVO attendQueryVO);

    List<AuthsDO> listUsernameByAuthId(List<String> memberIds);

    String createLine(LineBaseVO lineBaseVO);

    boolean removeLineById(String id);

    boolean updateLineById(LineUpdateVO lineVO);

    Pg<LineVO> pageLine(LineQueryVO lineQueryVO);

    String getLimit1Line();
}
