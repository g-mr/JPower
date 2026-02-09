package top.jpower.user.service;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import top.jpower.user.dbs.entity.CorePost;
import top.jpower.user.vo.PostSelectVO;
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
     * @param ids 岗位ID
     * @return boolean 删除成功返回true
     **/
    boolean deleteInIds(List<Long> ids);

    /**
     * 查询岗位下拉框数据
     *
     * @author mr.g
     * @param name 搜索名称
     * @return 岗位列表
     **/
    List<PostSelectVO> listSelect(String name);

    /**
     * 创建岗位
     *
     * @author mr.g
     * @param corePost 岗位信息
     * @return 岗位ID
     **/
    Long createPost(CorePost corePost);

    /**
     * 编辑岗位
     *
     * @author mr.g
     * @param corePost 岗位信息
     * @return 岗位ID
     **/
    Long editById(CorePost corePost);
}
