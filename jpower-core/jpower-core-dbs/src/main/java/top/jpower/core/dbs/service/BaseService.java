package top.jpower.core.dbs.service;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.core.util.LambdaGetter;
import top.jpower.core.util.rsp.Pg;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author mr.gmac
 */
public interface BaseService<T> extends IService<T> {

	/**
	 * 根据ID查询
	 *
	 * @param id 主键ID
	 * @param clz 查询结果类型
	 * @return 查询结果
	 */
	<R> R getByIdAs(Serializable id, Class<R> clz);

	/**
     * 查询树形结构
	 *
	 * @author mr.g
     * @param treeWrapper 查询条件
	 * @return java.util.List<Tree<E>> 树形结构
     */
    <TREE extends QueryWrapper, E extends Serializable> List<Tree<E>> tree(TREE treeWrapper);

    /**
     * 把查询结果转换成任何类型
	 *
	 * @author mr.g
     * @param queryWrapper 查询条件
     * @param function 转换方法
     * @return java.util.List<V> 转换结果
     */
    <V> List<V> listConver(QueryWrapper queryWrapper, Function<T, V> function);

    /**
     * 根据 ID 真实删除
     * @param id 主键ID
     */
    boolean removeRealById(Serializable id);

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper
     */
    boolean removeReal(QueryWrapper queryWrapper);

    /**
     * 删除（根据ID 批量删除）
     * @param idList 主键ID列表
     */
    boolean removeRealByIds(Collection<? extends Serializable> idList);

    /**
     * 根据 columnMap 条件，删除记录
     * @param columnMap 表字段 map 对象
     */
    boolean removeRealByMap(Map<String, Object> columnMap);

    /**
     * 批量新增指定列
     * @param entityList 实体列表
     */
    boolean addBatchSomeColumn(List<T> entityList);

    /**
     * 根据 ID 更新所有列
     * @param entity 实体
     */
    boolean updateAllById(T entity);

	/**
	 * <p>根据查询条件分页查询数据。</p>
	 *
	 * @param query 查询条件
	 * @return 分页对象
	 */
	Pg<T> pg(QueryWrapper query);

	/**
	 * <p>根据查询条件分页查询数据。</p>
	 *
	 * @param query 查询条件
	 * @return 分页对象
	 */
	<R> Pg<R> pgAs(QueryWrapper query, Class<R> asType);

	/**
	 * 通过字段查询是否存在
	 *
	 * @param column 字段
	 * @param value 值
	 * @return 是否存在
	 */
	boolean existsByField(LambdaGetter<T> column, Object value);

	/**
	 * 通过字段信息
	 *
	 * @param column 字段
	 * @param value 值
	 * @return 数量
	 */
	T getOneByField(LambdaGetter<T> column, Object value);

}
