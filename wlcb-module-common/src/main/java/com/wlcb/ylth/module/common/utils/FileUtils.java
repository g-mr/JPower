package com.wlcb.ylth.module.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName FileUtils
 * @Description TODO 文件工具
 * @Author 郭丁志
 * @Date 2020-02-05 00:57
 * @Version 1.0
 */
public class FileUtils {

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static String getFileType(String filename) {
        if (filename.equals("jpg") || filename.endsWith("jpeg")) {
            return ".jpg";
        } else if (filename.equals("png") || filename.equals("PNG")) {
            return ".png";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * 目录间文件拷贝，未传完时用临时文件表示
     *
     * @param targetDir
     *        dest full path
     * @param target
     *        target full path
     * @return
     */
    public static File moveFileToDirtmp(File targetDir, File target ) {
        File fnew = null;
        String tmpTarget = target.getName( ) + ".tmp";
        File desfile = new File( targetDir.getPath( ) + "/" + tmpTarget );
        try {
            org.apache.commons.io.FileUtils.copyFile( target, desfile );
            if( target.length( ) == desfile.length( ) ) {
                fnew = new File( targetDir.getPath( ), target.getName( ) );

                if( !fnew.exists( ) ) {
                    desfile.renameTo( fnew );
                } else {
                    desfile.delete( );
                }
            }
            if( target.exists( ) ) {
                if( !target.delete( ) ) {
                    logger.info( "target canonicalPath {}", target.getCanonicalPath( ) );
                }
            }
        } catch( IOException e ) {
            logger.error( e.getMessage( ) );
        }
        return desfile;

    }

    /***
     * 删除文件夹
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static  boolean delDirectory(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delDirectory(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                flag = true;
            }
        }
        file.delete();
        return flag;
    }

    /**
     * @Description: 根据文件名（路径+名称）删除文件
     * @author: suyutang
     * @param: [fileName]
     * @return: void
     * @Date: 17:06  2019/1/31
     */
    public static void deleteFile(File file){
        try{
            if(file.exists()){
                file.delete();
                logger.info( "{}文件删除成功！", file.getAbsolutePath());
            }else{
                logger.info( "{}文件不存在！", file.getAbsolutePath());
            }
        }catch(Exception e){
            logger.error( "{}文件删除失败！失败原因{}", file.getAbsolutePath(),e);
            e.printStackTrace();
        }
    }

    /**
     * @Author 郭丁志
     * @Description // 下载文件
     * @Date 01:59 2020-04-06
     * @Param [file, res]
     * @return void
     **/
    public static Integer download(File file, HttpServletResponse response,String filename) throws IOException {
        if (filename != null) {
            // 如果文件存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return 0;

                } catch (Exception e) {
                    logger.error("下载文件错误，error={}",e.getMessage());
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }
}
