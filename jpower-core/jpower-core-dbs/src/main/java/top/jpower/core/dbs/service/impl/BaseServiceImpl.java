package top.jpower.core.dbs.service.impl;

import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;

/**
 * @author mr.gmac
 */
public class BaseServiceImpl<M extends JpowerBaseMapper<T>,T extends BaseEntity> extends JpowerServiceImpl<M,T> implements BaseService<T> {

}
