package com.wlcb.jpower.module.dbs.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName UpdateRelatedFieldsMetaHandler
 * @Description TODO 公用字段配置
 * @Author 郭丁志
 * @Date 2020-07-09 17:35
 * @Version 1.0
 */
@Component
public class UpdateRelatedFieldsMetaHandler implements MetaObjectHandler {

    /**
     * @Author 郭丁志
     * @Description //TODO 新增时配置的字段
     * @Date 17:48 2020-07-09
     * @Param [metaObject]
     * @return void
     **/
    @Override
    public void insertFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject, "createUser", String.class, getUserName());
        this.strictInsertFill(metaObject, "updateUser", String.class, getUserName());
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "status", Integer.class, 1);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 更新时候配置的字段
     * @Date 17:49 2020-07-09
     * @Param
     * @return
     **/
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateUser", String.class, getUserName());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

    private String getUserName(){
        String username = LoginUserContext.getUserName()==null?"":LoginUserContext.getUserName();
        return username;
    }

}
