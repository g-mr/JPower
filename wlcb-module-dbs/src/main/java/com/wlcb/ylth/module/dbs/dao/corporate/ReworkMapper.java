package com.wlcb.ylth.module.dbs.dao.corporate;

import com.wlcb.ylth.module.dbs.dao.BaseMapper;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgRework;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("reworkMapper")
public interface ReworkMapper extends BaseMapper<TblCsrrgRework> {


    List<TblCsrrgRework> listAll(TblCsrrgRework rework);

    Integer selectStatusCountByCid(String corporateId);

    String countCorporateByRework(String quxian);
}