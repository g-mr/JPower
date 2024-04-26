package top.jpower.jpower.dbs.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tbCoreUserMapper")
public interface TbCoreUserMapper extends JpowerBaseMapper<TbCoreUser> {

    TbCoreUser selectAllById(Long id);

    List<TbCoreUser> selectUserList(@Param("coreUser") TbCoreUser coreUser, @Param("orgIds") List<Long> orgIds);
}
