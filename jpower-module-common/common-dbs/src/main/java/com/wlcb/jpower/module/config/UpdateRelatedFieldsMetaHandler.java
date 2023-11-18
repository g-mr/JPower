package com.wlcb.jpower.module.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.config.LoginUserContext;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 公用字段配置
 *
 * @Author 郭丁志
 * @Date 2020-07-09 17:35
 */
public class UpdateRelatedFieldsMetaHandler implements MetaObjectHandler {

    /**
     * 新增时配置的字段
     *
     * @Author 郭丁志
     * @Date 17:48 2020-07-09
     **/
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createUser", Long.class, getUserId());
        this.strictInsertFill(metaObject, "updateUser", Long.class, getUserId());
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "createOrg", Long.class, getOrg());
        this.strictInsertFill(metaObject, "isDeleted", Boolean.class, Boolean.FALSE);
    }

    /**
     * 更新时候配置的字段
     *
     * @Author 郭丁志
     * @Date 17:49 2020-07-09
     **/
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateUser", Long.class, getUserId());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

    /**
     * 获取当前登陆用户
     *
     * @Author 郭丁志
     * @Date 17:49 2020-07-09
     **/
    private Long getUserId(){
        return Fc.isNull(LoginUserContext.getUserId()) ? RoleConstant.ANONYMOUS_ID:LoginUserContext.getUserId();
    }

    /**
     * 获取当前登陆用户的所在部门
     *
     * @Author 郭丁志
     * @Date 17:49 2020-07-09
     **/
    private Long getOrg(){
        return LoginUserContext.getOrgId();
    }

}
