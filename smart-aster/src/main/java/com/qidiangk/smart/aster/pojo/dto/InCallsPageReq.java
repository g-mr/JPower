package com.qidiangk.smart.aster.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class InCallsPageReq implements Serializable {

    @Schema(description = "手机号")
    private String phone;

}
