package top.jpower.core.dbs.config.filling;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.mybatisflex.annotation.UpdateListener;
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
public class UpdateFieldsListener implements UpdateListener {

    private final UserConfig userConfig;

    /**
     * 更新时候配置的字段
     *
     * @author mr.g
     * @date 17:49 2020-07-09
     **/
    @Override
    public void onUpdate(Object entity) {

        if (entity instanceof BaseEntity baseEntity) {

            // 更新时间为空，则以当前时间为插入时间
            if (Fc.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(LocalDateTimeUtil.now());
            }

            UserDto userDto = userConfig.queryUser();
            if (Fc.notNull(userDto)) {
                // 当前登录用户不为空，创建人为空，则当前登录用户为更新人
                if (Fc.isNull(baseEntity.getUpdateUser())) {
                    // 如何没有登录用户，则默认为为匿名用户
                    baseEntity.setUpdateUser(Fc.notNull(userDto.getUserId())?userDto.getUserId(): 2L);
                }
            }
        }
    }

}
