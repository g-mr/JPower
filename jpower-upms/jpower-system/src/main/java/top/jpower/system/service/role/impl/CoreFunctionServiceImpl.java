package top.jpower.system.service.role.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.enums.FunctionTargetEnum;
import top.jpower.common.enums.FunctionTypeEnum;
import top.jpower.core.auth.annotation.Menu;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.ForestNodeMerger;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.nacos.utils.NacosUtil;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.system.dbs.dao.client.CoreClientDao;
import top.jpower.system.dbs.dao.role.CoreFunctionDao;
import top.jpower.system.dbs.dao.role.CoreFunctionMenuDao;
import top.jpower.system.dbs.dao.role.CoreRoleFunctionDao;
import top.jpower.system.dbs.dao.role.mapper.CoreFunctionMapper;
import top.jpower.system.dbs.entity.function.CoreFunction;
import top.jpower.system.service.role.CoreFunctionService;
import top.jpower.system.vo.DataFunctionVO;
import top.jpower.system.vo.FunctionSimpleVO;
import top.jpower.system.vo.FunctionVO;
import top.jpower.system.vo.SelectIdNameVO;

import java.util.*;
import java.util.stream.Collectors;

import static top.jpower.common.constants.ServiceCodeConstants.CODE_EXIST;
import static top.jpower.common.constants.ServiceCodeConstants.DELETE_EXIST_CHILD;
import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_CLIENT;
import static top.jpower.core.auth.endpoint.BuiltEndpoint.PATH;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE_LONG;

/**
 * 功能服务实现
 * 
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class CoreFunctionServiceImpl extends BaseServiceImpl<CoreFunctionMapper, CoreFunction> implements CoreFunctionService {

    private final RestTemplate restTemplate;
    private final CoreFunctionDao coreFunctionDao;
    private final CoreRoleFunctionDao coreRoleFunctionDao;
    private final CoreFunctionMenuDao functionMenuDao;
    private final CoreClientDao clientDao;

    @Override
    public List<Tree<Long>> treeMenuTypeByClientId(List<Long> roleIds, Long clientId) {
		if (Fc.isEmpty(roleIds)) {
			return ListUtil.empty();
		}
        return coreFunctionDao.treeMenuTypeByClientId(roleIds, clientId);
    }

    @Override
    public List<FunctionVO> listFunction(Map<String,Object> map) {
        return coreFunctionDao.listFunction(map);
    }


    @Override
    public Long create(CoreFunction coreFunction) {
		coreFunction.setParentId(Fc.toLong(coreFunction.getParentId(), TOP_CODE_LONG));
		coreFunction.setIsHide(Fc.toBoolean(coreFunction.getIsHide(), Boolean.FALSE));

		JpowerAssert.notTrue(coreFunctionDao.existsByField(CoreFunction::getCode, coreFunction.getCode()), JpowerError.Business, CODE_EXIST);

        if (Fc.equalsValue(coreFunction.getParentId(), TOP_CODE_LONG)){
            coreFunction.setAncestorId(TOP_CODE);
        }else {
			String ancestorId = coreFunctionDao.selectAncestorIdById(coreFunction.getParentId());
            coreFunction.setAncestorId(Fc.toStr(coreFunction.getParentId()).concat(StringPool.COMMA).concat(ancestorId));
        }

        coreFunctionDao.save(coreFunction);
		CacheUtil.clear(CacheNames.FUNCTION_KEY);
		return coreFunction.getId();
    }

    @Override
    public Boolean delete(List<Long> ids) {
		JpowerAssert.notTrue(coreFunctionDao.existsInField(CoreFunction::getId, ids), JpowerError.Business, DELETE_EXIST_CHILD);

		CacheUtil.clear(CacheNames.FUNCTION_KEY);
        coreRoleFunctionDao.removeRealByFunctionId(ids);
        functionMenuDao.removeRealByFunctionId(ids);
        return coreFunctionDao.removeRealByIds(ids);
    }

    @Override
    public Boolean update(CoreFunction coreFunction) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(coreFunction.getCode())){
			CoreFunction function = coreFunctionDao.getOneByField(CoreFunction::getCode, coreFunction.getCode());
			if (function != null && !NumberUtil.equals(function.getId(),function.getId())){
				JpowerAssert.createException(JpowerError.Business, CODE_EXIST);
			}
		}

        CoreFunction function = coreFunctionDao.getById(coreFunction.getId());
        if (Fc.notNull(function) && Fc.notEqualsValue(function.getParentId(),coreFunction.getParentId())){
			// 如果是最高级菜单切换到其他菜单下面，就删除和顶级菜单的关联关系
            if (Fc.notNull(coreFunction.getParentId()) && Fc.notEqualsValue(coreFunction.getParentId(), TOP_CODE_LONG) && Fc.equalsValue(function.getParentId(), TOP_CODE_LONG)){
                functionMenuDao.removeRealByFunctionId(Collections.singletonList(function.getId()));
            }
        }

        if (Fc.notNull(coreFunction.getParentId())){
			String ancestorId = coreFunctionDao.selectAncestorIdById(coreFunction.getParentId());
			coreFunction.setAncestorId(Fc.toStr(coreFunction.getParentId()).concat(StringPool.COMMA).concat(ancestorId));
        }

		CacheUtil.clear(CacheNames.FUNCTION_KEY);
        return coreFunctionDao.updateById(coreFunction);
    }

    @Override
    public boolean saveHierarchy(Long parentId, List<Long> ids) {
        functionMenuDao.removeRealByFunctionId(ids);

        List<CoreFunction> list = coreFunctionDao.listDescendantsByIds(ids);
        CoreFunction parentFunction = coreFunctionDao.getById(parentId);
        List<Long> oldParents = list.stream().filter(f->ids.contains(f.getId())).map(CoreFunction::getParentId).collect(Collectors.toList());
        list.forEach(f->{
            if (ids.contains(f.getId())){
                f.setParentId(parentId);
                f.setAncestorId(parentFunction.getAncestorId().concat(StringPool.COMMA).concat(Fc.toStr(parentId)));
            }else {
                f.setAncestorId(StringUtil.replaceFirst(f.getAncestorId(), Fc.toStrList(Fc.join(oldParents)), Fc.toStr(parentId)));
            }
        });
        return coreFunctionDao.updateBatch(list);
    }

    /**
     * 查询菜单列表
     * 
     * @author mr.g
     * @param map 查询条件
     * @return 数据功能列表
     */
    @Override
    public List<DataFunctionVO> listDataFunction(Map<String, Object> map) {
        Long menuId = Fc.toLong(map.remove("menuId_eq"));
        return coreFunctionDao.listDataFunction(menuId, map, ShieldUtil.isRoot() ? null : ShieldUtil.getUserRole());
    }

    /**
     * 客户端下的接口资源
     * 
     * @author mr.g
     * @param roleIds 角色ID列表
     * @param clientId 客户端ID
     * @return 接口资源列表
     */
    @Override
    public List<FunctionSimpleVO> listInterface(List<Long> roleIds, Long clientId) {
        return coreFunctionDao.listInterface(roleIds,clientId);
    }

	@Override
	public List<SelectIdNameVO> selectByClientId(Long clientId) {
		return coreFunctionDao.selectByClientId(clientId);
	}

	@Override
	public Long getIdByCode(String code) {
		return coreFunctionDao.getIdByCode(code);
	}

	@Override
    public Set<Long> queryUrlIdByRole(List<Long> roleIds) {
        return new HashSet<>(coreRoleFunctionDao.listFunctionIdByRole(roleIds));
    }

    @Override
    public List<Tree<Long>> lazyTreeByRole(Long parentId, List<Long> roleIds) {
        return coreFunctionDao.lazyTreeByRoleIds(parentId, roleIds);
    }

    @Override
    public List<String> getUrlsByRoleIds(List<Long> roleIds, String clientCode) {
		Long clientId = clientDao.queryIdByCode(clientCode).orElseThrow(()->new JpowerException(JpowerError.NotFind.getCode(), NOT_FOUND_CLIENT));
        return coreFunctionDao.listUrlByRole(roleIds, clientId);
    }

    @Override
    public List<Tree<Long>> listMenuByRoleId(List<Long> roleIds, String clientCode, Long topMenuId,boolean isHide) {
        if (Fc.isEmpty(roleIds)){
            return ListUtil.empty();
        }

        List<Tree<Long>> list = coreFunctionDao.treeInfo(roleIds, null, clientDao.queryIdByCode(clientCode).orElseThrow(()->new JpowerException(JpowerError.NotFind.getCode(), NOT_FOUND_CLIENT)), null, FunctionTypeEnum.MENU, isHide);

        //获取顶部菜单关联的左侧菜单
        if (Fc.notNull(topMenuId) && Fc.isNotEmpty(list)){
            Set<Long> listId = new HashSet<>(functionMenuDao.listFunctionId(topMenuId));
            list = list.stream().filter(function -> listId.contains(function.getId())).toList();
        }
        return list;
    }

    @Override
    public List<Tree<Long>> menuTreeByRoleIds(List<Long> roleIds, Long clientId, Long topMenuId) {
        List<Tree<Long>> list = coreFunctionDao.treeMenu(roleIds, clientId);

        if (Fc.isNull(topMenuId)){
            return list;
        }

        List<Long> functionIds = functionMenuDao.listFunctionId(topMenuId);
        if (Fc.isEmpty(functionIds)){
            return ListUtil.empty();
        }
        return list.stream().filter(tree->functionIds.contains(tree.getId())).collect(Collectors.toList());
    }

    @Override
    public List<Tree<Long>> treeClientMenu(List<Long> roleIds) {
		List<SelectIdNameVO> clients = clientDao.select();
		List<Tree<Long>> list = coreFunctionDao.treeMenu(roleIds, null);


		List<Tree<Long>> tree = ForestNodeMerger.mergeTree(clients);

		tree.forEach(t->{
			t.setChildren(list.stream()
					.filter(f->Fc.equalsValue(t.getId(), f.get("clientId")))
					.peek(f->f.setParentId(t.getId())).toList());
			if (t.hasChild()) {
				t.put("disabled",Boolean.FALSE);
			} else {
				t.put("disabled",Boolean.FALSE);
			}
		});

		return tree;
    }

    @Override
    public List<String> listBtnByRoleId(List<Long> roleIds) {
        Long clientId = clientDao.queryIdByCode(ShieldUtil.getClientCode()).orElseThrow(() -> new JpowerException(JpowerError.NotFind.getCode(), NOT_FOUND_CLIENT));
        return coreFunctionDao.listCodeByRoleIdClientBtn(roleIds, clientId);
    }

    @Override
    public List<Tree<Long>> treeButByMenu(List<Long> roleIds, Long parentId, Long clientId) {
        List<Long> parentIds = coreFunctionDao.listIdByRoleIdParentId(roleIds, parentId, clientId);
        if (Fc.isEmpty(parentIds)){
            return ListUtil.empty();
        }
        return coreFunctionDao.treeInfo(roleIds, parentId, clientId, parentIds, FunctionTypeEnum.BTN, false);
    }

    @Override
    public List<Tree<Long>> listTreeByRoleId(List<Long> roleIds) {
		Long clientId = clientDao.queryIdByCode(ShieldUtil.getClientCode()).orElseThrow(() -> new JpowerException(JpowerError.NotFind.getCode(), NOT_FOUND_CLIENT));
        return coreFunctionDao.treeFunction(roleIds, clientId);
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
            List<CoreFunction> menus = coreFunctionDao.listMenu(FunctionTypeEnum.MENU);

            if (Fc.isNotEmpty(menus)){
                //拿到所有的code
                List<String> allCode = coreFunctionDao.objListAs(Wrappers.getQueryWrapper().select(CoreFunction::getCode), String.class);

                //存储每个code的父级BTN的CODE
                Map<String,String> codeMap = new HashMap<>();

                //存储要保存的功能
                List<CoreFunction> functionList = new ArrayList<>();
                list.forEach(map->{
                    if (Fc.isNotEmpty(map)){
                        map.forEach((menuCode,functions)->{
                            Optional<CoreFunction> menu = menus.stream().filter(f->Fc.equalsValue(f.getCode(),menuCode)).findAny();
                            menu.ifPresent(tbCoreFunction -> ((List<Map<String,Object>>)functions).forEach(fun -> {
                                String code = MapUtil.getStr(fun, "code");
                                //只要不重复的code才去存储
                                if (functionList.stream().noneMatch(f -> Fc.equalsValue(f.getCode(), code)) && !allCode.contains(code)) {
                                    CoreFunction function = new CoreFunction();
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
                    List<CoreFunction> notNullFunctions = functionList.stream().filter(f->Fc.notNull(f.getParentId())).collect(Collectors.toList());
                    if (Fc.isNotEmpty(notNullFunctions)){
                        coreFunctionDao.addBatchSomeColumn(notNullFunctions);
                    }

                    List<CoreFunction> funcs = functionList.stream().filter(f->Fc.isNull(f.getParentId())).collect(Collectors.toList());
                    if (Fc.isNotEmpty(funcs)){
                        Map<String,CoreFunction> idCode = coreFunctionDao.selectIdByCode(new HashSet<>(codeMap.values()));
                        coreFunctionDao.addBatchSomeColumn(funcs.stream().peek(f-> {
                            CoreFunction parent = idCode.get(codeMap.get(f.getCode()));
                            f.setParentId(parent.getId());
                            f.setAncestorId(Fc.toStr(parent.getAncestorId(), TOP_CODE).concat(StringPool.COMMA).concat(Fc.toStr(parent.getId())));
                        }).collect(Collectors.toList()));
                    }
                }
            }
        }

		CacheUtil.clear(CacheNames.FUNCTION_KEY);
        return true;
    }

}
