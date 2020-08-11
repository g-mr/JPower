package com.wlcb.jpower.module.common.service.base.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.wlcb.jpower.module.common.service.base.BaseService;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;

import java.io.Serializable;

/**
 * @author mr.gmac
 */
public class BaseServiceImpl<M extends BaseMapper<T>,T> extends JpowerServiceImpl<M,T> implements BaseService<T> {

}
