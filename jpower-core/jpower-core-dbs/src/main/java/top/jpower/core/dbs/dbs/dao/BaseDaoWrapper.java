package top.jpower.core.dbs.dbs.dao;

import com.mybatisflex.core.paginate.Page;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author mr.g
 * @Date 2021/11/22 0022 23:33
 */
public interface BaseDaoWrapper<T,V> {

    V conver(T entity);

    @SuppressWarnings("unchecked")
    default List<V> listConver(List<T> list){
        return list.stream().filter(Objects::nonNull).map(this::conver).collect(Collectors.toList());
    }

    default Page<V> pageConver(Page<T> page){
        List<V> list = listConver(page.getRecords());
        Page<V> pageVo = new Page<>(page.getPageNumber(),page.getPageSize(),page.getTotalRow());
        pageVo.setRecords(list);
        return pageVo;
    }

}
