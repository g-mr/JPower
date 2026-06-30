package com.qidiangk.smart.aster.pojo.vo.attend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AttendPageVO implements Serializable {

    @Schema(description = "主键")
    private String id;
    @Schema(description = "拨号时的上下文；线路")
    private String context;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "在线状态")
    private Boolean onlineState;
    @Schema(description = "注册状态")
    private Boolean registerState;
    @Schema(description = "注册地址")
    private String registerAddress;
    @Schema(description = "拨打状态")
    private String dialState;
    @Schema(description = "创建时间")
    private Date createTime;
}
