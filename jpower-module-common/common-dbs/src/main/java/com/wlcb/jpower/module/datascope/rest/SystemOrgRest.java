package com.wlcb.jpower.module.datascope.rest;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


/**
 * @author mr.g
 * @date 2022-04-22 00:31
 */
public class SystemOrgRest {


    private static RestTemplate restTemplate;

    static {
        restTemplate = SpringUtil.getBean(RestTemplate.class);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 根据部门ID获取下级所有ID
     * @Date 15:47 2020-05-06
     **/
    public static List<String> getChildIdOrgById(String orgId) {
        ResponseData<List<String>> responseData = restTemplate.getForObject("http://"+AppConstant.JPOWER_SYSTEM+"/core/org/queryChildById?id={1}",ResponseData.class,orgId);
        return Optional.ofNullable(responseData).orElse(new ResponseData<List<String>>()).getData();
    }

}
