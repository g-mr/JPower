package top.jpower.resource.service;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.resource.dbs.entity.ResourceOss;
import top.jpower.system.api.dto.SelectDTO;

import java.util.List;

/**
 * 对象存储服务接口
 * <p>
 * 提供对象存储相关的服务方法
 * </p>
 *
 * @author mr.g
 */
public interface ResourceOssService extends BaseService<ResourceOss> {

    /**
     * 获取编码名称列表
     *
     * @author mr.g
     * @return List<SelectVO> 编码名称列表
     */
    List<SelectDTO> listCodeName();

}
