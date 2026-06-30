package com.qidiangk.smart.maxkb.config.retrofit;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.lianjiatech.retrofit.spring.boot.core.ErrorDecoder;
import com.github.lianjiatech.retrofit.spring.boot.exception.ReadResponseBodyException;
import com.github.lianjiatech.retrofit.spring.boot.exception.RetrofitException;
import com.github.lianjiatech.retrofit.spring.boot.util.RetrofitUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.utils.Fc;

import java.io.IOException;

@Slf4j
public class HttpErrorDecoder extends ErrorDecoder.DefaultErrorDecoder {

    /**
     * 当无效响应的时候，将HTTP信息解码到异常中，无效响应由业务自行判断。
     * <p>
     * When the response is invalid, decode the HTTP information into the exception, invalid response is determined by business.
     *
     * @param request  request
     * @param response response
     * @return If it returns null, the processing is ignored and the processing continues with the original response.
     */
    public RuntimeException invalidRespDecode(Request request, Response response) {
        if (!response.isSuccessful()) {
            try {
                String responseBody = RetrofitUtils.readResponseBody(response);
                log.warn("返回数据异常==={}，body={}", request.url(), UnicodeUtil.toString(responseBody));
                if (JSONUtil.isTypeJSONObject(responseBody)){
                    JSONObject jsonObject = JSONUtil.parseObj(responseBody);
                    String msg = Fc.toStr(jsonObject.getStr("msg"), jsonObject.getStr("message"));
                    return new JpowerException(Fc.toInt(jsonObject.getStr("code"), response.code()), Fc.toStr(msg, responseBody));
                } else {
                    return new JpowerException(response.code(), UnicodeUtil.toString(responseBody));
                }
            } catch (ReadResponseBodyException e){
                throw new RetrofitException(
                        String.format("read ResponseBody error! request=%s, response=%s", request, response), e);
            }
        }
        return null;
    }

    /**
     * 当请求发生IO异常时，将HTTP信息解码到异常中。
     * <p>
     * When an IO exception occurs in the request, the HTTP information is decoded into the exception.
     *
     * @param request request
     * @param cause   IOException
     * @return 解码后的异常
     */
    public RuntimeException ioExceptionDecode(Request request, IOException cause) {
//        return RetrofitException.errorExecuting(request, cause);
        log.warn(cause.getMessage() + ", request=" + request);
        return new JpowerException(999, cause.getMessage());
    }

    /**
     * 当请求发生除IO异常之外的其它异常时，将HTTP信息解码到异常中。
     * <p>
     * When the request has an exception other than the IO exception, the HTTP information is decoded into the exception.
     *
     * @param request request
     * @param cause   Exception
     * @return 解码后的异常
     */
    public RuntimeException exceptionDecode(Request request, Exception cause) {
//        return RetrofitException.errorUnknown(request, cause);
        log.warn(cause.getMessage() + ", request=" + request);
        return new JpowerException(999, cause.getMessage());
    }

}
