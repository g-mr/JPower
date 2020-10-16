package com.wlcb.jpower.module.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "返回消息通用包装")
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(required = true,notes = "结果码",example = "200")
    private int code = -1;
    @ApiModelProperty(required = true,notes = "返回状态",example = "true")
    private boolean status = false;
    @ApiModelProperty(required = true,notes = "返回信息",example = "请求成功")
    private String message = "请求失败";
    @ApiModelProperty(required = true,notes = "返回数据")
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isSuccess() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
