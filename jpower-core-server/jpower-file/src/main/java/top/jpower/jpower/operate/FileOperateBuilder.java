package top.jpower.jpower.operate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dbs.dao.TbCoreFileDao;
import top.jpower.jpower.dbs.dao.TbResourceOssDao;
import top.jpower.jpower.dbs.entity.TbResourceOss;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;
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
@RequiredArgsConstructor
public class FileOperateBuilder {

    /**
     * FileUpload缓存池
     */
    private final Map<String, FileOperate> uploadPool;
    private final TbCoreFileDao coreFileDao;
    private final TbResourceOssDao resourceOssDao;

    /**
     * 获取上传实现类
     *
     * @param storageType 存储位置
     * @return FileUpload
     */
    public synchronized FileOperate getBuilder(String storageType) {
        FileOperate fileUpload = uploadPool.get(Fc.toStr(storageType, ConstantsEnum.FILE_STORAGE_TYPE.SERVER.getValue()));

        if (Fc.isEmpty(fileUpload)){
            TbResourceOss resourceOss = resourceOssDao.getByCode(storageType);
            switch (ConstantsEnum.OSS_CATEGORY.getEnum(resourceOss.getCategory())){
                case ALI:
                    fileUpload = new OssAliFileOperate(resourceOss, coreFileDao);
                    uploadPool.put(resourceOss.getCode(), fileUpload);
                    break;
                case QN:
                    fileUpload = new OssQnFileOperate(resourceOss, coreFileDao);
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
