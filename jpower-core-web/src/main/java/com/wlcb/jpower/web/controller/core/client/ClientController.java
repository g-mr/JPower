package com.wlcb.jpower.web.controller.core.client;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.core.client.CoreClientService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.dbs.entity.core.client.TbCoreClient;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ClientController
 * @Description TODO 客户端控制器
 * @Author 郭丁志
 * @Date 2020-07-31 14:27
 * @Version 1.0
 */
@RestController
@RequestMapping("/core/client")
@AllArgsConstructor
public class ClientController extends BaseController {

    private CoreClientService coreClientService;

    /**
     * @Author 郭丁志
     * @Description //TODO 保存或者更新客户端信息
     * @Date 14:45 2020-07-31
     * @Param [coreClient]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @PostMapping("save")
    public ResponseData save(TbCoreClient coreClient){
        if (Fc.isBlank(coreClient.getId())){
            JpowerAssert.notEmpty(coreClient.getClientCode(), JpowerError.Arg,"客户端Code不可为空");
            JpowerAssert.notEmpty(coreClient.getName(), JpowerError.Arg,"客户端名称不可为空");
        }

        Integer c = coreClientService.count(Condition.<TbCoreClient>getQueryWrapper().lambda().eq(TbCoreClient::getClientCode,coreClient.getClientCode()));
        if (c > 0){
            return ReturnJsonUtil.busFail("该客户端已存在");
        }

        return ReturnJsonUtil.status(coreClientService.saveOrUpdate(coreClient));
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 删除客户端信息
     * @Date 14:52 2020-07-31
     * @Param [coreClient]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @DeleteMapping("delete")
    public ResponseData delete(String ids){
        JpowerAssert.notEmpty(ids,JpowerError.Arg,"客户端主键不可为空");
        return ReturnJsonUtil.status(coreClientService.removeRealByIds(Fc.toStrList(ids)));
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 分页查询客户端列表
     * @Date 14:54 2020-07-31
     * @Param [coreClient]
     * @return com.wlcb.jpower.module.base.vo.ResponseData
     **/
    @GetMapping("list")
    public ResponseData list(@RequestParam Map<String,Object> coreClient){
        PaginationContext.startPage();
        List<TbCoreClient> list = coreClientService.list(Condition.getQueryWrapper(coreClient,TbCoreClient.class).lambda().orderByAsc(TbCoreClient::getSortNum));
        return ReturnJsonUtil.ok("查询成功",new PageInfo<>(list));
    }

}
