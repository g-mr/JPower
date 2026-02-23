package top.jpower.resource.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;

import java.io.ByteArrayInputStream;

/**
 * FastDFS工具类
 * <p>
 * 提供FastDFS文件上传、下载、删除等操作的工具类
 * </p>
 *
 * @author mr.g
 */
@Slf4j
public class FileDfsUtil {

    static private final FastFileStorageClient STORAGE_CLIENT;

    static {
        STORAGE_CLIENT = SpringUtil.getBean(DefaultFastFileStorageClient.class);
    }

    /**
     * 上传图片文件并生成缩略图
     *
     * @author mr.g
     * @param multipartFile Multipart文件对象
     * @return String 上传后的文件路径
     */
    @SneakyThrows(Exception.class)
    public static String uploadImg(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().
                        lastIndexOf(".") + 1);
        StorePath storePath = STORAGE_CLIENT.uploadImageAndCrtThumbImage(
                multipartFile.getInputStream(),
                multipartFile.getSize(),originalFilename , null);
        return storePath.getFullPath() ;
    }

    /**
     * 文件上传
     *
     * @param bytes     文件字节
     * @param fileSize  文件大小
     * @param extension 文件扩展名
     * @return fastDFS中的文件名称 eg:group1/M00/01/04/CgMKrVvS0geAQ0pzAACAAJxmBeM793.doc
     */
    public static String upload(byte[] bytes, long fileSize, String extension) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        StorePath storePath = STORAGE_CLIENT.uploadFile(byteArrayInputStream, fileSize, extension, null);
        return storePath.getFullPath();
    }

    /**
     * 下载文件
     *
     * @author mr.g
     * @param fileUrl 文件URL
     * @return byte[] 文件字节数组
     */
    public static byte[] downloadFile(String fileUrl){
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = STORAGE_CLIENT.downloadFile(group, path, downloadByteArray);
        return bytes;
    }

    /**
     * 删除文件
     *
     * @author mr.g
     * @param fileUrl 文件URL
     * @return Boolean 删除结果
     */
    public static Boolean deleteFile(String fileUrl) {
        if (Fc.isBlank(fileUrl)) {
            log.warn("fileUrl == >>文件路径为空...");
            return false;
        }
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        STORAGE_CLIENT.deleteFile(storePath.getGroup(), storePath.getPath());
        return true;
    }
}