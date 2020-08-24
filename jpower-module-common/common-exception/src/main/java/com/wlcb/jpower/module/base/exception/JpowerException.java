package com.wlcb.jpower.module.base.exception;


import com.wlcb.jpower.module.base.enums.JpowerError;

/**
 * @author mr.gmac
 */
public class JpowerException extends RuntimeException {
    private static final long serialVersionUID = -3288337436322386813L;
    private int code;

    public JpowerException(String message) {
        super(message);
    }

    public JpowerException(int code, String message) {
        super(message);
        this.code = code;
    }

    public JpowerException(JpowerError err) {
        super(err.getMessage());
        this.code = err.getCode();
    }

    public JpowerException(Throwable cause) {
        super(cause);
    }

    public JpowerException(JpowerError err, Throwable cause) {
        super(err.getMessage(), cause);
        this.code = err.getCode();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }


}
