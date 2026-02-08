package top.jpower.jpower.dbs.entity.tenant;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName TbCoreTenant
 * @Description TODO 租户
 * @Author 郭丁志
 * @Date 2020-10-23 10:07
 * @Version 1.0
 */
@Data
@Table("tb_core_tenant")
@EqualsAndHashCode(callSuper = true)
public class TbCoreTenant extends BaseEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;
    @Schema(description = "租户Code",hidden = true)
    private String tenantCode;
    @Schema(description = "租户名称")
    private String tenantName;
    @Schema(description = "域名地址")
    private String domain;
    @Schema(description = "联系人")
    private String contactName;
    @Schema(description = "联系电话")
    private String contactPhone;
    @Schema(description = "联系地址")
    private String address;
    @Schema(description = "账号额度")
    private Integer accountNumber;
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    @Schema(description = "过期时间")
    private Date expireTime;
    @Schema(description = "授权码",hidden = true)
    private String licenseKey;
    @Schema(description = "设置内容")
    @Column(typeHandler = JacksonTypeHandler.class, isLarge = true)
    private Map<String, String> config;

}
