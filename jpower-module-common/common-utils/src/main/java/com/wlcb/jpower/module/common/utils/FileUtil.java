package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ZipUtil;
import com.wlcb.jpower.module.common.support.NamedThreadFactory;
import com.wlcb.jpower.module.common.utils.constants.CharPool;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * @ClassName FileUtils
 * @Description TODO 文件工具
 * @Author 郭丁志
 * @Date 2020-02-05 00:57
 * @Version 1.0
 */
@Slf4j
public class FileUtil extends cn.hutool.core.io.FileUtil {

    /**
     * 创建文件夹，如果文件夹存在则对文件夹重命名后创建，会把新的文件夹返回
     *
     * @author mr.g
     * @param dir 目录
     * @return 创建的目录
     **/
    public static File mkdirCycle(File dir) {
        return mkdirCycle(dir,0);
    }

    /**
     * 创建文件夹，如果文件夹存在则对文件夹重命名后创建，会把新的文件夹返回
     *
     * @author mr.g
     * @param dir 目录
     * @param index 文件重命名后缀下标
     * @return 创建的目录
     **/
    private static File mkdirCycle(File dir,int index) {
        if (dir.exists()){
            String path = dir.getAbsolutePath();
            if (index!=0){
                path = path.substring(0,path.length()-((index+"").length()+2));
            }
            index++;
            return mkdirCycle(new File(path+"("+index+")"),index);
        }else {
            return mkdir(dir);
        }
    }

    /**
     * 文件删除
     *
     * @author mr.g
     * @param file 文件
     **/
    public static void deleteFile(File file){
        try{
            if(del(file)){
                log.info( "{}文件删除成功！", file.getAbsolutePath());
            }else{
                log.info( "{}文件不存在！", file.getAbsolutePath());
            }
        }catch(IORuntimeException e){
            log.error( "{}文件删除失败！失败原因{}", file.getAbsolutePath(),e);
        }
    }

    /**
     * 文件下载
     *
     * @author mr.g
     * @param bytes byte内容
     * @param response HttpServletResponse
     * @param fileName 下载后的文件名
     * @return 是否下载成功
     **/
    public static Boolean download(byte[] bytes, HttpServletResponse response,String fileName) throws UnsupportedEncodingException {

        if (Fc.isNotEmpty(bytes)){
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            try {
                OutputStream os = response.getOutputStream();
                os.write(bytes);
                Fc.closeQuietly(os);
                return true;
            } catch (IOException e) {
                log.error("下载byte文件错误，{}error={}",StringPool.NEWLINE,ExceptionUtil.getStackTraceAsString(e));
            }
        }
        return false;
    }

    /**
     * 文件下载
     *
     * @author mr.g
     * @param file 文件
     * @param response HttpServletResponse
     * @param fileName 下载后的文件名
     * @return 是否下载成功
     **/
    public static boolean download(File file, HttpServletResponse response,String fileName) throws IOException {
        if (fileName != null) {
            // 如果文件存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
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
                    return true;
                } catch (Exception e) {
                    log.error("下载文件错误，{}error={}",StringPool.NEWLINE,ExceptionUtil.getStackTraceAsString(e));
                } finally {
                    Fc.closeQuietly(bis);
                    Fc.closeQuietly(fis);
                }
            }
        }
        return false;
    }

    /**
     * 压缩文件
     *
     * @author mr.g
     * @param srcFile 原文件夹
     * @param zipFile 压缩文件
     **/
    public static void toZip(File srcFile,File zipFile){
        ZipUtil.zip(srcFile.getAbsolutePath(),zipFile.getAbsolutePath());
    }

    /**
     * 文件名称验证
     *
     * @param fileName 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String fileName){
        return !FileNameUtil.containsInvalid(fileName);
    }









    /**
     * @Author 郭丁志
     * @Description //TODO 追加内容到一个文件中 多线程
     * @Date 17:44 2020-06-28
     * @Param [content, filePath]
     * @return boolean
     **/
    public static boolean saveSendMobileFileTemp(final String content, final String filePath)throws Exception{
        boolean flag = false;
        ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1024),new NamedThreadFactory("FileUtil"));
        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean b = saveSendMobileFile(content, filePath);
                return b;
            }
        });
        executorService.execute(futureTask);
        try {
            //设置超时时间
            flag = futureTask.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            futureTask.cancel(true);
            throw e;
        }finally {
            executorService.shutdown();
        }
        return flag;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 追加内容到一个文件中
     * @Date 17:43 2020-06-28
     * @Param [content, filePath]
     * @return boolean
     **/
    public static boolean saveSendMobileFile(String content,String filePath)throws Exception{
        boolean flag = false;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try{
            //判断根目录是否存在
            File synMobileFile = new File(filePath);
            File fileParent = synMobileFile.getParentFile();
            //如果目录不存在则创建
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            if(!synMobileFile.exists()){
                synMobileFile.createNewFile();
            }
            fos = new FileOutputStream(synMobileFile,true);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(content);
            bw.newLine();
            bw.flush();
            flag = true;
            log.info( "{}文件写入成功！", filePath);
        }catch(Exception e){
            log.error( "{}文件写入出现异常，异常原因：{} ", filePath,e);
            throw new IOException();
        }finally {
            Fc.closeQuietly(bw);
            Fc.closeQuietly(osw);
            Fc.closeQuietly(fos);
        }
        return flag;
    }

    /**
     * 获取文件后缀名
     * @param fullName 文件全名
     * @return {String}
     */
    public static String getFileExtension(String fullName) {
        Assert.notNull(fullName, "file fullName is null.");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 获取文件名，去除后缀名
     * @param file 文件
     * @return {String}
     */
    public static String getNameWithoutExtension(String file) {
        Assert.notNull(file, "file is null.");
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(CharPool.DOT);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * 如果已打成jar包，则返回jar包所在目录
     * 如果未打成jar，则返回resources所在目录
     * @return
     */
    public static String getSysRootPath() {
        // 项目的编译文件的根目录
        String path = null;
        try {
            path = URLDecoder.decode(FileUtil.class.getResource(StringPool.SLASH).getPath(), String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (path.startsWith("file:")) {
            int i = path.indexOf(".jar!");
            if(i<=0){
                // 如果未找到jar包则尝试找war包
                i = path.indexOf(".war!");
            }
            path = path.substring(0, i);
            path = path.replaceFirst("file:", "");
            path = new File(path).getParentFile().getAbsolutePath();
            path = path + File.separator + "config";
        }
        // 项目所在的目录
        return new File(path).getAbsolutePath();
    }

    /**
     * 修改文件名称，检测目录下是否有同名，有则改名没有则返回
     * @Author mr.g
     * @param file
     * @return java.io.File
     **/
    public static File rename(File file) {

        if (Fc.isNull(file)) {
            return null;
        }

        File parentDir = file.getParentFile();
        if (ArrayUtil.isEmpty(parentDir.listFiles((dir, name) -> Fc.equalsValue(name,file.getName())))){
            return file;
        }else {
            String fileNamePrefix = FileNameUtil.getPrefix(file);

            String num = StringUtil.subBetween(fileNamePrefix,StringPool.LEFT_BRACKET,StringPool.RIGHT_BRACKET,true);
            if (Fc.isBlank(num) || !NumberUtil.isInteger(num)){
                fileNamePrefix = fileNamePrefix + StringPool.LEFT_BRACKET + "0" + StringPool.RIGHT_BRACKET;
            }else {
                fileNamePrefix = StringUtil.replace(fileNamePrefix,StringPool.LEFT_BRACKET + num + StringPool.RIGHT_BRACKET,StringPool.LEFT_BRACKET + Fc.toStr(NumberUtil.add(num,"1")) + StringPool.RIGHT_BRACKET);
            }

            return rename(new File(parentDir.getAbsolutePath() + File.separator + fileNamePrefix + StringPool.DOT + FileNameUtil.getSuffix(file)));
        }
    }

}
