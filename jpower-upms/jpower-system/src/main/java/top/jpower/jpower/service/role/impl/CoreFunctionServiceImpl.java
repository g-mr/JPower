package top.jpower.jpower.service.role.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import top.jpower.common.enums.FunctionTargetEnum;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.nacos.utils.NacosUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.dbs.dao.client.TbCoreClientDao;
import top.jpower.jpower.dbs.dao.role.TbCoreFunctionDao;
import top.jpower.jpower.dbs.dao.role.TbCoreFunctionMenuDao;
import top.jpower.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreFunctionMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.dbs.entity.function.TbCoreFunctionMenu;
import top.jpower.jpower.dbs.entity.role.TbCoreRoleFunction;
import top.jpower.jpower.module.annotation.Menu;
import top.jpower.jpower.module.common.auth.RoleConstant;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.ShieldUtil;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.module.mp.support.LambdaTreeWrapper;
import top.jpower.jpower.service.role.CoreFunctionService;
import top.jpower.jpower.vo.DataFunctionVo;
import top.jpower.jpower.vo.FunctionVo;

import java.util.*;
import java.util.stream.Collectors;

import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;
import static top.jpower.jpower.module.config.BuiltEndpoint.PATH;

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
    public List<Tree<String>> treeMenuTypeByClientId(List<Long> roleIds, Long clientId) {
        return coreFunctionDao.treeMenuTypeByClientId(roleIds, clientId);
    }

    @Override
    public List<FunctionVo> listFunction(Map<String,Object> coreFunction) {

        String functionType = null;
        if (coreFunction.containsKey("functionType_eq")){
            functionType = Fc.toStr(coreFunction.get("functionType_eq"));
        }

        Long menuId = Fc.toLong(coreFunction.remove("menuId_eq"));

        LambdaQueryWrapper<TbCoreFunction> wrapper =
                Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                        .inSql(Fc.notNull(menuId),TbCoreFunction::getId,StringUtil.format("select function_id from tb_core_function_menu where menu_id = {}",menuId))
                        .orderByAsc(TbCoreFunction::getSort);

        return coreFunctionDao.getBaseMapper().listFunction(wrapper, functionType);
    }


    @Override
    public Boolean add(TbCoreFunction coreFunction) {
        if (Fc.isNull(coreFunction.getParentId()) || Fc.equalsValue(coreFunction.getParentId(), TOP_CODE)){
            coreFunction.setParentId(Fc.toLong(TOP_CODE));
            coreFunction.setAncestorId(TOP_CODE);
        }else {
            coreFunction.setAncestorId(Fc.toStr(coreFunction.getParentId()).concat(StringPool.COMMA).concat(Fc.toStr(coreFunctionDao.getById(coreFunction.getParentId()).getAncestorId())));
        }
        return coreFunctionDao.save(coreFunction);
    }

    @Override
    public Boolean delete(List<Long> ids) {
        coreRoleFunctionDao.removeReal(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda().in(TbCoreRoleFunction::getFunctionId,ids));
        functionMenuDao.removeReal(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().in(TbCoreFunctionMenu::getFunctionId,ids));
        return coreFunctionDao.removeRealByIds(ids);
    }

    @Override
    public long listByPids(List<Long> ids) {
        return coreFunctionDao.count(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .in(TbCoreFunction::getParentId,ids)
                .notIn(TbCoreFunction::getId,ids));
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
            if (Fc.notNull(coreFunction.getParentId()) && Fc.isNull(function.getParentId())){
                functionMenuDao.removeReal(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().eq(TbCoreFunctionMenu::getFunctionId,function.getId()));
            }
        }

        if (Fc.notNull(coreFunction.getParentId())){
            coreFunction.setAncestorId(StringUtil.replace(function.getAncestorId(),Fc.toStr(function.getParentId()),Fc.toStr(coreFunction.getParentId())));
        }

        return coreFunctionDao.updateById(coreFunction);
    }

    @Override
    public boolean hierarchySave(Long parentId, List<Long> ids) {

        functionMenuDao.removeReal(Condition.<TbCoreFunctionMenu>getQueryWrapper()
                .lambda().in(TbCoreFunctionMenu::getFunctionId,ids));


        List<TbCoreFunction> list = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda().in(TbCoreFunction::getId,ids).or(or->{
            for (Long id : ids) {
                or.like(TbCoreFunction::getAncestorId, id).or();
            }
        }));

        TbCoreFunction parentFunction = coreFunctionDao.getById(parentId);

        List<Long> oldParents = list.stream().filter(f->ids.contains(f.getId())).map(TbCoreFunction::getParentId).collect(Collectors.toList());

        list.forEach(f->{
            if (ids.contains(f.getId())){
                f.setParentId(parentId);
                f.setAncestorId(parentFunction.getAncestorId().concat(StringPool.COMMA).concat(Fc.toStr(parentId)));
            }else {
                f.setAncestorId(StringUtil.replaceFirst(f.getAncestorId(), Fc.toStrList(Fc.join(oldParents)), Fc.toStr(parentId)));
            }
        });

        return coreFunctionDao.updateBatchById(list);
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
        Long menuId = Fc.toLong(coreFunction.remove("menuId_eq"));

        return coreFunctionDao.getBaseMapper().listDataFunction(Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,Fc.join(ShieldUtil.getUserRole())))
                .inSql(Fc.notNull(menuId),TbCoreFunction::getId,StringUtil.format("select function_id from tb_core_function_menu where menu_id = {}",menuId))
                .orderByAsc(TbCoreFunction::getSort));
    }

    /**
     * 客户端下的接口资源
     *
     * @param roleIds  角色ID
     * @param clientId 客户端ID
     * @return 接口资源
     * @author mr.g
     **/
    @Override
    public List<Map<String, Object>> listInterface(List<Long> roleIds, Long clientId) {
        return coreFunctionDao.listInterface(roleIds,clientId);
    }

    @Override
    public Set<Long> queryUrlIdByRole(List<Long> roleIds) {
        return new HashSet<>(coreRoleFunctionDao.listObjs(Condition.<TbCoreRoleFunction>getQueryWrapper()
                .lambda()
                .select(TbCoreRoleFunction::getFunctionId)
                .in(TbCoreRoleFunction::getRoleId,roleIds),Fc::toLong));
    }

    @Override
    public List<Tree<Long>> lazyTreeByRole(Long parentId, List<Long> roleIds) {
        return coreFunctionDao.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .lazy(parentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getUrl,TbCoreFunction::getSort)
                .inSql(TbCoreFunction::getId, StringUtil.format(ROLE_SQL,Fc.join(roleIds))));
    }

    @Override
    public List<String> getUrlsByRoleIds(List<Long> roleIds, String clientCode) {
        String inSql = StringUtils.collectionToCommaDelimitedString(roleIds);

        return coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getUrl)
                .isNotNull(TbCoreFunction::getUrl)
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(clientCode))
                .inSql(TbCoreFunction::getId,StringUtil.format(ROLE_SQL,inSql)),Fc::toStr).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<TbCoreFunction> listMenuByRoleId(List<Long> roleIds, String clientCode, Long topMenuId,boolean isHide) {

        if (Fc.isEmpty(roleIds)){
            return new ArrayList<>();
        }

        List<TbCoreFunction> list = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(clientCode))
                .eq(isHide, TbCoreFunction::getIsHide, YN01Enum.N.getValue())
                .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(ROLE_SQL, StringUtil.join(roleIds)))
                .orderByAsc(TbCoreFunction::getSort));

        //获取顶部菜单关联的左侧菜单
        if (Fc.notNull(topMenuId)){
            Set<Long> listId = new HashSet<>(functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,topMenuId), Fc::toLong));
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
    private Set<Long> findDescendants(List<TbCoreFunction> listAll, Set<Long> listId) {

        Set<Long> childrenId = listAll.stream().filter(function -> listId.contains(function.getParentId())).map(TbCoreFunction::getId).collect(Collectors.toSet());

        if (Fc.isNotEmpty(childrenId)) {
            listId.addAll(findDescendants(listAll, childrenId));
        }

        return listId;
    }

    @Override
    public List<Tree<Long>> menuTreeByRoleIds(List<Long> roleIds, Long clientId, Long topMenuId) {
        LambdaTreeWrapper<TbCoreFunction> wrapper = Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getCode,TbCoreFunction::getUrl,TbCoreFunction::getSort)
                .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
                // 如果不是超级用户，则查出自己权限的菜单
                .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,Fc.join(roleIds)));

        List<Tree<Long>> list = coreFunctionDao.tree(wrapper.eq(TbCoreFunction::getClientId,clientId).orderByAsc(TbCoreFunction::getSort));

        if (Fc.isNull(topMenuId)){
            return list;
        }

        List<Long> functionIds = functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,topMenuId),Fc::toLong);
        if (Fc.isEmpty(functionIds)){
            return new ArrayList<>();
        }
        return list.stream().filter(tree->functionIds.contains(tree.getId())).collect(Collectors.toList());
    }

    @Override
    public List<TbCoreFunction> menuByRoleIds(List<Long> roleIds) {
        if (Fc.isEmpty(roleIds)){
            return new ArrayList<>();
        }

        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue())
                .func(q->{
                    if (!ShieldUtil.isRoot()){
                        q.inSql(TbCoreFunction::getId,StringUtil.format(ROLE_SQL, Fc.join(ShieldUtil.getUserRole())));
                    }
                })
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<String> listBtnByRoleId(List<Long> roleIds) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);

        Long clientId = clientDao.queryIdByCode(ShieldUtil.getClientCode());

        return coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getCode)
                .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.BTN.getValue())
                .eq(TbCoreFunction::getClientId,clientId)
                .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(ROLE_SQL,inSql)),Fc::toStr);
    }

    @Override
    public List<Tree<String>> treeButByMenu(List<Long> roleIds, Long parentId, Long clientId) {

        List<String> parentIds = coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                        .select(TbCoreFunction::getId)
                        .eq(TbCoreFunction::getClientId,clientId)
                        .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.BTN.getValue())
                        .eq(TbCoreFunction::getParentId,parentId)
                        .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                ,Fc::toStr);
        if (Fc.isEmpty(parentIds)){
            return ListUtil.toList();
        }

        return coreFunctionDao.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class, TbCoreFunction::getId, TbCoreFunction::getParentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getAlias,TbCoreFunction::getCode,TbCoreFunction::getUrl,TbCoreFunction::getFunctionType)
                .eq(TbCoreFunction::getClientId,clientId)
                .eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.BTN.getValue())
                .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                .func(q->{
                    if (Fc.equalsValue(parentId, TOP_CODE)){
                        q.and(and->{
                            and.eq(TbCoreFunction::getParentId, parentId);
                            for (String pId: parentIds){
                                and.or(or->or.like(TbCoreFunction::getAncestorId, pId));
                            }
                        });
                    } else {
                        q.like(TbCoreFunction::getAncestorId,parentId);
                    }
                })
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Tree<Long>> listTreeByRoleId(List<Long> roleIds) {
        return coreFunctionDao.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(ShieldUtil.getClientCode()))
                .inSql(TbCoreFunction::getId, StringUtil.format(ROLE_SQL, Fc.join(roleIds))).orderByAsc(TbCoreFunction::getSort));
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
    @Transactional(rollbackFor = Exception.class)
    public boolean generateFunction() {
        List<String> servers = NacosUtil.getAllServers();
        if (Fc.isNotEmpty(servers)){
            //去请求拿到所有的功能点
            List<Map> list = servers.stream().map(name-> {
                try {
                    return restTemplate.getForObject("http://"+name+PATH, Map.class);
                }catch (Exception e){
                    return new HashMap();
                }
            }).collect(Collectors.toList());

            //拿到所有的菜单
            List<TbCoreFunction> menus = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda().eq(TbCoreFunction::getFunctionType, FunctionTypeEnum.MENU.getValue()));

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
                            menu.ifPresent(tbCoreFunction -> ((List<Map<String,Object>>)functions).forEach(fun -> {
                                String code = MapUtil.getStr(fun, "code");
                                //只要不重复的code才去存储
                                if (functionList.stream().noneMatch(f -> Fc.equalsValue(f.getCode(), code)) && !allCode.contains(code)) {
                                    TbCoreFunction function = new TbCoreFunction();
                                    function.setCode(code);
                                    function.setFunctionName(MapUtil.getStr(fun, "name"));
                                    function.setAlias(MapUtil.getStr(fun, "alias"));
                                    function.setUrl(MapUtil.getStr(fun, "url"));
                                    function.setClientId(tbCoreFunction.getClientId());
                                    if (Fc.isEmpty(fun.get("btnCode"))){
                                        function.setParentId(tbCoreFunction.getId());
                                        function.setAncestorId(Fc.toStr(tbCoreFunction.getAncestorId(), TOP_CODE).concat(StringPool.COMMA).concat(Fc.toStr(tbCoreFunction.getId())));
                                    }else {
                                        codeMap.put(code, MapUtil.getStr(fun, "btnCode"));
                                    }
                                    function.setFunctionType(Objects.requireNonNull(FunctionTypeEnum.getEnumByType(MapUtil.get(fun, "type", Menu.TYPE.class)), "未找到枚举[FunctionTypeEnum]").getValue());
                                    function.setTarget(FunctionTargetEnum.SELF.getValue());
                                    function.setIsHide(Boolean.FALSE);
                                    functionList.add(function);
                                }
                            }));
                        });
                    }
                });

                //去保存功能点
                if (Fc.isNotEmpty(functionList)){
                    List<TbCoreFunction> notNullFunctions = functionList.stream().filter(f->Fc.notNull(f.getParentId())).collect(Collectors.toList());
                    if (Fc.isNotEmpty(notNullFunctions)){
                        coreFunctionDao.addBatchSomeColumn(notNullFunctions);
                    }

                    List<TbCoreFunction> funcs = functionList.stream().filter(f->Fc.isNull(f.getParentId())).collect(Collectors.toList());
                    if (Fc.isNotEmpty(funcs)){
                        Map<String,TbCoreFunction> idCode = coreFunctionDao.selectIdByCode(new HashSet<>(codeMap.values()));
                        coreFunctionDao.addBatchSomeColumn(funcs.stream().peek(f-> {
                            TbCoreFunction parent = idCode.get(codeMap.get(f.getCode()));
                            f.setParentId(parent.getId());
                            f.setAncestorId(Fc.toStr(parent.getAncestorId(), TOP_CODE).concat(StringPool.COMMA).concat(Fc.toStr(parent.getId())));
                        }).collect(Collectors.toList()));
                    }
                }
            }
        }
        return true;
    }

}
