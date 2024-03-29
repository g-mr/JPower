package com.wlcb.jpower.module.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.config.LoginUserContext;
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
     **/
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createUser", String.class, getUserName());
        this.strictInsertFill(metaObject, "updateUser", String.class, getUserName());
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "createOrg", String.class, getOrg());
        this.strictInsertFill(metaObject, "status", Integer.class, JpowerConstants.DB_STATUS_NORMAL);
        this.strictInsertFill(metaObject, "isDeleted", Boolean.class, Boolean.FALSE);
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 更新时候配置的字段
     * @Date 17:49 2020-07-09
     **/
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateUser", String.class, getUserName());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取当前登陆用户
     * @Date 17:49 2020-07-09
     **/
    private String getUserName(){
        return Fc.isBlank(LoginUserContext.getUserId())? RoleConstant.ANONYMOUS_ID:LoginUserContext.getUserId();
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 获取当前登陆用户的所在部门
     * @Date 17:49 2020-07-09
     **/
    private String getOrg(){
        return LoginUserContext.getOrgId();
    }

}
