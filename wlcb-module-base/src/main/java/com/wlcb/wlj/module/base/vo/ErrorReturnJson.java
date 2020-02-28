package com.wlcb.wlj.module.base.vo;


/**
 * @ClassName ResponseData
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-01-27 17:26
 * @Version 1.0
 */
public class ErrorReturnJson {

    private int code = -1;
    private boolean status = false;
    private String message = "请求失败";
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isStatus() {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
