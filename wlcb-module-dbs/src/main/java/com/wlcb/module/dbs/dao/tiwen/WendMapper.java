package com.wlcb.module.dbs.dao.tiwen;

import com.wlcb.module.dbs.dao.BaseMapper;
import com.wlcb.module.dbs.entity.tiwen.User;
import com.wlcb.module.dbs.entity.tiwen.Wend;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("wendMapper")
public interface WendMapper extends BaseMapper<Wend> {

    List<Map<String, Object>> listDetail(User user, Wend wend, String startTime, String endTime);

    Map<String, Object> queryNew(String openid);
}
