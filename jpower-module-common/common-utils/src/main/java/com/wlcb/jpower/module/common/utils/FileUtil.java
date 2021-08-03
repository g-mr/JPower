package com.wlcb.jpower.module.common.utils;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName FileUtils
 * @Description TODO 文件工具
 * @Author 郭丁志
 * @Date 2020-02-05 00:57
 * @Version 1.0
 */
@Slf4j
public class FileUtil extends org.apache.commons.io.FileUtils {

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 移动文件，未传完用临时文件代替
     *
     * @param targetDir
     *        dest full path
     * @param target
     *        target full path
     * @return
     */
    public static File moveFileToDirtmp(File target, File targetDir ) {
        File fnew = null;
        String tmpTarget = target.getName( ) + ".tmp";
        File desfile = new File( targetDir.getPath( ) + "/" + tmpTarget );
        try {
            copyFile( target, desfile );
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
                    log.info( "target canonicalPath {}", target.getCanonicalPath( ) );
                }
            }
        } catch( IOException e ) {
            log.error( e.getMessage( ) );
        }
        return desfile;

    }

    /**
     * @Author 郭丁志
     * @Description //TODO 创建文件夹，如果文件夹存在则建立新的名字并返回新文件夹
     * @Date 00:50 2020-04-30
     * @Param [dirFile]
     * @return java.io.File
     **/
    public static File creatDir(File dirFile) {
        return creatDir(dirFile,0);
    }
    private static File creatDir(File dirFile,int i) {
        if (dirFile.exists()){
            String path = dirFile.getAbsolutePath();
            if (i!=0){
                path = path.substring(0,path.length()-((i+"").length()+2));
            }
            i++;
            return creatDir(new File(path+"("+i+")"),i);
        }else {
            dirFile.mkdirs();
            return dirFile;
        }
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
                // 先删除文件夹里面的文件
                delDirectory(path + File.separator + tempList[i]);
                flag = true;
            }
        }
        file.delete();
        return flag;
    }

    /**
     * @Description: 删除文件
     * @author: suyutang
     * @param: [fileName]
     * @return: void
     * @Date: 17:06  2019/1/31
     */
    public static void deleteFile(File file){
        try{
            if(file.exists()){
                file.delete();
                log.info( "{}文件删除成功！", file.getAbsolutePath());
            }else{
                log.info( "{}文件不存在！", file.getAbsolutePath());
            }
        }catch(Exception e){
            log.error( "{}文件删除失败！失败原因{}", file.getAbsolutePath(),e);
            e.printStackTrace();
        }
    }

    public static Boolean download(byte[] bytes, HttpServletResponse response,String filename) throws UnsupportedEncodingException {
        if (Fc.isNotEmpty(bytes)){
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            try {
                OutputStream os = response.getOutputStream();
                os.write(bytes);
                Fc.closeQuietly(os);
                return true;
            } catch (IOException e) {
                log.error("下载byte文件错误，error={}",e.getMessage());
            }finally {

            }
        }
        return false;
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
                    log.error("下载文件错误，error={}",e.getMessage());
                } finally {
                    Fc.closeQuietly(bis);
                    Fc.closeQuietly(fis);
                }
            }
        }
        return -1;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 压缩文件夹
     * @Date 02:09 2020-04-30
     * @Param [srcDir :压缩文件夹全路径, file ：压缩文件, keepDirStructure：是否保留原文件夹格式]
     * @return void
     **/
    public static void toZip(String srcDir,File file,boolean keepDirStructure){

        ZipOutputStream zos = null ;
        try {
            FileOutputStream out= new FileOutputStream(file);
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),keepDirStructure);
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            log.error("压缩出错：{}",e.getMessage());
        }finally{
            Fc.closeQuietly(zos);
        }

    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[1024*2];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            //是文件夹
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + File.separator));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + File.separator + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }

                }
            }
        }
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename){
        return filename.matches(FILENAME_PATTERN);
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
            path = path.substring(0, i);
            path = path.replaceFirst("file:", "");
            path = new File(path).getParentFile().getAbsolutePath();
            path = path + File.separator + "config";
        }
        // 项目所在的目录
        return new File(path).getAbsolutePath();
    }
}
