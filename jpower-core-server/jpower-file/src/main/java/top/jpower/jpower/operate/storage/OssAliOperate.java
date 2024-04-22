package top.jpower.jpower.operate.storage;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import top.jpower.jpower.dbs.dao.TbCoreFileDao;
import top.jpower.jpower.dbs.entity.TbCoreFile;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.module.common.utils.DateUtil;
import top.jpower.jpower.module.common.utils.DesUtil;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.SnowFlakeIdUtil;
import top.jpower.jpower.module.common.utils.constants.ConstantsUtils;
import top.jpower.jpower.operate.FileOperate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author mr.g
 * @date 2024/4/21 5:09 PM
 */
@Slf4j
public class OssAliOperate implements FileOperate {

    private final OSS ossClient;
    private final TbResourceOss resourceOss;
    private final TbCoreFileDao fileDao;

    public OssAliOperate(TbResourceOss resourceOss, TbCoreFileDao fileDao){
        ossClient = new OSSClientBuilder().build(resourceOss.getInternalAddress(), CredentialsProviderFactory.newDefaultCredentialProvider(resourceOss.getAccessKey(),resourceOss.getSecretKey()));
        this.resourceOss = resourceOss;
        this.fileDao = fileDao;
    }

    /**
     * 上传文件
     *
     * @param bytes
     * @param name
     * @param size
     * @return TbCoreFile
     */
    @Override
    public TbCoreFile upload(byte[] bytes, String name, Long size) {

        String objectName = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN) + File.separator + SnowFlakeIdUtil.nextIdStr();

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(resourceOss.getBucketName(), objectName, new ByteArrayInputStream(bytes));

        // 创建PutObject请求。
        PutObjectResult result = ossClient.putObject(putObjectRequest);


        TbCoreFile coreFile = new TbCoreFile();
        coreFile.setFileType(FileTypeUtil.getType(IoUtil.toStream(bytes), name));
        coreFile.setFileSize(size);
        coreFile.setId(Fc.randomSnowFlakeId());
        coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), ConstantsUtils.FILE_DES_KEY));
        coreFile.setStorageType(resourceOss.getCode());
        coreFile.setPath(resourceOss.getBucketName()+File.separator+objectName);
        coreFile.setName(name);

        if (!fileDao.save(coreFile)){
            ossClient.deleteObject(resourceOss.getBucketName(), objectName);
        }

        return coreFile;
    }

    /**
     * 下载文件
     *
     * @param coreFile
     * @return java.lang.Boolean
     * @Author mr.g
     **/
    @Override
    public Boolean download(TbCoreFile coreFile) throws IOException {
        return null;
    }

    /**
     * 获取文件字节
     *
     * @param coreFile
     * @return byte[]
     * @Author mr.g
     **/
    @Override
    public byte[] getByte(TbCoreFile coreFile) {
        return new byte[0];
    }

    /**
     * 删除文件
     *
     * @param tbCoreFile
     * @return java.lang.Boolean
     * @Author mr.g
     **/
    @Override
    public Boolean deleteFile(TbCoreFile tbCoreFile) {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        log.info("阿里云OSS客户端关闭......");
        ossClient.shutdown();
        super.finalize(); // 调用父类的finalize()方法
    }
}
