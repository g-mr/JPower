package top.jpower.jpower.module.service.impl;

import top.jpower.jpower.module.service.BaseService;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.jpower.module.dbs.entity.base.BaseEntity;

/**
 * @author mr.gmac
 */
public class BaseServiceImpl<M extends JpowerBaseMapper<T>,T extends BaseEntity> extends JpowerServiceImpl<M,T> implements BaseService<T> {

}
