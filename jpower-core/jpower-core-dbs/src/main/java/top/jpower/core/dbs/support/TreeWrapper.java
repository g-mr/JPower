package top.jpower.core.dbs.support;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.exception.MybatisFlexException;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.RawQueryColumn;
import com.mybatisflex.core.util.LambdaGetter;
import com.mybatisflex.core.util.LambdaUtil;
import top.jpower.core.util.utils.Fc;

import java.util.HashMap;
import java.util.Map;

import static com.mybatisflex.core.query.QueryMethods.case_;
import static com.mybatisflex.core.query.QueryMethods.exists;
import static top.jpower.core.dbs.support.Wrappers.EXCLUDE;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

/**
 * 树形条件构造器
 *
 * @author mr.g
 **/
public class TreeWrapper extends QueryWrapper {

    private final QueryColumn id;
    private final QueryColumn parentId;
    private final String idAlias = ForestNodeMerger.CONFIG.getIdKey();
    private final String parentIdAlias = ForestNodeMerger.CONFIG.getParentIdKey();

    public TreeWrapper(String id, String parentId) {
        this.id = new RawQueryColumn(id).as(idAlias);
        this.parentId = new RawQueryColumn(parentId).as(parentIdAlias);
        init();
    }

    public <T> TreeWrapper(LambdaGetter<T> id, LambdaGetter<T> parentId) {
        this.id = LambdaUtil.getQueryColumn(id).as(idAlias);
        this.parentId = LambdaUtil.getQueryColumn(parentId).as(parentIdAlias);
        init();
    }

    public TreeWrapper(QueryColumn id, QueryColumn parentId) {
        this.id = id.as(idAlias);
        this.parentId = parentId.as(parentIdAlias);
        init();
    }

    private void init(){
        super.addSelectColumn(this.id);
        super.addSelectColumn(this.parentId);
    }


    /**
     * 懒加载
     *
     * @author mr.g
     **/
    public TreeWrapper lazy(){
        return lazy(TOP_CODE);
    }

    /**
     * 懒加载
     *
     * @author mr.g
     **/
    public TreeWrapper lazy(Object parentIdValue){
        if (Fc.isEmpty(queryTables)) {
            throw new MybatisFlexException("请先from表");
        }

        String tableNameAlias = StrUtil.blankToDefault(queryTables.get(0).getAlias(), queryTables.get(0).getNameWithSchema());

        super.select(
                case_()
                .when(exists(QueryWrapper.create()
                        .select("1")
                        .from(queryTables.get(0))
                        .where(this.parentId.eq(tableNameAlias+"."+this.id.getName()))))
                    .then(1)
                .else_(0)
                .end()
                .as(ForestNodeMerger.HAS_CHILDREN)
        );

        super.addWhereQueryCondition(this.parentId.eq(parentIdValue));
        return this;
    }

    @Override
    public void clear() {
        super.clear();
        init();
    }

	public TreeWrapper map(Map<String, Object> query) {
		EXCLUDE.forEach(query::remove);
		where(w->{
			SqlWrapper.buildCondition(w, query);
		});
		return this;
	}

	public TreeWrapper map(Map<String, Object> query, String prefix) {
		EXCLUDE.forEach(query::remove);
		Map<String, Object> params = new HashMap<>(query.size());
		query.forEach((key, value) -> params.put(prefix + "." + key, value));
		where(w->{
			SqlWrapper.buildCondition(w, params);
		});
		return this;
	}
}
