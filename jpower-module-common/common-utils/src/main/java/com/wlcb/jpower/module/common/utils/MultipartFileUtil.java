package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.support.FileType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName MultipartFileUtil
 * @Description TODO 上传文件工具类
 * @Author 郭丁志
 * @Date 2020-03-31 00:45
 * @Version 1.0
 */
public class MultipartFileUtil{

    private static final Logger logger = LoggerFactory.getLogger(MultipartFileUtil.class);

    /**
     * @Author 郭丁志
     * @Description //TODO 上传文件
     * @Date 00:47 2020-03-31
     * @Param [file, fileSuffixName 支持的文件后缀名 多个,分割, savePath]
     * @return java.lang.String
     **/
    public static String saveFile(MultipartFile file,String savePath) throws Exception{

        return saveFile(file,null,savePath);
    }

    public static String saveFile(MultipartFile file,String fileSuffixName,String savePath) throws IOException {

        String fileName = UUIDUtil.getUUID();
        //获得文件后缀名
        String suffixName=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        if (StringUtils.isNotBlank(fileSuffixName) && !StringUtils.containsIgnoreCase(fileSuffixName,suffixName) && !StringUtils.containsIgnoreCase(fileSuffixName, FileType.getFileType(file.getInputStream()))){
            throw new IllegalStateException("不支持的后缀类型");
        }

        String path = DateUtil.getDate(new Date(), DateUtil.PATTERN_DATE) + File.separator + fileName + "." + suffixName;

        File nFile = new File(savePath+File.separator+path);

        if(!nFile.getParentFile().exists()){
            nFile.getParentFile().mkdirs();
        }

        file.transferTo(nFile);

        logger.info("文件保存成功，文件路径={}",nFile.getAbsolutePath());

        return path;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 读取文件内容
     * @Date 23:08 2020-04-04
     * @Param [multipartFile]
     * @return java.util.List<java.lang.String>
     **/
    public static List<String> readContent(MultipartFile multipartFile,String chares){

        List<String> list = new ArrayList<>();
        Reader reader = null;

        try {
            reader = new InputStreamReader(multipartFile.getInputStream(), chares);
            BufferedReader br = new BufferedReader( reader);
            String line;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                if(line!=null && line!=""){
                    list.add(line);
                }
            }
        }catch (Exception e){
            logger.info("上传文件读取失败,error={}",ExceptionsUtil.getStackTraceAsString(e));
        } finally {
            Fc.closeQuietly(reader);
        }

        return list;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 读取上传文件第一行
     * @Date 23:08 2020-04-04
     * @Param [multipartFile]
     * @return java.lang.String
     **/
    public static String readStartRowContent(MultipartFile multipartFile){

        String s = null;
        Reader reader = null;

        try {
            reader = new InputStreamReader(multipartFile.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader( reader);
            String line;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                if(line!=null && line!=""){
                    s = line;
                }
                break;
            }
        }catch (Exception e){
            logger.info("上传文件读取失败,error={}",ExceptionsUtil.getStackTraceAsString(e));
        } finally {
            Fc.closeQuietly(reader);
        }

        return s;
    }

}
