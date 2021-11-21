package com.wlcb.jpower.dbs.dao.mapper;

import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tbCoreUserMapper")
public interface TbCoreUserMapper extends JpowerBaseMapper<TbCoreUser> {

    TbCoreUser selectAllById(String id);

    List<TbCoreUser> selectUserList(@Param("coreUser") TbCoreUser coreUser, @Param("orgIds") List<String> orgIds);
}
