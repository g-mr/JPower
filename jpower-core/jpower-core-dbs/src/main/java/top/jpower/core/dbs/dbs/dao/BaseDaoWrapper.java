package top.jpower.core.dbs.dbs.dao;

import cn.hutool.core.util.TypeUtil;
import com.mybatisflex.core.paginate.Page;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.BeanUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Author mr.g
 * @Date 2021/11/22 0022 23:33
 */
public interface BaseDaoWrapper<T> {

    default <V> V convert(T entity, Class<V> targetClass, Consumer<V> postProcessor) {
        if (entity == null) {
            return null;
        }
        V v = BeanUtil.copyProperties(entity, targetClass);
        if (postProcessor != null && v != null) {
            postProcessor.accept(v);
        }
        return v;
    }

    default <V> List<V> listConvert(List<T> list, Class<V> targetClass, Consumer<V> postProcessor) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(e -> convert(e, targetClass, postProcessor))
                .collect(Collectors.toList());
    }

    default <V> Pg<V> pageConvert(Page<T> page, Class<V> targetClass, Consumer<V> postProcessor) {
        List<V> list = listConvert(page.getRecords(), targetClass, postProcessor);
        return Pg.of(page.getTotalRow(), list);
    }

    default <V> V convert(V entity, Consumer<V> postProcessor) {
        if (entity == null) {
            return null;
        }
        if (postProcessor != null) {
            postProcessor.accept(entity);
        }
        return entity;
    }

    default <V> List<V> listConvert(List<V> list, Consumer<V> postProcessor) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(e -> convert(e, postProcessor))
                .collect(Collectors.toList());
    }

    default <V> Pg<V> pageConvert(Page<V> page, Consumer<V> postProcessor) {
        List<V> list = listConvert(page.getRecords(), postProcessor);
        return Pg.of(page.getTotalRow(), list);
    }

    default <V> Pg<V> pageConvert(Pg<V> page, Consumer<V> postProcessor) {
        List<V> list = listConvert(page.getList(), postProcessor);
        return Pg.of(page.getTotal(), list);
    }
}