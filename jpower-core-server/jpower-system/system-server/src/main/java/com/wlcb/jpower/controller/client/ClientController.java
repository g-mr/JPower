package com.wlcb.jpower.controller.client;

import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.controller.BaseController;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.utils.CacheUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.client.CoreClientService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "客户端管理")
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
    @ApiOperation("保存或者更新客户端信息")
    @PostMapping("save")
    public ResponseData save(TbCoreClient coreClient){

        if (Fc.isBlank(coreClient.getId())){
            JpowerAssert.notEmpty(coreClient.getClientCode(), JpowerError.Arg,"客户端Code不可为空");
            JpowerAssert.notEmpty(coreClient.getName(), JpowerError.Arg,"客户端名称不可为空");

            if (coreClientService.count(Condition.<TbCoreClient>getQueryWrapper().lambda().eq(TbCoreClient::getClientCode,coreClient.getClientCode())) > 0){
                return ReturnJsonUtil.busFail("该客户端已存在");
            }
        }else {
            //防止用户A在更新时，用户B做了删除操作
            TbCoreClient client =coreClientService.getById(coreClient.getId());
            if (Fc.isNull(client)){
                return ReturnJsonUtil.fail("该数据不存在");
            }

            if (Fc.isNotBlank(coreClient.getId())){
                TbCoreClient tbCoreClient = coreClientService.getOne(Condition.<TbCoreClient>getQueryWrapper().lambda().eq(TbCoreClient::getClientCode,coreClient.getClientCode()));
                if (!Fc.isNull(tbCoreClient) && !Fc.equals(tbCoreClient.getId(),client.getId())){
                    return ReturnJsonUtil.busFail("该客户端已存在");
                }
            }

        }

        CacheUtil.clear(CacheNames.SYSTEM_REDIS_CACHE);
        return ReturnJsonUtil.status(coreClientService.saveOrUpdate(coreClient));
    }

    @ApiOperation("删除客户端")
    @DeleteMapping("delete")
    public ResponseData delete(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        JpowerAssert.notEmpty(ids,JpowerError.Arg,"客户端主键不可为空");
        CacheUtil.clear(CacheNames.SYSTEM_REDIS_CACHE);
        return ReturnJsonUtil.status(coreClientService.removeByIds(Fc.toStrList(ids)));
    }

    @ApiOperation("分页查询客户端列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataType = "int",required = true),
            @ApiImplicitParam(name = "name",value = "客户端名称",paramType = "query"),
            @ApiImplicitParam(name = "clientCode",value = "客户端编码",paramType = "query")
    })
    @GetMapping("list")
    public ResponseData<PageInfo<TbCoreClient>> list(@ApiIgnore @RequestParam Map<String,Object> coreClient){
        PaginationContext.startPage();
        List<TbCoreClient> list = coreClientService.list(Condition.getQueryWrapper(coreClient,TbCoreClient.class).lambda().orderByAsc(TbCoreClient::getSortNum));
        return ReturnJsonUtil.ok("查询成功",new PageInfo<>(list));
    }

}
