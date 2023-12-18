package top.jpower.jpower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.jpower.jpower.dbs.entity.TbCorePost;
import top.jpower.jpower.module.common.service.BaseService;
import top.jpower.jpower.vo.PostVo;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 */
public interface CorePostService extends BaseService<TbCorePost> {

    /**
     * 分页查询
     *
     * @author mr.g
     * @param map
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<top.jpower.jpower.vo.PostVo>
     **/
    Page<PostVo> pageVo(Map<String, Object> map);

    /**
     * 删除岗位
     *
     * @author mr.g
     * @param ids
     * @return boolean
     **/
    boolean delete(List<Long> ids);
}
