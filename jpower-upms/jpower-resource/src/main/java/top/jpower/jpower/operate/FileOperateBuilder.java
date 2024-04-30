package top.jpower.jpower.operate;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import top.jpower.common.enums.FileStorageTypeEnum;
import top.jpower.common.enums.OssCategoryEnum;
import top.jpower.core.utils.utils.Fc;
import top.jpower.jpower.dbs.dao.TbResourceFileDao;
import top.jpower.jpower.dbs.dao.TbResourceOssDao;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.operate.properties.FileProperties;
import top.jpower.jpower.operate.storage.OssAliFileOperate;
import top.jpower.jpower.operate.storage.OssQnFileOperate;

import java.util.Map;

/**
 * @ClassName TokenGranterBuilder
 * @Description TODO 构造登录查询
 * @Author 郭丁志
 * @Date 2020-07-28 00:34
 * @Version 1.0
 */
@Component
@EnableConfigurationProperties(FileProperties.class)
@RequiredArgsConstructor
public class FileOperateBuilder {

    /**
     * FileUpload缓存池
     */
    private final Map<String, FileOperate> uploadPool;
    private final TbResourceFileDao resourceFileDao;
    private final TbResourceOssDao resourceOssDao;

    /**
     * 获取上传实现类
     *
     * @param storageType 存储位置
     * @return FileUpload
     */
    public synchronized FileOperate getBuilder(String storageType) {
        FileOperate fileUpload = uploadPool.get(Fc.toStr(storageType, FileStorageTypeEnum.SERVER.getValue()));

        if (Fc.isEmpty(fileUpload)){
            TbResourceOss resourceOss = resourceOssDao.getByCode(storageType);
            switch (OssCategoryEnum.getEnum(resourceOss.getCategory())){
                case ALI:
                    fileUpload = new OssAliFileOperate(resourceOss, resourceFileDao);
                    uploadPool.put(resourceOss.getCode(), fileUpload);
                    break;
                case QN:
                    fileUpload = new OssQnFileOperate(resourceOss, resourceFileDao);
                    uploadPool.put(resourceOss.getCode(), fileUpload);
                    break;
                default:
                    break;
            }
        }

        JpowerAssert.notNull(fileUpload, JpowerError.Arg,"storageType无效，请传递正确的storageType参数");
        return fileUpload;
    }

    /**
     * 删除存储
     *
     * @author mr.g
     * @param storageType 类型
     **/
    public void removeBuilder(String storageType) {
        uploadPool.remove(storageType);
    }
}
