package com.wlcb.jpower.module.dbs.dao.sync;

import com.wlcb.jpower.module.dbs.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Component("syncMapping")
public interface SyncMapper extends BaseMapper {

    int insterList(@Param("listMap") List<Map<String, Object>> listMap,@Param("listFileds") List<String> listFileds,@Param("tableName") String tableName);

}
