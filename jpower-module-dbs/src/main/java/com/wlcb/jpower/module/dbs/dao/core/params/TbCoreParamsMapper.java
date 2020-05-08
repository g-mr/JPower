package com.wlcb.jpower.module.dbs.dao.core.params;


import com.wlcb.jpower.module.dbs.dao.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam;
import org.springframework.stereotype.Component;


/**
 * @author mr.gmac
 */
@Component("tbCoreParamsMapper")
public interface TbCoreParamsMapper extends BaseMapper<TbCoreParam> {

    String selectByCode(String code);
}
