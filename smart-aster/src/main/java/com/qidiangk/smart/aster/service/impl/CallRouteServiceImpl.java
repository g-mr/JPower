package com.qidiangk.smart.aster.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qidiangk.smart.aster.constants.CallRouteTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.constants.CallRouteProcessEnum;
import com.qidiangk.smart.aster.constants.StatusEnum;
import com.qidiangk.smart.aster.dbs.dao.ivr.CallRouteDao;
import com.qidiangk.smart.aster.dbs.dao.ivr.mapper.CallRouteMapper;
import com.qidiangk.smart.aster.dbs.entity.ivr.CallRouteDO;
import com.qidiangk.smart.aster.pojo.SelectVO;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.aster.pojo.vo.ivr.RoutePageReqVO;
import com.qidiangk.smart.aster.service.ICallRouteService;
import top.jpower.core.util.utils.JsonUtil;

import java.io.IOException;
import java.util.*;

import static com.qidiangk.smart.aster.dbs.entity.ivr.table.CallRouteDOTableDef.CALL_ROUTE_DO;

@Service
@RequiredArgsConstructor
public class CallRouteServiceImpl extends BaseServiceImpl<CallRouteMapper, CallRouteDO> implements ICallRouteService {

    private final CallRouteDao callRouteDao;
    private final ObjectMapper objectMapper;

    @Override
    public Pg<CallRouteDO> page(RoutePageReqVO routePageReqVO) {
        return callRouteDao.pg(Wrappers.getQueryWrapper()
                .like(CallRouteDO::getRouteName, routePageReqVO.getName(), Fc.isNotBlank(routePageReqVO.getName()))
                .like(CallRouteDO::getRouteCode, routePageReqVO.getCode(), Fc.isNotBlank(routePageReqVO.getCode()))
                .eq(CallRouteDO::getProcess, routePageReqVO.getProcess(), Fc.notNull(routePageReqVO.getProcess()))
                .eq(CallRouteDO::getType, routePageReqVO.getType(), Fc.notNull(routePageReqVO.getType()))
                .orderBy(CallRouteDO::getPriority).asc()
                .orderBy(CallRouteDO::getCreateTime).desc());
    }

    @Override
    @CacheEvict(cacheNames = "intent_deepseek", allEntries = true)
    public boolean flow(Long id, List<? extends UserIntent.Node> nodes) {
        // 保存路由
        CallRouteDO callRouteDo = getById(id);
        callRouteDo.setFlow(nodes);
        return updateById(callRouteDo);
    }

    @Override
    public List<? extends UserIntent.Node> flowView(Long id) {
        CallRouteDO callRouteDo = getById(id);
        JpowerAssert.notNull(callRouteDo, JpowerError.NotFind, "流程");
        return callRouteDo.getFlow();
    }

    @Override
    public Optional<CallRouteDO> getFlowById(Long routId, CallRouteProcessEnum processEnum) {
        Optional<CallRouteDO> optionalCallRouteDO = super.getOneOpt(Wrappers.getQueryWrapper()
                .eq(CallRouteDO::getId, routId)
                .eq(CallRouteDO::getProcess, processEnum.getValue())
                .eq(CallRouteDO::getStatus, StatusEnum.DISABLE.getStatus())
                .isNotNull(CallRouteDO::getFlow)
                .isNotNull(CallRouteDO::getType));
        return optionalCallRouteDO;
    }

    @Override
    public Optional<CallRouteDO> matching(String callerId, String extension, CallRouteProcessEnum processEnum) {

        List<CallRouteDO> list = super.list(Wrappers.getQueryWrapper()
                .eq(CallRouteDO::getProcess, processEnum.getValue())
                .eq(CallRouteDO::getStatus, StatusEnum.DISABLE.getStatus())
                .isNotNull(CallRouteDO::getFlow)
                .isNotNull(CallRouteDO::getType)
                .orderBy(CallRouteDO::getPriority).asc());

        return list.stream()
                .filter(callRouteDo -> ReUtil.isMatch(callRouteDo.getCallingReg(), callerId) && ReUtil.isMatch(callRouteDo.getCalledReg(), extension))
                .findFirst();
    }

    /**
     * 获取完整的路由流程
     *
     * @param id 路由ID
     * @return 完整流程
     */
    @Override
    public List<? extends UserIntent.Node> findCompleteFlow(Long id) {
        return findFlow(id);
    }

    @Override
    public List<SelectVO> select(Integer process) {
        return callRouteDao.listAs(Wrappers.getQueryWrapper()
                        .select(CALL_ROUTE_DO.ID.as(SelectVO::getId), CALL_ROUTE_DO.ROUTE_NAME.as(SelectVO::getName))
                        .eq(CallRouteDO::getProcess, process, Fc.notNull(process))
                        .eq(CallRouteDO::getStatus, StatusEnum.DISABLE.getStatus())
                        .eq(CallRouteDO::getType, CallRouteTypeEnum.MAIN.getValue())
                        .orderBy(CallRouteDO::getPriority).asc(),
                SelectVO.class);
    }

    @Override
    public List<CallRouteDO> childList() {
        return callRouteDao.list(Wrappers.getQueryWrapper()
                        .eq(CallRouteDO::getStatus, StatusEnum.DISABLE.getStatus())
                        .eq(CallRouteDO::getType, CallRouteTypeEnum.CHILDREN.getValue())
                        .orderBy(CallRouteDO::getPriority).asc()
                        .orderBy(CallRouteDO::getCreateTime).desc());
    }

    private List<? extends UserIntent.Node> findFlow(Long id) {
        Optional<Object> optionalFlow = super.getObjOpt(Wrappers.getQueryWrapper()
                .select(CallRouteDO::getFlow)
                .eq(CallRouteDO::getId, id));
        return optionalFlow.map(flow -> {
            return parseArray(flow.toString());
        }).orElse(null);
    }

    private List<? extends UserIntent.Node> parseArray(String text) {
        if (StrUtil.isEmpty(text)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(text, objectMapper.getTypeFactory().constructCollectionType(List.class, UserIntent.Node.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
