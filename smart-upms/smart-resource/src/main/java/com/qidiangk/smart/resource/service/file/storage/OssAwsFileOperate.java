package com.qidiangk.smart.resource.service.file.storage;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import com.qidiangk.smart.common.constants.DefaultValConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.*;
import com.qidiangk.smart.resource.dbs.dao.ResourceFileDao;
import com.qidiangk.smart.resource.dbs.entity.ResourceFile;
import com.qidiangk.smart.resource.dbs.entity.ResourceOss;
import com.qidiangk.smart.resource.service.file.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * AWS S3文件操作实现
 * <p>
 * 使用AWS S3作为存储服务的文件操作实现类
 * </p>
 *
 * @author mr.g
 */
@Slf4j
public class OssAwsFileOperate implements FileOperate {

    private final S3Client s3Client;
    private final ResourceOss resourceOss;
    private final ResourceFileDao fileDao;

    public OssAwsFileOperate(ResourceOss resourceOss, ResourceFileDao fileDao){
        s3Client = S3Client.builder().credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return resourceOss.getAccessKey();
                    }

                    @Override
                    public String secretAccessKey() {
                        return resourceOss.getSecretKey();
                    }
                })
                .region(Region.of(resourceOss.getRegion()))
                .serviceConfiguration(builder -> builder
                        .multiRegionEnabled(Boolean.TRUE)
                )
                .build();

        this.resourceOss = resourceOss;
        this.fileDao = fileDao;
    }

    @Override
    public ResourceFile upload(byte[] bytes, String name, Long size, Long groupId) {

        String type = FileTypeUtil.getType(IoUtil.toStream(bytes), name);
        String key = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN) + File.separator + IdUtil.objectId() + StringPool.DOT + type;

        PutObjectResponse result = s3Client.putObject(builder -> builder
                .bucket(resourceOss.getBucketName())
                .key(key), RequestBody.fromBytes(bytes));

        log.info("AWS上传完成===>{}", JSON.toJSONString(result));

        ResourceFile coreFile = new ResourceFile();
        coreFile.setFileType(type);
        coreFile.setFileSize(size);
        coreFile.setId(Fc.randomSnowFlakeId());
        coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), DefaultValConstants.FILE_DES_KEY));
        coreFile.setStorageType(resourceOss.getCode());
        coreFile.setPath(key);
        coreFile.setName(name);
		coreFile.setGroupId(groupId);

        if (!fileDao.save(coreFile)){
            s3Client.deleteObject(builder -> builder.key(key).bucket(resourceOss.getBucketName()));
        }

        return coreFile;
    }

    @Override
    public Boolean download(ResourceFile coreFile) throws IOException {

        @Cleanup ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(builder -> builder
                .bucket(resourceOss.getBucketName())
                .key(coreFile.getPath()));
        return FileUtil.download(responseInputStream, WebUtil.getResponse(), coreFile.getName());
    }

    @Override
    @SneakyThrows(IOException.class)
    public byte[] getByte(ResourceFile coreFile) {
        @Cleanup ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(builder -> builder
                .bucket(resourceOss.getBucketName())
                .key(coreFile.getPath()));
        return IoUtil.readBytes(responseInputStream);
    }

    @Override
    public Boolean deleteFile(ResourceFile tbCoreFile) {
        s3Client.deleteObject(builder -> builder.key(tbCoreFile.getPath()).bucket(resourceOss.getBucketName()));
        return Boolean.TRUE;
    }

    @Override
    public String getUrl(ResourceFile coreFile) {
        String domain = StringUtil.removeAllSuffix(Fc.toStr(resourceOss.getExternalAddress(), resourceOss.getInternalAddress()), StringPool.SLASH);
        return StringUtil.concat(domain, StringPool.SLASH, StrUtil.replaceFirst(coreFile.getPath(), resourceOss.getBucketName() + StringPool.SLASH, StringPool.EMPTY));
    }

}
