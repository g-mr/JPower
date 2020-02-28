package com.wlcb.wlj.module.dbs.dao.corporate;

import com.wlcb.wlj.module.dbs.dao.BaseMapper;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRework;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("reworkMapper")
public interface ReworkMapper extends BaseMapper<TblCsrrgRework> {


    List<TblCsrrgRework> listAll(String corporateId);
}