package com.qidiangk.smart.aster.service.asterisk;

import top.jpower.core.util.rsp.Pg;
import com.qidiangk.smart.aster.pojo.vo.home.NameSelectVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineBaseVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineQueryVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineUpdateVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineVO;

import java.util.List;

public interface ILineService {
    List<NameSelectVO> select();

    String create(LineBaseVO lineBaseVO);

    boolean removeById(String id);

    boolean updateById(LineUpdateVO lineVO);

    Pg<LineVO> page(LineQueryVO lineQueryVO);

    String getLimit1Line();

}
