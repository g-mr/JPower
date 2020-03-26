package com.wlcb.ylth.module.common.service.user.impl;

import com.wlcb.ylth.module.common.service.redis.RedisUtils;
import com.wlcb.ylth.module.common.service.user.WxUserService;
import com.wlcb.ylth.module.common.utils.*;
import com.wlcb.ylth.module.dbs.dao.user.ZhengwuUserMapper;
import com.wlcb.ylth.module.dbs.entity.user.TblZhengwuUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author mr.gmac
 */
@Service("wxUserService")
public class WxUserServiceImpl implements WxUserService {

    private static final Logger logger = LoggerFactory.getLogger(WxUserServiceImpl.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ZhengwuUserMapper zhengwuUserMapper;

    @Override
    public TblZhengwuUser selectWxuser(String openid, Integer kid){
        TblZhengwuUser wx = zhengwuUserMapper.queryByOpenid(openid);
        if (wx != null) {
            // 从缓存取用户今日在该卡口的次数
            if (kid != null) {
                String key = ("kcount-") + wx.getId() + "-" + kid;
                if (redisUtils.exists(key)) {
                    wx.setKcount((Integer) redisUtils.get(key));
                } else {
                    wx.setKcount(0);
                }
            }

            if (StringUtils.isBlank(wx.getQrcodeText())){
                String qrcodeText = JkQrcode.getInstance().getJkInfo(wx);
                if (StringUtils.isNotBlank(qrcodeText)){
                    wx.setQrcodeText(qrcodeText);
                    updateQrcodeById(qrcodeText,wx.getId());
                }
            }
        }
        return wx;
    }

    public Integer updateQrcodeById(String qrcodeText,String id){
        Integer count = zhengwuUserMapper.updateQrcodeById(qrcodeText,id);
        if (count <= 0){
            logger.error("修改用户健康码信息失败，id={},二维码信息={}",id,qrcodeText);
        }
        return count;
    }

}
