package com.wlcb.wlj.module.dbs.dao.tiwen;

import com.wlcb.wlj.module.dbs.dao.BaseMapper;
import com.wlcb.wlj.module.dbs.entity.tiwen.TblSupplies;
import com.wlcb.wlj.module.dbs.entity.tiwen.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TblSupplies
 * @Description TODO 物资分配
 * @Author 郭丁志
 * @Date 2020-03-09 17:57
 * @Version 1.0
 */
@Component("supplieMapper")
public interface TblSuppliesMapper extends BaseMapper<TblSupplies> {


    List<Map<String, Object>> listDetail(User user, TblSupplies supplies);

}
