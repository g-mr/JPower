package top.jpower.core.util.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结果码
     **/
    private int code = -1;
    /**
     * 返回状态
     **/
    private boolean status = false;
    /**
     * 返回信息
     **/
    private String message = "请求失败";
    /**
     * 返回数据
     **/
    private T data;

}
