package top.jpower.system.dbs.dao.role.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.system.dbs.entity.function.CoreFunction;
import top.jpower.system.vo.DataFunctionVo;
import top.jpower.system.vo.FunctionVo;

import java.util.List;

/**
 * 功能数据访问映射器
 * 
 * @author mr.g
 */
@Mapper
public interface CoreFunctionMapper extends JpowerBaseMapper<CoreFunction> {

	/**
	 * 查功能列表
	 *
	 * @author mr.g
	 * @param coreFunction 查询条件
	 * @param functionType 功能类型
	 * @return java.util.List<top.jpower.system.vo.FunctionVo> 功能列表
	 **/
	List<FunctionVo> listFunction(@Param(Constants.WRAPPER) Wrapper<CoreFunction> coreFunction, @Param("functionType") String functionType);

	/**
	 * 查询数据权限的菜单
	 * @author mr.g
	 * @param wrapper
	 * @return
	 **/
	List<DataFunctionVo> listDataFunction(@Param(Constants.WRAPPER) LambdaQueryWrapper<CoreFunction> wrapper);

}
