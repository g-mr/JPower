package com.wlcb.jpower.module.dbs.dao.core.params;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.params.TbCoreParam;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author mr.gmac
 */
@Component("tbCoreParamsMapper")
public interface TbCoreParamsMapper extends BaseMapper<TbCoreParam> {

    String selectByCode(String code);

    List<TbCoreParam> listAll(TbCoreParam coreParam);

    Integer updateByPrimaryKeySelective(TbCoreParam coreParam);
}
