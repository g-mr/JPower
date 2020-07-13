package com.wlcb.jpower.module.dbs.dao.core.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import com.wlcb.jpower.module.dbs.entity.core.file.TbCoreFile;
import org.springframework.stereotype.Component;

/**
 * @ClassName TbCoreParamsDao
 * @Description TODO 文件管理
 * @Author 郭丁志
 * @Date 2020-07-03 13:30
 * @Version 1.0
 */
@Component("tbCoreFileMapper")
public interface TbCoreFileMapper extends BaseMapper<TbCoreFile> {


}
