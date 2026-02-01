package top.jpower.core.dbs.dbs.dao;

import cn.hutool.core.util.TypeUtil;
import com.mybatisflex.core.paginate.Page;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.BeanUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author mr.g
 * @Date 2021/11/22 0022 23:33
 */
public interface BaseDaoWrapper<T,V> {

    V build(V entity);

    default V conver(T entity) {
        Type type = TypeUtil.getTypeArgument(getClass(), 1);

        //noinspection unchecked
        V v = Objects.requireNonNull(BeanUtil.copyProperties(entity, (Class<V>)type));
        build(v);
        return v;
    }

    default List<V> listConver(List<T> list){
        return list.stream().filter(Objects::nonNull).map(this::conver).collect(Collectors.toList());
    }

    default Page<V> pageConver(Page<T> page){
        List<V> list = listConver(page.getRecords());
        Page<V> pageVo = new Page<>(page.getPageNumber(),page.getPageSize(),page.getTotalRow());
        pageVo.setRecords(list);
        return pageVo;
    }

    default List<V> listBuild(List<V> list){
        list.forEach(this::build);
        return list;
    }

    default Pg<V> pageBuild(Pg<V> page){
        listBuild(page.getList());
        return page;
    }

}
