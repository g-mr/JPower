package top.jpower.core.dbs.mp.support;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.mybatisflex.core.exception.MybatisFlexException;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.RawQueryColumn;
import com.mybatisflex.core.util.LambdaGetter;
import com.mybatisflex.core.util.LambdaUtil;
import org.apache.poi.ss.formula.functions.T;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

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

    private String hasChildren;


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
    public TreeWrapper lazy(String parentIdValue){
        if (Fc.isEmpty(queryTables)) {
            throw new MybatisFlexException("请先from表");
        }

        String tableNameAlias = StrUtil.blankToDefault(queryTables.get(0).getAlias(), queryTables.get(0).getNameWithSchema());


        this.hasChildren = "( SELECT CASE WHEN count( 1 ) > 0 THEN 1 ELSE 0 END FROM "+queryTables.get(0).getNameWithSchema()+" as c WHERE "+this.parentId+" = "+tableNameAlias+"."+this.id+" ) AS "+ForestNodeMerger.HAS_CHILDREN;
        select(ArrayUtil.toArray(this.list, String.class));
        eq(this.parentId, StringUtil.isBlank(parentIdValue)? JpowerConstants.TOP_CODE:parentIdValue);
        return this;
    }

    @Override
    public void clear() {
        super.clear();
        init();
    }
}
