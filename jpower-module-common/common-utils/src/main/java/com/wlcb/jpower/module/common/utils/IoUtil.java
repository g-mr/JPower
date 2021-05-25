/**
 * Copyright (c) 2018-2028, DreamLu 卢春梦 (qq596392912@gmail.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import org.springframework.lang.Nullable;

import java.io.*;
import java.nio.charset.Charset;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.NEWLINE;

/**
 * xpath解析xml
 *
 * <pre>
 *     文档地址：
 *     http://www.w3school.com.cn/xpath/index.asp
 * </pre>
 *
 * @author L.cm
 */
@Slf4j
public class IoUtil extends org.springframework.util.StreamUtils {

    /**
     * closeQuietly
     *
     * @param closeable 自动关闭
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * InputStream to String utf-8
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     */
    public static String toString(InputStream input) {
        return toString(input, CharsetKit.CHARSET_UTF_8);
    }

    /**
     * InputStream to String
     *
     * @param input   the <code>InputStream</code> to read from
     * @param charset the <code>Charsets</code>
     * @return the requested String
     */
    public static String toString(@Nullable InputStream input, java.nio.charset.Charset charset) {
        try {
            return IoUtil.copyToString(input, charset);
        } catch (IOException e) {
            throw ExceptionsUtil.unchecked(e);
        } finally {
            IoUtil.closeQuietly(input);
        }
    }

    public static byte[] toByteArray(@Nullable InputStream input) {
        try {
            return IoUtil.copyToByteArray(input);
        } catch (IOException e) {
            throw ExceptionsUtil.unchecked(e);
        } finally {
            IoUtil.closeQuietly(input);
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>String</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws IOException if an I/O error occurs
     */
    public static void write(@Nullable final String data, final OutputStream output, final java.nio.charset.Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(encoding));
        }
    }

    public static String readResponseBody(ResponseBody responseBody) {
        if (Fc.notNull(responseBody)){
            try {
                BufferedSource source = responseBody.source();
                //缺这行会拿到一个空的Buffer
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();

                if (IoUtil.isReadable(buffer)){

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
                if (isReadable(buffer)){

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
            log.error("读取requestBody出错:{}",NEWLINE+ ExceptionsUtil.getStackTraceAsString(e));
            return "(unknown bodyContent)";
        }
    }

    public static boolean isReadable(Buffer buffer) {
        try{
            for (int i = 0; i < 64 && buffer.exhausted() && buffer.size()>64; i++) {
                int codePoint = buffer.readUtf8CodePoint();
                if (Character.isIdentifierIgnorable(codePoint) && Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            e.printStackTrace();
            return false;
        }
    }
}
