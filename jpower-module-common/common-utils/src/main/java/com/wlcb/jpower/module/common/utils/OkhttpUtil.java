package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 读取Okhttp内容
 *
 * @author mr.g
 */
public class OkhttpUtil {


    public static String readResponseBody(ResponseBody responseBody) {
        if (Fc.notNull(responseBody)){
            try {
                BufferedSource source = responseBody.source();
                //缺这行会拿到一个空的Buffer
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();

                if (BufferUtil.isReadable(buffer)){

                    Charset charset = CharsetKit.CHARSET_UTF_8;
                    if (responseBody.contentType() != null){
                        charset = responseBody.contentType().charset(CharsetKit.CHARSET_UTF_8);
                    }

                    return buffer.clone().readString(charset);
                }
                return "omit bodyContent";
            } catch (IOException e) {
                return "(unknown bodyContent)";
            }
        }else {
            return "responseBody is null";
        }
    }

    public static String readRequestBody(RequestBody requestBody) {
        try(Buffer buffer = new Buffer()){
            if (Fc.notNull(requestBody)){
                requestBody.writeTo(buffer);
                if (BufferUtil.isReadable(buffer)){

                    Charset charset = CharsetKit.CHARSET_UTF_8;
                    if (requestBody.contentType() != null){
                        charset = requestBody.contentType().charset(CharsetKit.CHARSET_UTF_8);
                    }

                    return buffer.readString(charset);
                }
                return "omit bodyContent";
            }
            return "requestBody is null";
        }catch (IOException e){
            return "(unknown bodyContent)";
        }
    }

}
