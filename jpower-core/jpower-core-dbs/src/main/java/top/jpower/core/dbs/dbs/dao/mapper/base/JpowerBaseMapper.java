/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package top.jpower.core.dbs.dbs.dao.mapper.base;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import top.jpower.core.util.rsp.Pg;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 * <p>这个 Mapper 支持 id 泛型</p>
 *
 * @author hubin
 * @since 2016-01-23
 */
public interface JpowerBaseMapper<T> extends BaseMapper<T> {

    /**
     * 根据 ID 真实删除
     *
     * @param id 主键ID
     * @return 删除成功条数
     */
    default int deleteRealById(Serializable id) {
        return LogicDeleteManager.execWithoutLogicDelete(()-> this.deleteById(id));
    }

    /**
     * 根据 columnMap 条件，删除记录
     *
     * @param columnMap 表字段 map 对象
     * @return 删除成功条数
     */
    default int deleteRealByMap(Map<String, Object> columnMap) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> this.deleteByMap(columnMap));
    }

    /**
     * 根据 entity 条件，删除记录
     *
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return 删除成功条数
     */
    default int deleteReal(QueryWrapper wrapper) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> this.deleteByQuery(wrapper));
    }

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @return 删除成功条数
     */
    default int deleteRealBatchIds(Collection<? extends Serializable> idList) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> this.deleteBatchByIds(idList));
    }

    /**
     * 根据条件，删除记录
     *
     * @author mr.g
     * @param condition 删除条件
     * @return 删除成功条数
     **/
    default int deleteRealByCondition(QueryCondition condition) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> this.deleteByCondition(condition));
    }

    /**
     * 分页查询。
     *
     * @param page         包含了页码、每页的数据量，可能包含数据总量
     * @param queryWrapper 条件
     * @return page 数据
     */
    default Pg<T> page(Page<T> page, QueryWrapper queryWrapper) {
        Page<T> result = paginateAs(page, queryWrapper, null);
        return Pg.of(result.getTotalRow(), result.getRecords());
    }

    /**
     * 分页查询。
     *
     * @param page         包含了页码、每页的数据量，可能包含数据总量
     * @param queryWrapper 条件
     * @param asType       接收数据类型
     * @return 分页数据
     */
    default <R> Pg<R> pageAs(Page<R> page, QueryWrapper queryWrapper, Class<R> asType) {
        Page<R> result = BaseMapper.super.paginateAs(page, queryWrapper, asType);
        return Pg.of(result.getTotalRow(), result.getRecords());
    }
}
