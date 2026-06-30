package com.qidiangk.smart.aster.service;


import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.pojo.vo.ivr.CallVO;

import java.util.Map;

public interface CallService {

    boolean isPhoneZero(String phone, EndpointsDO endpointsDO);

    CallVO call(String attendId, String phone, String lineId);


    CallVO call(String phone, String endpointId, Map<String, Object> params);

    CallVO call(String phone, String endpointId, Map<String, Object> params, boolean isAsync, boolean waitHang);
}
