package top.jpower.user.service;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.user.dbs.entity.CorePost;
import top.jpower.user.vo.PostVO;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 */
public interface CorePostService extends BaseService<CorePost> {

    /**
     * 分页查询
     *
     * @author mr.g
     * @param map 查询条件
     * @return 分页数据
     **/
    Pg<PostVO> pageVo(Map<String, Object> map);

    /**
     * 删除岗位
     *
     * @author mr.g
     * @param ids
     * @return boolean
     **/
    boolean delete(List<Long> ids);
}
