package com.wlcb.jpower.module.common.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;

/**
 * @ClassName FileType
 * @Description TODO 文件类型
 * @Author 郭丁志
 * @Date 2020-05-02 00:43
 * @Version 1.0
 */
@Slf4j
public class FileType {

    /**
     * 缓存文件头信息-文件头信息
     **/
    private static final HashMap<String, String> M_FILE_TYPES = new HashMap<String, String>();

    static {
        // images
        M_FILE_TYPES.put("FFD8FF", "jpg");
        M_FILE_TYPES.put("89504E47", "png");
        M_FILE_TYPES.put("47494638", "gif");
        M_FILE_TYPES.put("49492A00", "tif");
        M_FILE_TYPES.put("424D", "bmp");
        // CAD
        M_FILE_TYPES.put("41433130", "dwg");
        M_FILE_TYPES.put("38425053", "psd");
        // 日记本
        M_FILE_TYPES.put("7B5C727466", "rtf");
        M_FILE_TYPES.put("3C3F786D6C", "xml");
        M_FILE_TYPES.put("68746D6C3E", "html");
        // 邮件
        M_FILE_TYPES.put("44656C69766572792D646174653A", "eml");
        M_FILE_TYPES.put("D0CF11E0", "doc");
        M_FILE_TYPES.put("D0CF11E0", "ppt");
        //excel2003版本文件
        M_FILE_TYPES.put("D0CF11E0", "xls");
        M_FILE_TYPES.put("5374616E64617264204A", "mdb");
        M_FILE_TYPES.put("252150532D41646F6265", "ps");
        M_FILE_TYPES.put("255044462D312E", "pdf");
        M_FILE_TYPES.put("504B0304", "docx");
        //excel2007以上版本文件
        M_FILE_TYPES.put("504B0304", "xlsx");
        M_FILE_TYPES.put("52617221", "rar");
        M_FILE_TYPES.put("57415645", "wav");
        M_FILE_TYPES.put("41564920", "avi");
        M_FILE_TYPES.put("2E524D46", "rm");
        M_FILE_TYPES.put("000001BA", "mpg");
        M_FILE_TYPES.put("000001B3", "mpg");
        M_FILE_TYPES.put("6D6F6F76", "mov");
        M_FILE_TYPES.put("3026B2758E66CF11", "asf");
        M_FILE_TYPES.put("4D546864", "mid");
        M_FILE_TYPES.put("1F8B08", "gz");
    }

    /**
     * @return 文件头信息
     * @author liang.pan
     * <p>
     * 方法描述：根据输入流获取文件头信息
     */
    public static String getFileType(String filePath){
        String res = null;

        try {
            FileInputStream is = new FileInputStream(filePath);
            getFileType(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static String getFileType(InputStream inputStream) {
        return M_FILE_TYPES.get(getFileHeader(inputStream));
    }

    /**
     * @return 文件头信息
     * @author liang.pan
     * <p>
     * 方法描述：根据输入流获取文件头信息
     */
    public static String getFileHeader(InputStream inputStream) {
        InputStream is = null;
        String value = null;
        try {
            is = inputStream;
            byte[] b = new byte[4];
            /*
             * int read() 从此输入流中读取一个数据字节。int read(byte[] b) 从此输入流中将最多 b.length
             * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
             * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
             */
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        log.info(">>>>>文件的头部信息:" + value);
        if (StringUtils.startsWith(value, "FFD8FF")) {
            value = value.substring(0, 6);
        }
        return value;
    }

    /**
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     * @author liang.pan
     * <p>
     * 方法描述：将要读取文件头信息的文件的byte数组转换成string类型表示
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}

