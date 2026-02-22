package top.jpower.core.dbs.tenant.entity;

import com.mybatisflex.annotation.Column;
import lombok.Data;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * 租户信息
 *
 * @author mr.g
 */
@Data
public class TenantEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7549710952290937270L;

    /**
     * 租户编码
     **/
    @Column(tenantId = true)
    private String tenantCode;
}
