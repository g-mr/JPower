package com.wlcb.jpower.dbs.dao.client;

import com.wlcb.jpower.dbs.dao.client.mapper.TbCoreClientMapper;
import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @ClassName TbCoreClientDao
 * @Description TODO 客户端DAO
 * @Author 郭丁志
 * @Date 2020-07-31 13:00
 * @Version 1.0
 */
@Repository
public class TbCoreClientDao extends JpowerServiceImpl<TbCoreClientMapper, TbCoreClient> {
}
