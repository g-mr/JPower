package top.jpower.jpower.dbs.entity.role;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import top.jpower.jpower.module.dbs.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * @ClassName TbCoreRoleFunction
 * @Description TODO 角色权限信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreRoleFunction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long functionId;

    @TableField(exist = false)
    private String functionName;
}
