package top.jpower.jpower.module.common.nacos;

import cn.hutool.core.exceptions.UtilException;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ReflectUtil;

import java.util.Set;

/**
 * NacosConstants
 *
 * @author mr.g
 **/
public interface NacosConstants {

    /** 配置文件类型 **/
    String FILE_EXTENSION = "yaml";

    /** 配置文件是否支持动态刷新 **/
    String CONFIG_REFRESH = "true";

    /** 分组 **/
    String CONFIG_GROUP = "DEFAULT_GROUP";

    /**
     * 默认公共配置文件名称
     **/
    String JPOWER = "jpower";

    /**
     * 获取实例
     *
     * @author mr.g
     * @return 实例子
     **/
    static NacosConstants getInstance(){
        Set<Class<?>> set = ClassUtil.scanPackageBySuper(StringPool.EMPTY, NacosConstants.class);
        if (Fc.isEmpty(set)){
            return new NacosConstants() {};
        }

        return set.stream().map(clz->{
            try {
                return (NacosConstants)ReflectUtil.newInstance(clz);
            } catch (UtilException e){
                return null;
            }
        }).filter(Fc::notNull).findFirst().get();
    }

    /**
     * 动态获取公共nacos地址
     *
     * @param profile 环境变量
     * @return addr
     */
    default String nacosProfileDataId(String profile) {
        if (Fc.isBlank(profile)){
            profile = JpowerConstants.DEV_CODE;
        }

        return JPOWER.concat(StringPool.DASH).concat(profile).concat(StringPool.DOT).concat(FILE_EXTENSION);
    }

    /**
     * 动态获取公共nacos地址
     *
     * @return addr
     */
    default String nacosDataId() {
        return JPOWER.concat(StringPool.DOT).concat(FILE_EXTENSION);
    }

}
