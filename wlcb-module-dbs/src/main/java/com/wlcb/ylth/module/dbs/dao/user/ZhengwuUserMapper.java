package com.wlcb.ylth.module.dbs.dao.user;

import com.wlcb.ylth.module.dbs.dao.BaseMapper;
import com.wlcb.ylth.module.dbs.entity.user.TblZhengwuUser;
import org.springframework.stereotype.Component;

/**
 * @author mr.gmac
 */
@Component("zhengwuUserMapper")
public interface ZhengwuUserMapper extends BaseMapper<TblZhengwuUser> {

    TblZhengwuUser queryByOpenid(String openid);

    Integer updateQrcodeById(String qrcodeText, String id);
}
