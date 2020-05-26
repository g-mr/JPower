package com.wlcb.jpower.module.dbs.dao.core.user;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
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
}
