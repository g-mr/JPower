package top.jpower.jpower.exception;

/**
 * @author mr.g
 * @date 2024/3/6 21:10
 * @description
 */
public class JpowerFeignException extends RuntimeException{

    private int code;

    public JpowerFeignException(String message) {
        super(message);
    }

    public JpowerFeignException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
