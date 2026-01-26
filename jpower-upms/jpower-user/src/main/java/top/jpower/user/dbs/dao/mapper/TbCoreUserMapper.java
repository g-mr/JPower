package top.jpower.user.dbs.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.user.dbs.entity.CoreUser;

import java.util.List;

/**
 * @author mr.gmac
 */
@Mapper
public interface TbCoreUserMapper extends JpowerBaseMapper<CoreUser> {

    TbCoreUser selectAllById(Long id);

    List<TbCoreUser> selectUserList(@Param("coreUser") TbCoreUser coreUser, @Param("orgIds") List<Long> orgIds);
}
