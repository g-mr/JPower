package com.wlcb.ylth.module.common.service.user;

import com.wlcb.ylth.module.base.vo.ResponseData;
import com.wlcb.ylth.module.dbs.entity.user.TblZhengwuUser;

/**
 * @author mr.gmac
 */
public interface WxUserService {

    TblZhengwuUser selectWxuser(String openid, Integer kid);
}
