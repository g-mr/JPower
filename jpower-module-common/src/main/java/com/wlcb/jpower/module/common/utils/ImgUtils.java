package com.wlcb.jpower.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @ClassName ImgUtils
 * @Description TODO 图片文件工具
 * @Author 郭丁志
 * @Date 2020-06-16 17:39
 * @Version 1.0
 */
@Slf4j
public class ImgUtils {

    /**
     * @Author 郭丁志
     * @Description //TODO BASE64转文件
     * @Date 18:17 2020-06-16
     * @Param [fileBase64String, filePath, fileName]
     * @return java.io.File
     **/
    public static File convertBase64ToFile(String fileBase64String, String filePath, String fileName) {

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bfile = decoder.decodeBuffer(fileBase64String);

            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 文件转BASE64
     * @Date 18:17 2020-06-16
     * @Param [imgFilePath]
     * @return java.lang.String
     **/
    public static String getImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;

        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    public static void main(String[] args) {

//        System.out.println(getImageStr("/Users/mr.gmac/Desktop/下载.png"));

        System.out.println(convertBase64ToFile("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",
                "/Users/mr.gmac/IdeaProjects/crawler-police/file/","r9yojLmk3Paiz5hwHdF.png"));;
    }

}
