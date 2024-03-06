package top.jpower.jpower.operate;

import org.springframework.stereotype.Component;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.module.common.utils.constants.ConstantsEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName TokenGranterBuilder
 * @Description TODO 构造登录查询
 * @Author 郭丁志
 * @Date 2020-07-28 00:34
 * @Version 1.0
 */
@Component
public class FileOperateBuilder {

    /**
     * FileUpload缓存池
     */
    private final Map<String, FileOperate> uploadPool = new ConcurrentHashMap<>();

    public FileOperateBuilder(Map<String, FileOperate> uploadPool) {
        this.uploadPool.putAll(uploadPool);
    }

    /**
     * 获取上传实现类
     *
     * @param storageType 存储位置
     * @return FileUpload
     */
    public FileOperate getBuilder(String storageType) {
        FileOperate fileUpload = uploadPool.get(Fc.toStr(storageType, ConstantsEnum.FILE_STORAGE_TYPE.SERVER.getValue()));
        JpowerAssert.notNull(fileUpload, JpowerError.Arg,"storageType无效，请传递正确的storageType参数");
        return fileUpload;
    }

}
