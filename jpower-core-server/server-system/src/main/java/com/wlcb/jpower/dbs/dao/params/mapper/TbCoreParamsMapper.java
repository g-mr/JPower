package com.wlcb.jpower.dbs.dao.params.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.dbs.entity.params.TbCoreParam;
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
