package com.qidiangk.smart.user.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.qidiangk.smart.user.dbs.entity.CorePost;


/**
 * 岗位信息
 *
 * @author mr.g
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostVO extends CorePost {

    @Schema(description = "岗位人数")
    private Long userNum;
}
