package com.wlcb.jpower.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author mr.g
 * @date 2021-07-06 17:03
 */
@Slf4j
public class FileDfsUtil {

    static private FastFileStorageClient storageClient;

    static {
        storageClient = SpringUtil.getBean(DefaultFastFileStorageClient.class);
    }

    /**
     * 上传图片文件
     * 生成缩列图
     */
    @SneakyThrows
    public static String uploadImg(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().
                        lastIndexOf(".") + 1);
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(
                multipartFile.getInputStream(),
                multipartFile.getSize(),originalFilename , null);
        return storePath.getFullPath() ;
    }

    /**
     * 文件上传
     * 最后返回fastDFS中的文件名称;group1/M00/01/04/CgMKrVvS0geAQ0pzAACAAJxmBeM793.doc
     *
     * @param bytes     文件字节
     * @param fileSize  文件大小
     * @param extension 文件扩展名
     * @return fastDfs路径
     */
    public static String upload(byte[] bytes, long fileSize, String extension) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        StorePath storePath = storageClient.uploadFile(byteArrayInputStream, fileSize, extension, null);
        return storePath.getFullPath();
    }

    /**
     * 下载文件
     *  返回文件字节流大小
     * @param fileUrl 文件URL
     * @return 文件字节
     * @throws IOException
     */
    public static byte[] downloadFile(String fileUrl){
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = storageClient.downloadFile(group, path, downloadByteArray);
        return bytes;
    }

    /**
     * 删除文件
     */
    public static Boolean deleteFile(String fileUrl) {
        if (Fc.isBlank(fileUrl)) {
            log.warn("fileUrl == >>文件路径为空...");
            return false;
        }
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        return true;
    }
}