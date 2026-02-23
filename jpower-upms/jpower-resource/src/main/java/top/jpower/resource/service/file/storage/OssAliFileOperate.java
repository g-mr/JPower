package top.jpower.resource.service.file.storage;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.*;
import top.jpower.resource.dbs.dao.ResourceFileDao;
import top.jpower.resource.dbs.entity.ResourceFile;
import top.jpower.resource.dbs.entity.ResourceOss;
import top.jpower.resource.service.file.FileOperate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 阿里云
 *
 * @author mr.g
 * @date 2024/4/21 5:09 PM
 */
@Slf4j
public class OssAliFileOperate implements FileOperate {

    private final OSS ossClient;
    private final ResourceOss resourceOss;
    private final ResourceFileDao fileDao;

    public OssAliFileOperate(ResourceOss resourceOss, ResourceFileDao fileDao){
        ossClient = new OSSClientBuilder().build(resourceOss.getInternalAddress(), CredentialsProviderFactory.newDefaultCredentialProvider(resourceOss.getAccessKey(),resourceOss.getSecretKey()));
        this.resourceOss = resourceOss;
        this.fileDao = fileDao;
    }

    /**
     * 上传文件
     *
     * @param bytes 文件字节数组
     * @param name 文件名
     * @param size 文件大小
     * @return TbCoreFile
     */
    @Override
    public ResourceFile upload(byte[] bytes, String name, Long size) {

        String type = FileTypeUtil.getType(IoUtil.toStream(bytes), name);

        String objectName = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN) + File.separator + IdUtil.objectId() + StringPool.DOT + type;

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(resourceOss.getBucketName(), objectName, new ByteArrayInputStream(bytes));

        // 创建PutObject请求。
        PutObjectResult result = ossClient.putObject(putObjectRequest);

        log.info("阿里云上传完成===>{}", JSON.toJSONString(result));

        ResourceFile coreFile = new ResourceFile();
        coreFile.setFileType(type);
        coreFile.setFileSize(size);
        coreFile.setId(Fc.randomSnowFlakeId());
        coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), DefaultValConstants.FILE_DES_KEY));
        coreFile.setStorageType(resourceOss.getCode());
        coreFile.setPath(resourceOss.getBucketName() + File.separator + objectName);
        coreFile.setName(name);

        if (!fileDao.save(coreFile)){
            ossClient.deleteObject(resourceOss.getBucketName(), objectName);
        }

        return coreFile;
    }

    private String getBucketNameByPath(String path){
        List<String> list = StringUtil.split(path, File.separator, 2, Boolean.TRUE, Boolean.TRUE);
        return list.get(0);
    }

    private String getObjectNameByPath(String path){
        List<String> list = StringUtil.split(path, File.separator, 2, Boolean.TRUE, Boolean.TRUE);
        return list.get(1);
    }

    /**
     * 下载文件
     *
     * @param coreFile 文件实体
     * @return Boolean 下载结果
     * @author mr.g
     * @throws IOException IO异常
     */
    @Override
    public Boolean download(ResourceFile coreFile) throws IOException {
        String bucketName = getBucketNameByPath(coreFile.getPath());
        String objectName = getObjectNameByPath(coreFile.getPath());

        // ossObject包含文件所在的存储空间名称、文件名称、文件元数据以及一个输入流。
        @Cleanup OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        @Cleanup InputStream inputStream = ossObject.getObjectContent();
        return FileUtil.download(inputStream, WebUtil.getResponse(), coreFile.getName());
    }

    /**
     * 获取文件字节
     *
     * @param coreFile 文件实体
     * @return byte[] 文件字节数组
     * @author mr.g
     */
    @Override
    @SneakyThrows(IOException.class)
    public byte[] getByte(ResourceFile coreFile) {

        String bucketName = getBucketNameByPath(coreFile.getPath());
        String objectName = getObjectNameByPath(coreFile.getPath());

        // ossObject包含文件所在的存储空间名称、文件名称、文件元数据以及一个输入流。
        @Cleanup OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        @Cleanup InputStream inputStream = ossObject.getObjectContent();
        return IoUtil.readBytes(inputStream);
    }

    /**
     * 删除文件
     *
     * @param coreFile 文件实体
     * @return Boolean 删除结果
     * @author mr.g
     */
    @Override
    public Boolean deleteFile(ResourceFile coreFile) {
        ossClient.deleteObject(getBucketNameByPath(coreFile.getPath()), getObjectNameByPath(coreFile.getPath()));
        return Boolean.TRUE;
    }

    /**
     * 获取文件外链
     **/
    @Override
    public String getUrl(ResourceFile coreFile) {
        String domain = StringUtil.removeAllSuffix(Fc.toStr(resourceOss.getExternalAddress(), resourceOss.getInternalAddress()), StringPool.SLASH);
        return StringUtil.concat(domain, StringPool.SLASH, StrUtil.replaceFirst(coreFile.getPath(), resourceOss.getBucketName() + StringPool.SLASH, StringPool.EMPTY));
    }

    @Override
    protected void finalize() throws Throwable {
        log.info("阿里云OSS客户端关闭......");
        ossClient.shutdown();
        super.finalize(); // 调用父类的finalize()方法
    }
}
