package com.wlcb.jpower.module.common.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.module.common.service.BaseService;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;

/**
 * @author mr.gmac
 */
public class BaseServiceImpl<M extends BaseMapper<T>,T> extends JpowerServiceImpl<M,T> implements BaseService<T> {

}
