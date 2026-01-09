package top.jpower.core.dbs.service.impl;

import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.service.BaseService;

/**
 * @author mr.g
 */
public class BaseServiceImpl<M extends JpowerBaseMapper<T>,T extends BaseEntity> extends JpowerServiceImpl<M,T> implements BaseService<T> {

}
