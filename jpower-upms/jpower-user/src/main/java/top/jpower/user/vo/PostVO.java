package top.jpower.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.user.dbs.entity.CorePost;

import java.io.Serial;


/**
 * 岗位信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostVO extends CorePost {

    @Serial
    private static final long serialVersionUID = -4320881275642047517L;

    @Schema(description = "岗位人数")
    private Long userNum;
}
