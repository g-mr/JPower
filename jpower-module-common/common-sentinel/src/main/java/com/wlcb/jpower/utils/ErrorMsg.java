package com.wlcb.jpower.utils;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.wlcb.jpower.module.base.vo.ResponseData;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;

/**
 * @ClassName ErrorMsg
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/9/12 0012 22:33
 * @Version 1.0
 */
public class ErrorMsg {
    /** 限流后的异常提示 **/
    public static ResponseData getFlow(){
        return ResponseData.builder()
                .code(HttpStatus.SC_CONTINUE)
                .message("当前流量太大：请稍后访问")
                .build();
    }

    /** 降级后的异常提示 **/
    public static ResponseData getDegrade(){
        return ResponseData.builder()
                .code(HttpStatus.SC_SWITCHING_PROTOCOLS)
                .message("服务暂停访问：请稍后访问")
                .build();
    }

    /** 降级后的异常提示 **/
    public static ResponseData getParamFlow(){
        return ResponseData.builder()
                .code(HttpStatus.SC_PROCESSING)
                .message("当前参数请求量过大：请稍后访问")
                .build();
    }

    /** 系统规则异常提示 **/
    public static ResponseData getSystemBlock(){
        return ResponseData.builder()
                .code(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION)
                .message("不满足系统规则，拒绝访问")
                .build();
    }

    /** 授权规则异常提示 **/
    public static ResponseData getAuthority(){
        return ResponseData.builder()
                .code(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION)
                .message("授权规则不通过")
                .build();
    }

    /** 统一返回 **/
    @SneakyThrows
    public static ResponseData blockException(Throwable ex) {
        if(ex instanceof FlowException){
            return getFlow();
        }
        //降级异常
        else if(ex instanceof DegradeException){
            return getDegrade();
        }
        //参数热点异常
        else if(ex instanceof ParamFlowException){
            return getParamFlow();
        }
        //系统异常
        else if(ex instanceof SystemBlockException){
            return getSystemBlock();
        }
        //授权异常
        else if(ex instanceof AuthorityException){
            return getAuthority();
        }

        throw ex;
    }

}
