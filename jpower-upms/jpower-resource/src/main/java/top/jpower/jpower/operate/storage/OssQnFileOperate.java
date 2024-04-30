package top.jpower.jpower.operate.storage;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpConnection;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.core.utils.constants.StringPool;
import top.jpower.core.utils.utils.*;
import top.jpower.jpower.dbs.dao.TbResourceFileDao;
import top.jpower.jpower.dbs.entity.TbResourceFile;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.operate.FileOperate;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;

/**
 * 七牛云
 *
 * @author mr.g
 * @date 2024/4/21 5:09 PM
 */
@Slf4j
public class OssQnFileOperate implements FileOperate {

    /**
     * 构造一个带指定 Region 对象的配置类
     **/
    private final Configuration cfg = new Configuration(Region.autoRegion());
    private final Auth auth;

    private final TbResourceOss resourceOss;
    private final TbResourceFileDao fileDao;

    public OssQnFileOperate(TbResourceOss resourceOss, TbResourceFileDao fileDao){
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        this.auth = Auth.create(resourceOss.getAccessKey(), resourceOss.getSecretKey());

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
    @SneakyThrows(QiniuException.class)
    public TbResourceFile upload(byte[] bytes, String name, Long size) {
        String type = FileTypeUtil.getType(IoUtil.toStream(bytes), name);

        String key = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN) + File.separator + IdUtil.objectId() + StringPool.DOT + type;

        UploadManager uploadManager = new UploadManager(cfg);
        String upToken = auth.uploadToken(resourceOss.getBucketName());
        Response response = uploadManager.put(bytes, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

        log.info("七牛云上传完成===>{},{}", putRet.key, putRet.hash);

        TbResourceFile coreFile = new TbResourceFile();
        coreFile.setFileType(type);
        coreFile.setFileSize(size);
        coreFile.setId(Fc.randomSnowFlakeId());
        coreFile.setMark(DesUtil.encrypt(Fc.toStr(coreFile.getId()), DefaultValConstants.FILE_DES_KEY));
        coreFile.setStorageType(resourceOss.getCode());
        coreFile.setPath(resourceOss.getBucketName() + File.separator + putRet.key);
        coreFile.setName(name);

        if (!fileDao.save(coreFile)){
            BucketManager bucketManager = new BucketManager(auth, cfg);
            bucketManager.delete(resourceOss.getBucketName(), key);
        }

        return coreFile;
    }

    private String getBucketNameByPath(String path){
        List<String> list = StringUtil.split(path, File.separator, 2, Boolean.TRUE, Boolean.TRUE);
        return list.get(0);
    }

    private String getFileNameByPath(String path){
        List<String> list = StringUtil.split(path, File.separator, 2, Boolean.TRUE, Boolean.TRUE);
        return list.get(1);
    }

    /**
     * 下载文件
     *
     * @param coreFile
     * @return java.lang.Boolean
     * @Author mr.g
     **/
    @Override
    public Boolean download(TbResourceFile coreFile) throws IOException {
        HttpConnection connection = HttpConnection.create(getUrl(coreFile), Proxy.NO_PROXY);
        return FileUtil.download(connection.connect().getInputStream(), WebUtil.getResponse(), coreFile.getName());
    }

    /**
     * 获取文件字节
     *
     * @param coreFile
     * @return byte[]
     * @Author mr.g
     **/
    @Override
    public byte[] getByte(TbResourceFile coreFile) {
        return HttpUtil.downloadBytes(getUrl(coreFile));
    }


    /**
     * 删除文件
     *
     * @param coreFile
     * @return java.lang.Boolean
     * @Author mr.g
     **/
    @Override
    @SneakyThrows(QiniuException.class)
    public Boolean deleteFile(TbResourceFile coreFile) {
        BucketManager bucketManager = new BucketManager(auth, cfg);
        @Cleanup Response response = bucketManager.delete(getBucketNameByPath(coreFile.getPath()), getFileNameByPath(coreFile.getPath()));
        return response.isOK();
    }

    /**
     * 获取文件外链
     **/
    @Override
    public String getUrl(TbResourceFile coreFile) {
        String domain = StringUtil.removeAllSuffix(resourceOss.getExternalAddress(), StringPool.SLASH);
        return StringUtil.concat(domain, StringPool.SLASH, getFileNameByPath(coreFile.getPath()));
    }
}
