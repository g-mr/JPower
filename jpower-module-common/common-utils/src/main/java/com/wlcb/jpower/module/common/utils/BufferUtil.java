package com.wlcb.jpower.module.common.utils;

import okio.Buffer;

import java.io.EOFException;

/**
 * @author mr.g
 * @date 2021-08-07 23:18
 */
public class BufferUtil extends cn.hutool.core.io.BufferUtil {

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
