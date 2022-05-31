package com.wlcb.jpower.dbs.dao.role.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import com.wlcb.jpower.vo.FunctionVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tbCoreFunctionMapper")
public interface TbCoreFunctionMapper extends JpowerBaseMapper<TbCoreFunction> {

    /**
     * 查功能列表
     * @Author goo
     * @Date 16:47 2021-02-17
     * @param coreFunction
     * @param isMenu
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreFunction>
     **/
    List<FunctionVo> listFunction(@Param(Constants.WRAPPER) Wrapper<TbCoreFunction> coreFunction,@Param("isMenu") String isMenu);

}
