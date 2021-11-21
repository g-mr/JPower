package com.wlcb.jpower.module.common.service.impl;

import com.wlcb.jpower.module.common.service.BaseService;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;

/**
 * @author mr.gmac
 */
public class BaseServiceImpl<M extends JpowerBaseMapper<T>,T extends BaseEntity> extends JpowerServiceImpl<M,T> implements BaseService<T> {

}
