package com.wlcb.jpower.module.dbs.dao.core.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.dbs.entity.core.function.TbCoreFunction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tbCoreFunctionMapper")
public interface TbCoreFunctionMapper extends BaseMapper<TbCoreFunction> {

}
