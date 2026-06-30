package com.qidiangk.smart.aster.service.asterisk.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.Pg;
import com.qidiangk.smart.aster.dbs.dao.asterisk.EndpointsDao;
import com.qidiangk.smart.aster.pojo.vo.home.NameSelectVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineBaseVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineQueryVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineUpdateVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineVO;
import com.qidiangk.smart.aster.service.asterisk.ILineService;
import com.qidiangk.smart.aster.service.asterisk.IPjSipService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineServiceImpl implements ILineService {

    private final EndpointsDao endpointsDao;
    private final IPjSipService pjSipService;

    @Override
    public List<NameSelectVO> select() {
        return endpointsDao.listName();
    }

    @Override
    public String create(LineBaseVO lineBaseVO) {
        JpowerAssert.notTrue(endpointsDao.existEndpoint(lineBaseVO.getCallerid()), JpowerError.Business ,"号码已存在");
        return pjSipService.createLine(lineBaseVO);
    }

    @Override
    public boolean removeById(String id) {
        return pjSipService.removeLineById(id);
    }

    @Override
    public boolean updateById(LineUpdateVO lineVO) {
        return pjSipService.updateLineById(lineVO);
    }

    @Override
    public Pg<LineVO> page(LineQueryVO lineQueryVO) {
        return pjSipService.pageLine(lineQueryVO);
    }

    @Override
    public String getLimit1Line() {
        return endpointsDao.getLimit1Line();
    }
}
