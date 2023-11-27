package com.wlcb.jpower.service.role.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wlcb.jpower.dbs.dao.client.TbCoreClientDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreFunctionDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreFunctionMenuDao;
import com.wlcb.jpower.dbs.dao.role.TbCoreRoleFunctionDao;
import com.wlcb.jpower.dbs.dao.role.mapper.TbCoreFunctionMapper;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunctionMenu;
import com.wlcb.jpower.dbs.entity.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.NacosUtil;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.mp.support.LambdaTreeWrapper;
import com.wlcb.jpower.service.role.CoreFunctionService;
import com.wlcb.jpower.vo.DataFunctionVo;
import com.wlcb.jpower.vo.FunctionVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.wlcb.jpower.module.config.BuiltController.PATH;

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
    public List<FunctionVo> listFunction(Map<String,Object> coreFunction) {

        String functionType = null;
        if (coreFunction.containsKey("functionType_eq")){
            functionType = Fc.toStr(coreFunction.get("functionType_eq"));
        }

        Long menuId = Fc.toLong(coreFunction.remove("menuId_eq"));

        LambdaQueryWrapper<TbCoreFunction> wrapper =
                Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                        .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,Fc.join(ShieldUtil.getUserRole())))
                        .inSql(Fc.notNull(menuId),TbCoreFunction::getId,StringUtil.format("select function_id from tb_core_function_menu where menu_id = {}",menuId))
                        .orderByAsc(TbCoreFunction::getSort);

        return coreFunctionDao.getBaseMapper().listFunction(wrapper, functionType);
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

        return coreFunctionDao.updateById(coreFunction);
    }

    @Override
    public boolean saveHierarchy(Long parentId, List<Long> ids) {

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
        Long menuId = Fc.toLong(coreFunction.remove("menuId_eq"));

        return coreFunctionDao.getBaseMapper().listDataFunction(Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                .inSql(!ShieldUtil.isRoot(),TbCoreFunction::getId,StringUtil.format(ROLE_SQL,Fc.join(ShieldUtil.getUserRole())))
                .inSql(Fc.notNull(menuId),TbCoreFunction::getId,StringUtil.format("select function_id from tb_core_function_menu where menu_id = {}",menuId))
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Long> queryUrlIdByRole(List<Long> roleIds) {
        return coreRoleFunctionDao.listObjs(Condition.<TbCoreRoleFunction>getQueryWrapper()
                .lambda()
                .select(TbCoreRoleFunction::getFunctionId)
                .in(TbCoreRoleFunction::getRoleId,roleIds),Fc::toLong);
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
                .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(clientCode))
                .eq(isHide, TbCoreFunction::getIsHide, ConstantsEnum.YN01.N.getValue())
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
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreFunction>
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
                .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
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
                .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                .func(q->{
                    if (!ShieldUtil.isRoot()){
                        q.inSql(TbCoreFunction::getId,StringUtil.format(ROLE_SQL, Fc.join(ShieldUtil.getUserRole())));
                    }
                })
                .orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<String> listBtnByRoleId(List<Long> roleIds, Long topMenuId) {

        Long clientId = clientDao.queryIdByCode(ShieldUtil.getClientCode());

        Set<Long> listId = new HashSet<>();
        if (Fc.notNull(topMenuId)){
            List<TbCoreFunction> list = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                    .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                    .eq(TbCoreFunction::getClientId, clientId)
                    .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(ROLE_SQL,Fc.join(roleIds)))
                    .orderByAsc(TbCoreFunction::getSort));
            //获取顶部菜单关联的左侧菜单
            listId.addAll(functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,topMenuId), Fc::toLong));
            listId.addAll(findDescendants(list,listId));
        }

        return coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getCode)
                .eq(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.BTN.getValue())
                .eq(TbCoreFunction::getClientId,clientId)
                .and(Fc.isNotEmpty(listId),q -> q.in(TbCoreFunction::getParentId,listId).or().eq(TbCoreFunction::getParentId, Fc.toLong(JpowerConstants.TOP_CODE)))
                .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(ROLE_SQL,Fc.join(roleIds))),Fc::toStr);
    }

    @Override
    public List<TbCoreFunction> listButByMenu(List<Long> roleIds, Long parentId, Long clientId) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .eq(TbCoreFunction::getParentId,parentId)
                .ne(TbCoreFunction::getFunctionType, ConstantsEnum.FUNCTION_TYPE.MENU.getValue())
                .orderByAsc(TbCoreFunction::getFunctionType);

        if (!ShieldUtil.isRoot()){
            // 如果不是超级用户，则查出自己权限的资源
            wrapper.inSql(TbCoreFunction::getId,Fc.join(roleIds));
        }

        return coreFunctionDao.list(wrapper.eq(TbCoreFunction::getClientId,clientId).orderByAsc(TbCoreFunction::getSort));
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
    public boolean generateFunction() {
        List<String> servers = NacosUtil.getAllServers();
        if (Fc.isNotEmpty(servers)){
            //去请求拿到所有的功能点
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
                                    function.setParentId(tbCoreFunction.getId());
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
                    return coreFunctionDao.addBatchSomeColumn(functionList);
                }
            }
        }
        return true;
    }

}
