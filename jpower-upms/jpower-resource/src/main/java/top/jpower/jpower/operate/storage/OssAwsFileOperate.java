package top.jpower.jpower.operate.storage;

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
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.*;
import top.jpower.jpower.dbs.dao.TbResourceFileDao;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.operate.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * 亚马逊
 *
 * @author mr.g
 * @date 2024-12-11 14:39
 * @description
 */
@Slf4j
public class OssAwsFileOperate implements FileOperate {

    private final S3Client s3Client;
    private final TbResourceOss resourceOss;
    private final TbResourceFileDao fileDao;

    public OssAwsFileOperate(TbResourceOss resourceOss, TbResourceFileDao fileDao){
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
    public TbResourceFile upload(byte[] bytes, String name, Long size) {

        String type = FileTypeUtil.getType(IoUtil.toStream(bytes), name);
        String key = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN) + File.separator + IdUtil.objectId() + StringPool.DOT + type;

        PutObjectResponse result = s3Client.putObject(builder -> builder
                .bucket(resourceOss.getBucketName())
                .key(key), RequestBody.fromBytes(bytes));

        log.info("AWS上传完成===>{}", JSON.toJSONString(result));

        TbResourceFile coreFile = new TbResourceFile();
        coreFile.setFileType(type);
        coreFile.setFileSize(size);
        coreFile.setId(Fc.randomSnowFlakeId());
        coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), DefaultValConstants.FILE_DES_KEY));
        coreFile.setStorageType(resourceOss.getCode());
        coreFile.setPath(key);
        coreFile.setName(name);

        if (!fileDao.save(coreFile)){
            s3Client.deleteObject(builder -> builder.key(key).bucket(resourceOss.getBucketName()));
        }

        return coreFile;
    }

    @Override
    public Boolean download(TbResourceFile coreFile) throws IOException {

        @Cleanup ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(builder -> builder
                .bucket(resourceOss.getBucketName())
                .key(coreFile.getPath()));
        return FileUtil.download(responseInputStream, WebUtil.getResponse(), coreFile.getName());
    }

    @Override
    @SneakyThrows(IOException.class)
    public byte[] getByte(TbResourceFile coreFile) {
        @Cleanup ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(builder -> builder
                .bucket(resourceOss.getBucketName())
                .key(coreFile.getPath()));
        return IoUtil.readBytes(responseInputStream);
    }

    @Override
    public Boolean deleteFile(TbResourceFile tbCoreFile) {
        s3Client.deleteObject(builder -> builder.key(tbCoreFile.getPath()).bucket(resourceOss.getBucketName()));
        return Boolean.TRUE;
    }

    @Override
    public String getUrl(TbResourceFile coreFile) {
        String domain = StringUtil.removeAllSuffix(Fc.toStr(resourceOss.getExternalAddress(), resourceOss.getInternalAddress()), StringPool.SLASH);
        return StringUtil.concat(domain, StringPool.SLASH, StrUtil.replaceFirst(coreFile.getPath(), resourceOss.getBucketName() + StringPool.SLASH, StringPool.EMPTY));
    }

}
