package top.jpower.jpower.module.common.utils.constants;

import cn.hutool.core.collection.ListUtil;
import top.jpower.jpower.module.common.utils.CacheMap;
import top.jpower.jpower.module.common.utils.Fc;

import java.util.List;
import java.util.ServiceLoader;

/**
 * 环境常量
 *
 * @author mr.g
 **/
public interface AppConstant {

    /**
     * 开发环境
     */
    String DEV_CODE = "dev";
    /**
     * 生产环境
     */
    String PROD_CODE = "prod";
    /**
     * 测试环境
     */
    String TEST_CODE = "test";

    /**
     * 代码部署于 linux 上，工作默认为 mac 和 Windows
     */
    String OS_NAME_LINUX = "LINUX";

    /** 项目名称 **/
    String JPOWER = "jpower";

    /** spring boot admin **/
    String JPOWER_ADMIN = JPOWER + "-admin";

    /** swagger聚合文档 **/
    String JPOWER_DOC = JPOWER + "-doc";

    /** 日志服务 **/
    String JPOWER_LOG = JPOWER + "-log";

    /** 网关模块名称 **/
    String JPOWER_GATEWAY = JPOWER + "-gateway";

    /** 系统模块名称 **/
    String JPOWER_SYSTEM = JPOWER + "-system";

    /** 鉴权模块 **/
    String JPOWER_AUTH = JPOWER + "-auth";

    /** 用户模块 **/
    String JPOWER_USER = JPOWER + "-user";

    /** 文件模块名称 **/
    String JPOWER_RESOURCE = JPOWER + "-resource";

    String getJpower();

    String getJpowerSystem();

    String getJpowerLog();

    String getJpowerUser();

    String getJpowerAuth();

    String getJpowerResource();

    String getJpowerAdmin();

    String getJpowerDoc();

    String getJpowerGateway();

    static AppConstant getInstance(){

        //查看缓存中的AppConstant
        CacheMap<String,AppConstant> cacheMap = CacheMap.getInstance("APP_NAME");
        AppConstant app = cacheMap.get("appInstance");
        if (Fc.notNull(app)){
            return app;
        }

        //获取项目实现的AppConstant
        synchronized (AppConstant.class){
            List<AppConstant> apps = ListUtil.list(true);
            ServiceLoader.load(AppConstant.class).forEach(apps::add);
            if (apps.size() > 0){
                cacheMap.put("appInstance",apps.get(0));
                return apps.get(0);
            }
        }

        //默认返回的AppConstant
        AppConstant ac = new AppConstant() {
            @Override
            public String getJpower() {
                return JPOWER;
            }

            @Override
            public String getJpowerSystem(){
                return JPOWER_SYSTEM;
            }

            @Override
            public String getJpowerLog(){
                return JPOWER_LOG;
            }

            @Override
            public String getJpowerUser(){
                return JPOWER_USER;
            }

            @Override
            public String getJpowerAuth(){
                return JPOWER_AUTH;
            }

            @Override
            public String getJpowerResource(){
                return JPOWER_RESOURCE;
            }

            @Override
            public String getJpowerAdmin() {
                return JPOWER_ADMIN;
            }

            @Override
            public String getJpowerDoc() {
                return JPOWER_DOC;
            }

            @Override
            public String getJpowerGateway() {
                return JPOWER_GATEWAY;
            }
        };

        cacheMap.put("appInstance",ac);
        return ac;
    }

}
