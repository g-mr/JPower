package top.jpower.core.dbs.config.filling;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.mybatisflex.annotation.InsertListener;
import lombok.RequiredArgsConstructor;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.util.user.UserConfig;
import top.jpower.core.util.user.model.UserDto;
import top.jpower.core.util.utils.Fc;

/**
 * 公用字段配置
 *
 * @author mr.g
 * @date 2020-07-09 17:35
 */
@RequiredArgsConstructor
public class InsertFieldsListener implements InsertListener {

    private final UserConfig userConfig;

    /**
     * 新增时配置的字段
     *
     * @author mr.g
     * @date 17:48 2020-07-09
     **/
    @Override
    public void onInsert(Object entity) {

        if (entity instanceof BaseEntity baseEntity) {
            // 创建时间为空，则以当前时间为插入时间
            if (Fc.isNull(baseEntity.getCreateTime())) {
                baseEntity.setCreateTime(LocalDateTimeUtil.now());
            }

            // 更新时间为空，则以当前时间为插入时间
            if (Fc.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(LocalDateTimeUtil.now());
            }

            UserDto userDto = userConfig.queryUser();

            if (Fc.notNull(userDto)){

                // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
                if (Fc.isNull(baseEntity.getCreateUser())){
                    baseEntity.setCreateUser(Fc.notNull(userDto.getUserId())?userDto.getUserId(): 2L);
                }

                // 当前登录用户不为空，创建人为空，则当前登录用户为更新人
                if (Fc.isNull(baseEntity.getUpdateUser())){
                    baseEntity.setUpdateUser(Fc.notNull(userDto.getUserId())?userDto.getUserId(): 2L);
                }

                // 当前登录部门不为空，创建部门为空，则当前登录部门为创建部门
                if (Fc.isNull(baseEntity.getCreateOrg()) && Fc.notNull(userDto.getOrgId())){
                    baseEntity.setCreateOrg(userDto.getOrgId());
                }

            }
        }
    }

}
