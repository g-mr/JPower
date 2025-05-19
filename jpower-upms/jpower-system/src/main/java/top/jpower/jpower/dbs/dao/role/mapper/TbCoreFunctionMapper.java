package top.jpower.jpower.dbs.dao.role.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.jpower.dbs.entity.function.TbCoreFunction;
import top.jpower.jpower.vo.DataFunctionVo;
import top.jpower.jpower.vo.FunctionVo;

import java.util.List;

/**
 * @author mr.gmac
 */
@Mapper
public interface TbCoreFunctionMapper extends JpowerBaseMapper<TbCoreFunction> {

    /**
     * 查功能列表
     * @Author goo
     * @Date 16:47 2021-02-17
     * @param coreFunction
     * @param functionType
     * @return java.util.List<top.jpower.jpower.dbs.entity.function.TbCoreFunction>
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
