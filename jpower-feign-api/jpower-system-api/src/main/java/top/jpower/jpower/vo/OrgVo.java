package top.jpower.jpower.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import top.jpower.jpower.dbs.entity.org.TbCoreOrg;
import lombok.Data;

/**
 * @ClassName OrgVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/22 0022 0:36
 * @Version 1.0
 */
@Data
public class OrgVo extends TbCoreOrg {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
