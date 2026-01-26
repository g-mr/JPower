package top.jpower.core.util.rsp;

import cn.hutool.db.Page;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页信息-返回前端
 *
 * @Author mr.g
 * @Date 2022/6/15 1:30
 * @Description
 */
@Data
@AllArgsConstructor
public class Pg<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6885616031035948144L;

    /**
     * 总条数
     **/
    private long total;
    /**
     * 数据
     **/
    private List<T> list;

    public static <T> Pg<T> of(long total, List<T> list) {
        return new Pg<>(total, list);
    }

}
