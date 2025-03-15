package top.jpower.jpower.controller.client;

import cn.hutool.core.util.NumberUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.rsp.ResponseData;
import top.jpower.core.util.rsp.ReturnJsonUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.dbs.entity.client.TbCoreClient;
import top.jpower.core.dbs.annotation.Function;
import top.jpower.core.dbs.annotation.Menu;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.jpower.service.client.CoreClientService;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Api(tags = "客户端管理")
@RestController
@RequestMapping("/core/client")
@AllArgsConstructor
public class ClientController extends BaseController {

    private CoreClientService coreClientService;

    @GetMapping("test")
    public void test(){
        TbCoreClient client = new TbCoreClient();
        client.setName("1");
        client.setClientCode("2");
        client.setClientSecret("2");
        coreClientService.save(client);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 保存或者更新客户端信息
     * @Date 14:45 2020-07-31
     * @Param [coreClient]
     * @return top.jpower.jpower.module.base.vo.ResponseData
     **/
    @Function(value = "保存",menus = {
            @Menu(name = "编辑",client = "admin",menuCode = "SYSTEM_CLIENT",code = "SYSTEM_CLIENT_SAVE",type = Menu.TYPE.BTN),
            @Menu(name = "新增",client = "admin",menuCode = "SYSTEM_CLIENT",code = "SYSTEM_CLIENT_ADD",type = Menu.TYPE.BTN)
    })
    @ApiOperation("保存或者更新客户端信息")
    @PostMapping("save")
    public ResponseData save(TbCoreClient coreClient){

        if (Fc.isNull(coreClient.getId())){
            JpowerAssert.notEmpty(coreClient.getClientCode(), JpowerError.Arg,"客户端Code不可为空");
            JpowerAssert.notEmpty(coreClient.getName(), JpowerError.Arg,"客户端名称不可为空");
            JpowerAssert.notTrue(coreClient.getRefreshTokenValidity() <= coreClient.getAccessTokenValidity(),JpowerError.Arg,"刷新令牌时长不可小于令牌时长");


            if (coreClientService.count(Condition.<TbCoreClient>getQueryWrapper().lambda().eq(TbCoreClient::getClientCode,coreClient.getClientCode())) > 0){
                return ReturnJsonUtil.busFail("该客户端已存在");
            }
        }else {
            //防止用户A在更新时，用户B做了删除操作
            TbCoreClient client =coreClientService.getById(coreClient.getId());
            JpowerAssert.notNull(client, JpowerError.NotFind, "客户端");

            long refreshTokenValidity = Fc.isNull(coreClient.getRefreshTokenValidity())?client.getRefreshTokenValidity():coreClient.getRefreshTokenValidity();
            long accessTokenValidity = Fc.isNull(coreClient.getAccessTokenValidity())?client.getAccessTokenValidity():coreClient.getAccessTokenValidity();
            JpowerAssert.notTrue(refreshTokenValidity <= accessTokenValidity,JpowerError.Arg,"刷新令牌时长不可小于令牌时长");

            if (Fc.notNull(coreClient.getId())){
                Long id = coreClientService.getObj(Condition.<TbCoreClient>getQueryWrapper().lambda().select(TbCoreClient::getId).eq(TbCoreClient::getClientCode,coreClient.getClientCode()), Fc::toLong);
                if (Fc.notNull(id) && !NumberUtil.equals(id,client.getId())){
                    return ReturnJsonUtil.busFail("该客户端已存在");
                }
            }

        }

        CacheUtil.clear(CacheNames.CLIENT_KEY);
        return ReturnJsonUtil.status(coreClientService.saveOrUpdate(coreClient));
    }

    @Function(value = "删除",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_CLIENT",code = "SYSTEM_CLIENT_DELETE",type = Menu.TYPE.BTN)
    })
    @ApiOperation("删除客户端")
    @DeleteMapping("delete")
    public ResponseData delete(@ApiParam(value = "主键，多个逗号分割",required = true) @RequestParam String ids){
        JpowerAssert.notEmpty(ids,JpowerError.Arg,"客户端主键不可为空");
        CacheUtil.clear(CacheNames.CLIENT_KEY);
        return ReturnJsonUtil.status(coreClientService.removeByIds(Fc.toLongList(ids)));
    }

    @Function(value = "列表",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_CLIENT",code = "CLIENT_LIST",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("分页查询客户端列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "第几页",defaultValue = "1",paramType = "query",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页长度",defaultValue = "10",paramType = "query",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "name",value = "客户端名称",paramType = "query"),
            @ApiImplicitParam(name = "clientCode",value = "客户端编码",paramType = "query")
    })
    @GetMapping("list")
    public ResponseData<Pg<TbCoreClient>> list(@ApiIgnore @RequestParam Map<String,Object> coreClient){
        PaginationContext.startPage();
        List<TbCoreClient> list = coreClientService.list(Condition.getQueryWrapper(coreClient,TbCoreClient.class).lambda().orderByAsc(TbCoreClient::getSortNum));
        return ReturnJsonUtil.data(new PageInfo<>(list));
    }

    @Function(value = "客户端下拉",menus = {
            @Menu(client = "admin",menuCode = "SYSTEM_FUNCTION",code = "FUNCTION_CLIENT_SELECT",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_ROLE",btnCode = "SYSTEM_ROLE_SELECT_URL",code = "ROLE_CLIENT_SELECT",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_DATASCOPE",code = "DATASCOPE_CLIENT_SELECT",type = Menu.TYPE.INTERFACE),
            @Menu(client = "admin",menuCode = "SYSTEM_TOPMENU",code = "TOPMENU_CLIENT_SELECT",type = Menu.TYPE.INTERFACE)
    })
    @ApiOperation("下拉客户端列表")
    @GetMapping("selectList")
    public ResponseData<List<Map<String,Object>>> selectList(){

        return ReturnJsonUtil.data(coreClientService.listMaps(Condition.<TbCoreClient>getQueryWrapper()
                .lambda()
                .select(TbCoreClient::getId,TbCoreClient::getName)
                .orderByAsc(TbCoreClient::getSortNum)));
    }
}
