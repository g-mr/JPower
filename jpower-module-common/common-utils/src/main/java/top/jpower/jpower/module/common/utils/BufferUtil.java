package top.jpower.jpower.module.common.utils;

import cn.hutool.core.io.IoUtil;
import okio.Buffer;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * okio - Buffer
 *
 * @author mr.g
 */
public class BufferUtil extends cn.hutool.core.io.BufferUtil {

    /**
     * 判断okio的Buffer是否可读
     *
     * @author mr.g
     * @param buffer okio.Buffer
     * @return boolean
     **/
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

    /**
     * 读取Buffer
     *
     * @author mr.g
     * @param buffer
     * @param charset 编码
     * @return java.lang.String
     **/
    public static String read(Buffer buffer,Charset charset) {
        if (BufferUtil.isReadable(buffer)){
            return buffer.readString(charset);
        }
        return "buffer not readable";
    }

    /**
     * 克隆读取Buffer
     *
     * @author mr.g
     * @param buffer
     * @param charset 编码
     * @return java.lang.String
     **/
    public static String cloneRead(Buffer buffer,Charset charset) {
        if (BufferUtil.isReadable(buffer)){
            return buffer.clone().readString(charset);
        }
        return "buffer not readable";
    }

    /**
     * 写入流
     *
     * @author mr.g
     * @param out 输出流
     * @param lines 内容
     * @return void
     **/
    public static void writeLines(OutputStream out,Collection<String> lines)
            throws IOException {
        BufferedWriter writer = IoUtil.toBuffered(IoUtil.getUtf8Writer(out));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
        writer.flush();
    }
}
