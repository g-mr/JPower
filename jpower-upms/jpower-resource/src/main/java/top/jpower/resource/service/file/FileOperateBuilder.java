package top.jpower.resource.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.jpower.common.enums.OssCategoryEnum;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.utils.Fc;
import top.jpower.resource.dbs.dao.ResourceFileDao;
import top.jpower.resource.dbs.dao.ResourceOssDao;
import top.jpower.resource.dbs.entity.ResourceOss;
import top.jpower.resource.service.file.storage.*;

import java.util.Map;

import static top.jpower.common.constants.ServiceCodeConstants.INVALID_STORAGE_TYPE;

/**
 * 文件操作构建器
 * <p>
 * 用于获取不同类型的文件操作实现类
 * </p>
 *
 * @author mr.g
 */
@Component
@RequiredArgsConstructor
public class FileOperateBuilder {

    /**
     * FileUpload缓存池
     */
    private final Map<String, FileOperate> uploadPool;
    private final ResourceFileDao resourceFileDao;
    private final ResourceOssDao resourceOssDao;

    /**
     * 获取文件操作实现类
     *
     * @author mr.g
     * @param storageType 存储类型
     * @return FileOperate 文件操作实现类
     */
    public synchronized FileOperate getBuilder(String storageType) {
        FileOperate fileUpload = uploadPool.get(storageType);

        if (Fc.isEmpty(fileUpload)){
            ResourceOss resourceOss = resourceOssDao.getDefaultByCode(storageType);
            switch (OssCategoryEnum.getEnum(resourceOss.getCategory())){
                case SERVER:
                    fileUpload = new ServerFileOperate(resourceOss, resourceFileDao);
                    uploadPool.put(resourceOss.getCode(), fileUpload);
                    break;
                case FASTDFS:
                    fileUpload = new FastDfsFileOperate(resourceOss, resourceFileDao);
                    uploadPool.put(resourceOss.getCode(), fileUpload);
                    break;
                case ALI:
                    fileUpload = new OssAliFileOperate(resourceOss, resourceFileDao);
                    uploadPool.put(resourceOss.getCode(), fileUpload);
                    break;
                case QN:
                    fileUpload = new OssQnFileOperate(resourceOss, resourceFileDao);
                    uploadPool.put(resourceOss.getCode(), fileUpload);
                    break;
                case AWS:
                    fileUpload = new OssAwsFileOperate(resourceOss, resourceFileDao);
                    uploadPool.put(resourceOss.getCode(), fileUpload);
                    break;
                default:
                    break;
            }
        }

        JpowerAssert.notNull(fileUpload, JpowerError.Arg,INVALID_STORAGE_TYPE);
        return fileUpload;
    }

    /**
     * 删除指定类型的存储实现
     *
     * @author mr.g
     * @param storageType 存储类型
     */
    public void removeBuilder(String storageType) {
        uploadPool.remove(storageType);
    }
}
