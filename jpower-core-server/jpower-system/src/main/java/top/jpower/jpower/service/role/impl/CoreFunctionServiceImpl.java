package top.jpower.jpower.service.role.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import top.jpower.jpower.dbs.dao.client.TbCoreClientDao;
import top.jpower.jpower.dbs.dao.role.TbCoreFunctionDao;
import top.jpower.jpower.dbs.dao.role.TbCoreFunctionMenuDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreFunctionMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.function.TbCoreFunctionMenu;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.jpower.module.common.auth.RoleConstant;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.NacosUtil;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.common.utils.StringUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
import top.jpower.jpower.module.common.utils.constants.JpowerConstants;
import top.jpower.jpower.module.common.utils.constants.StringPool;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.module.mp.support.LambdaTreeWrapper;
import top.jpower.jpower.service.role.CoreFunctionService;
import top.jpower.jpower.vo.DataFunctionVo;
import top.jpower.jpower.vo.FunctionVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static top.jpower.jpower.module.config.BuiltController.PATH;

/**
 * @author mr.gmac
 */
@Service("coreFunctionService")
@AllArgsConstructor
public class CoreFunctionServiceImpl extends BaseServiceImpl<TbCoreFunctionMapper, TbCoreFunction> implements CoreFunctionService {

    private final String ROLE_SQL = "select function_id from tb_core_role_function where role_id in ({})";

    private RestTemplate restTemplate;
    private TbCoreFunctionDao coreFunctionDao;
    private TbCoreRoleFunctionDao coreRoleFunctionDao;
    private TbCoreFunctionMenuDao functionMenuDao;
    private TbCoreClientDao clientDao;

    @Override
    public List<Tree<String>> treeMenuTypeByClientId(List<String> roleIds, String clientId) {
        return coreFunctionDao.treeMenuTypeByClientId(roleIds, clientId);
    }

    @Override
    public List<FunctionVo> listFunction(Map<String,Object> coreFunction) {

        String functionType = null;
        if (coreFunction.containsKey("functionType_eq")){
            functionType = Fc.toStr(coreFunction.get("functionType_eq"));
        }

        String menuId = Fc.toStr(coreFunction.remove("menuId_eq"));

        LambdaQueryWrapper<TbCoreFunction> wrapper =
                Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                        .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,StringPool.SINGLE_QUOTE.concat(Fc.join(ShieldUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                        .inSql(Fc.isNotBlank(menuId),TbCoreFunction::getId,StringUtil.format("select function_id from tb_core_function_menu where menu_id = '{}'",menuId))
                        .orderByAsc(TbCoreFunction::getSort);

        return coreFunctionDao.getBaseMapper().listFunction(wrapper, functionType);
    }

    @Override
    public Boolean add(TbCoreFunction coreFunction) {
        return coreFunctionDao.save(coreFunction);
    }

    @Override
    public Boolean delete(String ids) {
        coreRoleFunctionDao.removeReal(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda().in(TbCoreRoleFunction::getFunctionId,Fc.toStrList(ids)));
        functionMenuDao.removeReal(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().eq(TbCoreFunctionMenu::getFunctionId,Fc.toStrList(ids)));
        return coreFunctionDao.removeRealByIds(Fc.toStrList(ids));
    }

    @Override
    public long listByPids(String ids) {
        return coreFunctionDao.count(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .in(TbCoreFunction::getParentId,Fc.toStrList(ids))
                .notIn(TbCoreFunction::getId,Fc.toStrList(ids)));
    }

    @Override
    public TbCoreFunction selectFunctionByCode(String code) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = new QueryWrapper<TbCoreFunction>().lambda();
        wrapper.eq(TbCoreFunction::getCode,code);
        return coreFunctionDao.getOne(wrapper);
    }

    @Override
    public TbCoreFunction selectFunctionByUrl(String url) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = new QueryWrapper<TbCoreFunction>().lambda();
        wrapper.eq(TbCoreFunction::getUrl,url);
        return coreFunctionDao.getOne(wrapper,false);
    }

    @Override
    public Boolean update(TbCoreFunction coreFunction) {

        TbCoreFunction function = coreFunctionDao.getById(coreFunction.getId());
        if (Fc.notNull(function) && !Fc.equalsValue(function.getParentId(),coreFunction.getParentId())){
            if (Fc.isNotBlank(coreFunction.getParentId()) && Fc.isBlank(function.getParentId())){
                functionMenuDao.removeReal(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().eq(TbCoreFunctionMenu::getFunctionId,function.getId()));
            }
        }

        return coreFunctionDao.updateById(coreFunction);
    }

    @Override
    public boolean saveHierarchy(String parentId, List<String> ids) {

        functionMenuDao.removeReal(Condition.<TbCoreFunctionMenu>getQueryWrapper()
                .lambda().in(TbCoreFunctionMenu::getFunctionId,ids));

        return coreFunctionDao.update(Wrappers.<TbCoreFunction>lambdaUpdate()
                .set(TbCoreFunction::getParentId, parentId)
                .in(TbCoreFunction::getId, ids));
    }

    /**
     * 查询菜单列表
     *
     * @param coreFunction
     * @return
     * @author mr.g
     **/
    @Override
    public List<DataFunctionVo> listDataFunction(Map<String, Object> coreFunction) {
        String menuId = Fc.toStr(coreFunction.remove("menuId_eq"));

        return coreFunctionDao.getBaseMapper().listDataFunction(Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,StringPool.SINGLE_QUOTE.concat(Fc.join(ShieldUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                .inSql(Fc.isNotBlank(menuId),TbCoreFunction::getId,StringUtil.format("select function_id from tb_core_function_menu where menu_id = '{}'",menuId))
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<String> queryUrlIdByRole(String roleIds) {
        return coreRoleFunctionDao.listObjs(Condition.<TbCoreRoleFunction>getQueryWrapper()
                .lambda()
                .select(TbCoreRoleFunction::getFunctionId)
                .in(TbCoreRoleFunction::getRoleId,Fc.toStrList(roleIds)),Fc::toStr);
    }

    @Override
    public List<Tree<String>> lazyTreeByRole(String parentId, List<String> roleIds) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        return coreFunctionDao.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .lazy(parentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getUrl,TbCoreFunction::getSort)
                .inSql(TbCoreFunction::getId, StringUtil.format(ROLE_SQL,inSql)));
    }

    @Override
    public List<String> getUrlsByRoleIds(List<String> roleIds, String clientCode) {
        String inSql = StringUtils.collectionToDelimitedString(roleIds, StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE);

        return coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getUrl)
                .isNotNull(TbCoreFunction::getUrl)
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(clientCode))
                .inSql(TbCoreFunction::getId,StringUtil.format(ROLE_SQL,inSql)),Fc::toStr).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<TbCoreFunction> listMenuByRoleId(List<String> roleIds, String clientCode, String topMenuId,boolean isHide) {

        if (Fc.isEmpty(roleIds)){
            return new ArrayList<>();
        }

        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        List<TbCoreFunction> list = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(clientCode))
                .eq(isHide, TbCoreFunction::getIsHide, ConstantsEnum.YN01.N.getValue())
                .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(ROLE_SQL,inSql))
                .orderByAsc(TbCoreFunction::getSort));

        //获取顶部菜单关联的左侧菜单
        if (Fc.isNotBlank(topMenuId)){
            Set<String> listId = new HashSet<>(functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,topMenuId), Fc::toStr));
            listId.addAll(findDescendants(list,listId));
            list = list.stream().filter(function -> listId.contains(function.getId())).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 查找子孙级别
     *
     * @author mr.g
     * @param listAll 全部功能
     * @param listId 一级功能ID
     * @return java.util.List<top.jpower.jpower.dbs.entity.function.TbCoreFunction>
     **/
    private Set<String> findDescendants(List<TbCoreFunction> listAll, Set<String> listId) {

        Set<String> childrenId = listAll.stream().filter(function -> listId.contains(function.getParentId())).map(TbCoreFunction::getId).collect(Collectors.toSet());

        if (Fc.isNotEmpty(childrenId)) {
            listId.addAll(findDescendants(listAll, childrenId));
        }

        return listId;
    }

    @Override
    public List<Tree<String>> menuTreeByRoleIds(List<String> roleIds, String clientId, String topMenuId) {
        LambdaTreeWrapper<TbCoreFunction> wrapper = Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getCode,TbCoreFunction::getUrl,TbCoreFunction::getSort)
                .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                // 如果不是超级用户，则查出自己权限的菜单
                .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));

        List<Tree<String>> list = coreFunctionDao.tree(wrapper.eq(TbCoreFunction::getClientId,clientId).orderByAsc(TbCoreFunction::getSort));

        if (Fc.isBlank(topMenuId)){
            return list;
        }

        List<String> functionIds = functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,topMenuId),Fc::toStr);
        if (Fc.isEmpty(functionIds)){
            return new ArrayList<>();
        }
        return list.stream().filter(tree->functionIds.contains(tree.getId())).collect(Collectors.toList());
    }

    @Override
    public List<TbCoreFunction> menuByRoleIds(List<String> roleIds) {
        if (Fc.isEmpty(roleIds)){
            return new ArrayList<>();
        }

        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                .func(q->{
                    if (!ShieldUtil.isRoot()){
                        q.inSql(TbCoreFunction::getId,StringUtil.format(ROLE_SQL, StringPool.SINGLE_QUOTE.concat(Fc.join(ShieldUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));
                    }
                })
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<String> listBtnByRoleId(List<String> roleIds, String topMenuId) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);

        String clientId = clientDao.queryIdByCode(ShieldUtil.getClientCode());

        Set<String> listId = new HashSet<>();
        if (Fc.isNotBlank(topMenuId)){
            List<TbCoreFunction> list = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                    .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                    .eq(TbCoreFunction::getClientId, clientId)
                    .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(ROLE_SQL,inSql))
                    .orderByAsc(TbCoreFunction::getSort));
            //获取顶部菜单关联的左侧菜单
            listId.addAll(functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,topMenuId), Fc::toStr));
            listId.addAll(findDescendants(list,listId));
        }

        return coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getCode)
                .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.BTN.getValue())
                .eq(TbCoreFunction::getClientId,clientId)
                .and(Fc.isNotEmpty(listId),q -> q.in(TbCoreFunction::getParentId,listId).or().eq(TbCoreFunction::getParentId, JpowerConstants.TOP_CODE))
                .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(ROLE_SQL,inSql)),Fc::toStr);
    }

    @Override
    public List<TbCoreFunction> listButByMenu(List<String> roleIds, String parentId, String clientId) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .eq(TbCoreFunction::getParentId,parentId)
                .ne(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                .orderByAsc(TbCoreFunction::getFunctionType);

        if (!ShieldUtil.isRoot()){
            // 如果不是超级用户，则查出自己权限的资源
            wrapper.inSql(TbCoreFunction::getId,StringUtil.format(ROLE_SQL,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));
        }

        return coreFunctionDao.list(wrapper.eq(TbCoreFunction::getClientId,clientId).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Tree<String>> listTreeByRoleId(List<String> roleIds) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        return coreFunctionDao.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(ShieldUtil.getClientCode()))
                .inSql(TbCoreFunction::getId, StringUtil.format(ROLE_SQL, inSql)).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public long queryRoleByUrl(String url) {
        TbCoreFunction function = selectFunctionByUrl(url);
        if (!Fc.isNull(function)){
            long roleCount = coreRoleFunctionDao.count(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda()
                    .eq(TbCoreRoleFunction::getRoleId, RoleConstant.ANONYMOUS_ID)
                    .eq(TbCoreRoleFunction::getFunctionId,function.getId()));
            return roleCount;
        }
        return 0;
    }

    @Override
    public boolean generateFunction() {
        List<String> servers = NacosUtil.getAllServers();
        if (Fc.isNotEmpty(servers)){
            //去请求拿到所有的功能点
            List<Map> list = servers.stream().map(name-> {
                try {
                    return restTemplate.getForObject("http://"+name+PATH,Map.class);
                }catch (Exception e){
                    return new HashMap();
                }
            }).collect(Collectors.toList());

            //拿到所有的菜单
            List<TbCoreFunction> menus = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda().eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue()));

            if (Fc.isNotEmpty(menus)){
                //拿到所有的code
                List<String> allCode = coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda().select(TbCoreFunction::getCode),Fc::toStr);

                //存储每个code的父级BTN的CODE
                Map<String,String> codeMap = new HashMap<>();

                //存储要保存的功能
                List<TbCoreFunction> functionList = new ArrayList<>();
                list.forEach(map->{
                    if (Fc.isNotEmpty(map)){
                        map.forEach((menuCode,functions)->{
                            Optional<TbCoreFunction> menu = menus.stream().filter(f->Fc.equalsValue(f.getCode(),menuCode)).findAny();
                            menu.ifPresent(tbCoreFunction -> ((List<Map<String,String>>)functions).forEach(fun -> {
                                String code = fun.get("code");
                                //只要不重复的code才去存储
                                if (functionList.stream().noneMatch(f -> Fc.equalsValue(f.getCode(), code)) && !allCode.contains(code)) {
                                    TbCoreFunction function = new TbCoreFunction();
                                    function.setCode(code);
                                    function.setFunctionName(fun.get("name"));
                                    function.setAlias(fun.get("alias"));
                                    function.setUrl(fun.get("url"));
                                    function.setClientId(tbCoreFunction.getClientId());
                                    if (Fc.isBlank(fun.get("btnCode"))){
                                        function.setParentId(tbCoreFunction.getId());
                                    }else {
                                        codeMap.put(code, fun.get("btnCode"));
                                    }
                                    function.setFunctionType(Fc.toInt(fun.get("type")));
                                    function.setTarget(ConstantsEnum.FUNCTION_TARGET.SELF.getValue());
                                    function.setIsHide(Boolean.FALSE);
                                    functionList.add(function);
                                }
                            }));
                        });
                    }
                });

                //去保存功能点
                if (Fc.isNotEmpty(functionList)){
                    coreFunctionDao.addBatchSomeColumn(functionList.stream().filter(f->Fc.isNotBlank(f.getParentId())).collect(Collectors.toList()));

                    List<TbCoreFunction> funcs = functionList.stream().filter(f->Fc.isBlank(f.getParentId())).collect(Collectors.toList());
                    if (Fc.isNotEmpty(funcs)){
                        Map<String,String> idCode = coreFunctionDao.selectIdByCode(new HashSet<>(codeMap.values()));
                        funcs = funcs.stream().peek(f-> f.setParentId(idCode.get(codeMap.get(f.getCode())))).collect(Collectors.toList());
                        coreFunctionDao.addBatchSomeColumn(funcs);
                    }
                }
            }
        }
        return true;
    }

}
