package com.wlcb.jpower.dbs.dao.role.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import com.wlcb.jpower.vo.DataFunctionVo;
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
     * @param functionType
     * @return java.util.List<com.wlcb.jpower.dbs.entity.function.TbCoreFunction>
     **/
    List<FunctionVo> listFunction(@Param(Constants.WRAPPER) Wrapper<TbCoreFunction> coreFunction,@Param("functionType") String functionType);

    /**
     * 查询数据权限的菜单
     * @author mr.g
     * @param wrapper
     * @return
     **/
    List<DataFunctionVo> listDataFunction(@Param(Constants.WRAPPER) LambdaQueryWrapper<TbCoreFunction> wrapper);
}
