package com.wlcb.jpower.feign;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import org.springframework.stereotype.Component;

/**
 * @ClassName TestClientFallback
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-10-09 15:19
 * @Version 1.0
 */
@Component
public class TestClientFallback implements TestClient {
    @Override
    public ResponseData<String> test(Integer t) {
//        if (!StringUtils.isBlank(RootContext.getXID())) {
//            try {
//                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
//            } catch (TransactionException e) {
//                e.printStackTrace();
//            }
//        }
        return ReturnJsonUtil.fail("请求失败");
    }
}
