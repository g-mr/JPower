package com.qidiangk.smart.aster.dbs.dao.asterisk.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.throwable.JpowerException;
import com.qidiangk.smart.aster.dbs.entity.asterisk.TransportsDO;

/**
 * @author mr.g
 */
@Mapper
public interface TransportsMapper extends JpowerBaseMapper<TransportsDO> {

    /**
     * 获取默认的transport
     * @return transport
     */
    default String get() {
        TransportsDO transport = selectOneByQuery(Wrappers.getQueryWrapper().limit(1));
        if (transport == null) {
            throw new JpowerException(500, "请先配置Transport");
        }
        return transport.getId();
    }
}
