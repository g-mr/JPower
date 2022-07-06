package com.wlcb.jpower.module.common.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ZipUtil;
import com.wlcb.jpower.module.common.support.FileType;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author mr.g
 **/
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
     * 如果已打成jar包，则返回jar包所在目录;如果未打成jar，则返回resources所在目录
     *
     * @author mr.g
     * @return 目录
     **/
    public static String getSysRootPath() {
        // 项目的编译文件的根目录
        String path;
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
     *
     * @author mr.g
     * @param file 文件
     * @return 文件
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

    /**
     * 保存上传文件
     *
     * @author mr.g
     * @param file 上传文件
     * @param savePath 保存路径
     * @return 文件路径
     **/
    public static File saveFile(MultipartFile file, String savePath) throws IOException{
        return saveFile(file,null,savePath);
    }

    /**
     * 保存上传文件
     *
     * @author mr.g
     * @param file 上传文件
     * @param fileSuffixName 支持得文件后缀
     * @param savePath 保存路径
     * @return 文件路径
     **/
    public static File saveFile(MultipartFile file,String fileSuffixName,String savePath) throws IOException {
        return saveFile(file, fileSuffixName, savePath,null);
    }

    /**
     * 保存上传文件
     *
     * @author mr.g
     * @param multipartFile 上传文件
     * @param fileSuffixName 支持得文件后缀
     * @param savePath 保存路径
     * @param fileName 文件名称
     * @return 文件路径
     **/
    public static File saveFile(MultipartFile multipartFile,String fileSuffixName,String savePath,String fileName) throws IOException {

        if (Fc.isBlank(fileName)){
            fileName = FileNameUtil.getPrefix(multipartFile.getOriginalFilename());
        }
        //获得文件后缀名
        String suffixName=FileNameUtil.getSuffix(multipartFile.getOriginalFilename());
        if (StringUtils.isNotBlank(fileSuffixName) && !StringUtils.containsIgnoreCase(fileSuffixName,suffixName) && !StringUtils.containsIgnoreCase(fileSuffixName, FileType.getFileType(multipartFile.getInputStream()))){
            throw new IllegalArgumentException("不支持的后缀类型");
        }

        File file = FileUtil.rename(new File(savePath+File.separator+DateUtil.today() + File.separator + fileName + "." + suffixName));

        assert file != null;
        if(!file.getParentFile().exists()){
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
        }

        multipartFile.transferTo(file);

        log.info("文件保存成功，文件路径={}",file.getAbsolutePath());

        return new File(savePath + File.separator + DateUtil.today() + File.separator + file.getName());
    }

    /**
     * 读取上传文件内容
     *
     * @author mr.g
     * @param multipartFile 上传文件
     * @param chares 编码
     * @return 文件内容
     **/
    @SneakyThrows(IOException.class)
    public static List<String> readContent(MultipartFile multipartFile, String chares){

        List<String> list = new ArrayList<>();

        @Cleanup Reader reader = new InputStreamReader(multipartFile.getInputStream(), chares);
        BufferedReader br = new BufferedReader( reader);
        String line;
        while ((line = br.readLine()) != null) {
            // 一次读入一行数据
            if(Fc.isNotBlank(line)){
                list.add(line);
            }
        }

        return list;
    }

}
