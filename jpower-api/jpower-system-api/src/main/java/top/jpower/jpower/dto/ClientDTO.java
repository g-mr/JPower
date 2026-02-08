package top.jpower.jpower.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户端信息
 *
 * @author mr.g
 */
@Data
public class ClientDTO implements Serializable {

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "客户端名称")
    private String name;
    @Schema(description = "客户端编码")
    private String clientCode;
    @Schema(description = "客户端密钥")
    private String clientSecret;
    @Schema(description = "登录限制")
    private String loginLimit;
    @Schema(description = "token有效时长 单位秒")
    private Long accessTokenValidity;
    @Schema(description = "刷新token有效时长 单位秒")
    private Long refreshTokenValidity;
    @Schema(description = "排序")
    private Integer sortNum;
    @Schema(description = "备注")
    private String note;

}
