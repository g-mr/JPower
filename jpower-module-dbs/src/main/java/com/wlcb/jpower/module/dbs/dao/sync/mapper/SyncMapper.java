package com.wlcb.jpower.module.dbs.dao.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.sync.ImportInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Component("syncMapping")
public interface SyncMapper extends BaseMapper<ImportInfo> {

    int insterList(@Param("listMap") List<Map<String, Object>> listMap,@Param("listFileds") List<String> listFileds,@Param("tableName") String tableName);

}
