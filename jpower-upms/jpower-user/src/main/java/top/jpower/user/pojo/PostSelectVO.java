package top.jpower.user.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 岗位下拉信息
 *
 * @author mr.g
 */
@Data
public class PostSelectVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "编号")
    private String code;
}
