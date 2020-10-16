package com.wlcb.jpower.dbs.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tbCoreUserMapper")
public interface TbCoreUserMapper extends BaseMapper<TbCoreUser> {

    Integer insertList(@Param("list") List<TbCoreUser> list);

    TbCoreUser selectAllById(String id);

    List<TbCoreUser> selectUserList(@Param("coreUser") TbCoreUser coreUser, @Param("orgIds") List<String> orgIds, @Param("tenantCode") String tenantCode);
}
