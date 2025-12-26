package top.jpower.core.dbs.datascope.handler;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.mybatisflex.core.dialect.OperateType;
import com.mybatisflex.core.dialect.impl.CommonsDialectImpl;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.table.TableInfo;
import com.mybatisflex.core.util.LambdaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.dbs.datascope.DataScope;
import top.jpower.core.dbs.datascope.constants.DataScopeConstant;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.user.model.UserDto;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.CollectionUtil;
import top.jpower.core.util.utils.DateUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.core.util.utils.WebUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mybatisflex.core.constant.SqlConsts.AND;

/**
 * @Author mr.g
 * @Date 2021/4/23 0023 22:25
 */
@Slf4j
@RequiredArgsConstructor
public class DataScopeHandler extends CommonsDialectImpl {

    protected final UserConfig userConfig;

    private QueryWrapper buildDataScopeQueryWrapper(List<QueryTable> tables) {
        QueryWrapper queryWrapper = QueryWrapper.create();

        Map<String, DataScope> map = this.findDataScope(tables.stream().map(QueryTable::getName).toList());
        if (Fc.isEmpty(map)){
            return null;
        }

        // 当前登录用户
        UserDto userDto = userConfig.queryUser();

        if (Fc.isNull(userDto)){
            log.warn("未获取到用户，无法进行数据权限过滤");
            queryWrapper.and("1 = 2");
            return queryWrapper;
        }

        // 超级用户不做数据权限
        if (userDto.isRoot()){
            return null;
        }

        for (QueryTable queryTable : tables) {
            //noinspection DataFlowIssue
            if (map.containsKey(queryTable.getName())) {

                DataScope dataScope = map.get(queryTable.getName());

                // 自定义数据权限
                if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.CUSTOM)){
                    Map<String,Object> userMap = ChainMap.<String,Object>create().build();

                    BeanUtil.beanToMap(userDto, userMap ,new CopyOptions(){
                        @Override
                        protected Object editFieldValue(String fieldName, Object fieldValue) {

                            switch (fieldName) {
                                case "birthday", "lastLoginTime" ->
                                        fieldValue = Fc.isNull(fieldValue) ? DateUtil.now() : fieldValue;
                                case "idType", "loginCount", "userType" ->
                                        fieldValue = Fc.isNull(fieldValue) ? -999 : fieldValue;
                                case "roleIds", "childOrgId" ->
                                        fieldValue = Fc.isNull(fieldValue) ? new ArrayList<>() : fieldValue;
                                case "orgId", "userId" -> fieldValue = Fc.isNull(fieldValue) ? -1L : fieldValue;
                                default -> fieldValue = Fc.isNull(fieldValue) ? StringPool.EMPTY : fieldValue;
                            }

                            return fieldValue;
                        }
                    });

                    Set<Long> roleIds = CollectionUtil.newHashSet(userDto.getRoleIds());
                    if (Fc.isEmpty(roleIds)){
                        //如果没有角色证明看不到数据
                        roleIds.add(-1L);
                    }

                    userMap.put(LambdaUtil.getFieldName(UserDto::getRoleIds),
                            CollUtil.join(roleIds, StringPool.COMMA, StringPool.SINGLE_QUOTE, StringPool.SINGLE_QUOTE));

                    Set<Long> listOrgId = CollectionUtil.newHashSet(userDto.getChildOrgId());
                    if (Fc.isEmpty(listOrgId)){
                        //如果没有子级部门证明看不到数据
                        listOrgId.add(-1L);
                    }
                    userMap.put(LambdaUtil.getFieldName(UserDto::getChildOrgId),
                            CollUtil.join(listOrgId, StringPool.COMMA,StringPool.SINGLE_QUOTE,StringPool.SINGLE_QUOTE));

                    queryWrapper.and(StringUtil.formatMap(Fc.toStr(dataScope.getScopeValue(),"1=1"), userMap));
                }
                // 本人数据权限
                else if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.OWN)){
//                    TableInfoFactory.ofTableName(queryTable.getNameWithSchema()).getQueryColumnByProperty(dataScope.getScopeColumn());
                    queryWrapper.and(new QueryColumn(queryTable, dataScope.getScopeColumn()).eq(Fc.toLong(userDto.getUserId(), -1L)));
                }
                // 所在机构数据权限
                else if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.OWN_ORG)){
                    queryWrapper.and(new QueryColumn(queryTable, dataScope.getScopeColumn()).eq(Fc.toLong(userDto.getOrgId(), -1L)));
                }
                // 所在机构及子部门数据权限
                else if (Fc.equalsValue(dataScope.getScopeType(), DataScopeConstant.OWN_ORG_CHILD)){
                    Set<Long> listOrgId = CollectionUtil.newHashSet(userDto.getChildOrgId());
                    listOrgId.add(userDto.getOrgId());
                    //如果没有部门就什么都不要查出来
                    listOrgId.add(-1L);
                    queryWrapper.and(new QueryColumn(queryTable, dataScope.getScopeColumn()).in(listOrgId));
                }

            }
        }

        return queryWrapper;
    }

    @Override
    public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
        List<QueryTable> queryTables = CPI.getQueryTables(queryWrapper);
        if (queryTables == null || queryTables.isEmpty()) {
            super.prepareAuth(queryWrapper, operateType);
            return;
        }

        QueryWrapper wrapper = buildDataScopeQueryWrapper(queryTables);
        if (wrapper != null){
            queryWrapper.and(wrapper.toSQL());
        }
        super.prepareAuth(queryWrapper, operateType);
    }

    @Override
    public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {

        QueryWrapper queryWrapper = buildDataScopeQueryWrapper(ListUtil.of(new QueryTable(schema, tableName)));
        if (queryWrapper != null) {
            sql.append(AND).append(queryWrapper.toSQL());
        }
        super.prepareAuth(schema, tableName, sql, operateType);
    }

    @Override
    public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
        QueryWrapper queryWrapper = buildDataScopeQueryWrapper(ListUtil.of(new QueryTable(tableInfo.getSchema(), tableInfo.getTableName())));
        if (queryWrapper != null) {
            sql.append(AND).append(queryWrapper.toSQL());
        }
        super.prepareAuth(tableInfo, sql, operateType);
    }

    /**
     * 获取数据权限
     *
     * @param tables 表名
     * @return 数据权限
     */
    private Map<String, DataScope> findDataScope(List<String> tables) {
        //没有HttpServletRequest不走数据权限
        if (Fc.isNull(WebUtil.getRequest())){
            return null;
        }

        //从WEB获取数据权限
        String data = WebUtil.getRequest().getHeader(TokenConstant.DATA_SCOPE_NAME);
        if (Fc.isNotBlank(data)){
            List<DataScope> dataScopeList = JSON.parseArray(data, DataScope.class);
            // 根据tables匹配DataScope中的scopeTables，并将匹配结果组合成Map<String, DataScope>
            return dataScopeList.stream()
                .filter(d -> Fc.isNotEmpty(d.getScopeTables()))
                .flatMap(d -> d.getScopeTables().stream()
                    .filter(tables::contains)
                    .map(table -> Map.entry(table, d)))
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (existing, replacement) -> existing, // 如果有重复key，保留第一个
                    LinkedHashMap::new // 保持插入顺序
                ));
        }
        return null;
    }
}
