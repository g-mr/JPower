package top.jpower.core.dbs.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.user.model.UserDto;
import top.jpower.core.util.utils.DateUtil;
import top.jpower.core.util.utils.Fc;

/**
 * 公用字段配置
 *
 * @author mr.g
 * @date 2020-07-09 17:35
 */
@RequiredArgsConstructor
public class UpdateRelatedFieldsMetaHandler implements MetaObjectHandler {

    private final UserConfig userConfig;

    /**
     * 新增时配置的字段
     *
     * @author mr.g
     * @date 17:48 2020-07-09
     **/
    @Override
    public void insertFill(MetaObject metaObject) {

        if (Fc.notNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();

            // 创建时间为空，则以当前时间为插入时间
            if (Fc.isNull(baseEntity.getCreateTime())) {
                baseEntity.setCreateTime(DateUtil.date());
            }

            // 更新时间为空，则以当前时间为插入时间
            if (Fc.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(DateUtil.date());
            }

            UserDto userDto = userConfig.queryUser();

            if (Fc.notNull(userDto)){

                // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
                if (Fc.isNull(baseEntity.getCreateUser()) && Fc.notNull(userDto.getUserId())){
                    baseEntity.setCreateUser(userDto.getUserId());
                }

                // 当前登录用户不为空，创建人为空，则当前登录用户为更新人
                if (Fc.isNull(baseEntity.getUpdateUser()) && Fc.notNull(userDto.getUserId())){
                    baseEntity.setUpdateUser(userDto.getUserId());
                }

                // 当前登录部门不为空，创建部门为空，则当前登录部门为创建部门
                if (Fc.isNull(baseEntity.getCreateOrg()) && Fc.notNull(userDto.getOrgId())){
                    baseEntity.setCreateOrg(userDto.getOrgId());
                }

            }
            // 新创建数据为未删除状态
            baseEntity.setIsDeleted(Boolean.FALSE);

        }
    }

    /**
     * 更新时候配置的字段
     *
     * @author mr.g
     * @date 17:49 2020-07-09
     **/
    @Override
    public void updateFill(MetaObject metaObject) {

        if (Fc.notNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();

            // 更新时间为空，则以当前时间为插入时间
            if (Fc.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(DateUtil.date());
            }

            UserDto userDto = userConfig.queryUser();
            if (Fc.notNull(userDto)) {
                // 当前登录用户不为空，创建人为空，则当前登录用户为更新人
                if (Fc.isNull(baseEntity.getUpdateUser()) && Fc.notNull(userDto.getUserId())) {
                    baseEntity.setUpdateUser(userDto.getUserId());
                }
            }
        }
    }

}
