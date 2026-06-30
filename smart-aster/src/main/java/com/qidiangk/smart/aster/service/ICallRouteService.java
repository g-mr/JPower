package com.qidiangk.smart.aster.service;

import com.qidiangk.smart.aster.constants.CallRouteProcessEnum;
import com.qidiangk.smart.aster.dbs.entity.ivr.CallRouteDO;
import com.qidiangk.smart.aster.pojo.SelectVO;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.aster.pojo.vo.ivr.RoutePageReqVO;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;

import java.util.List;
import java.util.Optional;

public interface ICallRouteService extends BaseService<CallRouteDO> {

    Pg<CallRouteDO> page(RoutePageReqVO routePageReqVO);

    boolean flow(Long id, List<? extends UserIntent.Node> nodes);

    List<? extends UserIntent.Node> flowView(Long id);

    boolean existsFlowById(Long routId, CallRouteProcessEnum processEnum);

    Optional<CallRouteDO> matching(String callerId, String extension, CallRouteProcessEnum processEnum);

    List<? extends UserIntent.Node> findCompleteFlow(Long id);

    List<SelectVO> select(Integer process);

    List<CallRouteDO> childList();
}
