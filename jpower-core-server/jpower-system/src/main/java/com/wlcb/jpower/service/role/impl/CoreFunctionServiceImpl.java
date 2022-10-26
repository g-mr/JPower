package com.wlcb.jpower.service.role.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.mp.support.LambdaTreeWrapper;
import com.wlcb.jpower.service.role.CoreFunctionService;
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

    private final String sql = "select function_id from tb_core_role_function where role_id in ({})";

    private RestTemplate restTemplate;
    private TbCoreFunctionDao coreFunctionDao;
    private TbCoreRoleFunctionDao coreRoleFunctionDao;
    private TbCoreFunctionMenuDao functionMenuDao;
    private TbCoreClientDao clientDao;

    @Override
    public List<FunctionVo> listFunction(Map<String,Object> coreFunction) {

        String isMenu = null;
        if (coreFunction.containsKey("isMenu_eq")){
            isMenu = Fc.toStr(coreFunction.get("isMenu_eq"));
        }

        LambdaQueryWrapper<TbCoreFunction> wrapper = ShieldUtil.isRoot() ?
                Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                        .orderByAsc(TbCoreFunction::getSort) :
                Condition.getQueryWrapper(coreFunction,TbCoreFunction.class).lambda()
                        .inSql(TbCoreFunction::getId,StringUtil.format(sql,StringPool.SINGLE_QUOTE.concat(Fc.join(ShieldUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)))
                        .orderByAsc(TbCoreFunction::getSort);

        return coreFunctionDao.getBaseMapper().listFunction(wrapper, isMenu);
    }

    @Override
    public Boolean add(TbCoreFunction coreFunction) {
        return coreFunctionDao.save(coreFunction);
    }

    @Override
    public Boolean delete(String ids) {
        coreRoleFunctionDao.removeReal(Condition.<TbCoreRoleFunction>getQueryWrapper().lambda().in(TbCoreRoleFunction::getFunctionId,Fc.toStrList(ids)));
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
        return coreFunctionDao.updateById(coreFunction);
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
                .inSql(TbCoreFunction::getId, StringUtil.format(sql,inSql)));
    }

    @Override
    public List<String> getUrlsByRoleIds(List<String> roleIds, String clientCode) {
        String inSql = StringUtils.collectionToDelimitedString(roleIds, StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE);

        return coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getUrl)
                .isNotNull(TbCoreFunction::getUrl)
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(clientCode))
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)),Fc::toStr).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<TbCoreFunction> listMenuByRoleId(List<String> roleIds, String clientCode, String topMenuId) {

        if (Fc.isEmpty(roleIds)){
            return new ArrayList<>();
        }

        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        List<TbCoreFunction> list = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(clientCode))
                .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(sql,inSql))
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
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreFunction>
     **/
    private Set<String> findDescendants(List<TbCoreFunction> listAll, Set<String> listId) {

        Set<String> childrenId = listAll.stream().filter(function -> listId.contains(function.getParentId())).map(TbCoreFunction::getId).collect(Collectors.toSet());

        if (Fc.isNotEmpty(childrenId)) {
            listId.addAll(findDescendants(listAll, childrenId));
        }

        return listId;
    }

    @Override
    public List<Tree<String>> menuTreeByRoleIds(List<String> roleIds,String clientId) {
        LambdaTreeWrapper<TbCoreFunction> wrapper = Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .select(TbCoreFunction::getFunctionName,TbCoreFunction::getCode,TbCoreFunction::getUrl)
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue());

        if (!ShieldUtil.isRoot()){
            // 如果不是超级用户，则查出自己权限的菜单
            wrapper.inSql(TbCoreFunction::getId,StringUtil.format(sql,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));
        }
        return coreFunctionDao.tree(wrapper.eq(TbCoreFunction::getClientId,clientId).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<TbCoreFunction> menuByRoleIds(List<String> roleIds) {
        if (Fc.isEmpty(roleIds)){
            return new ArrayList<>();
        }

        return coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                .func(q->{
                    if (!ShieldUtil.isRoot()){
                        q.inSql(TbCoreFunction::getId,StringUtil.format(sql, StringPool.SINGLE_QUOTE.concat(Fc.join(ShieldUtil.getUserRole(),StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));
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
                    .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue())
                    .eq(TbCoreFunction::getClientId, clientId)
                    .inSql(!ShieldUtil.isRoot(), TbCoreFunction::getId,StringUtil.format(sql,inSql))
                    .orderByAsc(TbCoreFunction::getSort));
            //获取顶部菜单关联的左侧菜单
            listId.addAll(functionMenuDao.listObjs(Condition.<TbCoreFunctionMenu>getQueryWrapper().lambda().select(TbCoreFunctionMenu::getFunctionId).eq(TbCoreFunctionMenu::getMenuId,topMenuId), Fc::toStr));
            listId.addAll(findDescendants(list,listId));
        }

        return coreFunctionDao.listObjs(Condition.<TbCoreFunction>getQueryWrapper().lambda()
                .select(TbCoreFunction::getCode)
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.N.getValue())
                .eq(TbCoreFunction::getClientId,clientId)
                .and(Fc.isNotEmpty(listId),q -> q.in(TbCoreFunction::getParentId,listId).or().eq(TbCoreFunction::getParentId, JpowerConstants.TOP_CODE))
                .inSql(TbCoreFunction::getId,StringUtil.format(sql,inSql)),Fc::toStr);
    }

    @Override
    public List<TbCoreFunction> listButByMenu(List<String> roleIds, String parentId, String clientId) {
        LambdaQueryWrapper<TbCoreFunction> wrapper = Condition.<TbCoreFunction>getQueryWrapper()
                .lambda()
                .eq(TbCoreFunction::getParentId,parentId)
                .eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.N.getValue());

        if (!ShieldUtil.isRoot()){
            // 如果不是超级用户，则查出自己权限的资源
            wrapper.inSql(TbCoreFunction::getId,StringUtil.format(sql,StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE)));
        }

        return coreFunctionDao.list(wrapper.eq(TbCoreFunction::getClientId,clientId).orderByAsc(TbCoreFunction::getSort));
    }

    @Override
    public List<Tree<String>> listTreeByRoleId(List<String> roleIds) {
        String inSql = StringPool.SINGLE_QUOTE.concat(Fc.join(roleIds,StringPool.SINGLE_QUOTE_CONCAT)).concat(StringPool.SINGLE_QUOTE);
        return coreFunctionDao.tree(Condition.getLambdaTreeWrapper(TbCoreFunction.class,TbCoreFunction::getId,TbCoreFunction::getParentId)
                .eq(TbCoreFunction::getClientId,clientDao.queryIdByCode(ShieldUtil.getClientCode()))
                .inSql(TbCoreFunction::getId, StringUtil.format(sql, inSql)).orderByAsc(TbCoreFunction::getSort));
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
            List<TbCoreFunction> menus = coreFunctionDao.list(Condition.<TbCoreFunction>getQueryWrapper().lambda().eq(TbCoreFunction::getIsMenu, ConstantsEnum.YN01.Y.getValue()));

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
                                    function.setIsMenu(ConstantsEnum.YN01.N.getValue());
                                    function.setTarget(ConstantsEnum.FUNCTION_TARGET.SELF.getValue());
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
