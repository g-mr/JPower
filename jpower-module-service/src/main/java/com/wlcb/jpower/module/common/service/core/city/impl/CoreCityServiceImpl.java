package com.wlcb.jpower.module.common.service.core.city.impl;

import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.city.CoreCityService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.RandomUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.dao.core.city.TbCoreCityDao;
import com.wlcb.jpower.module.dbs.dao.core.city.mapper.TbCoreCityMapper;
import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("coreCityService")
@AllArgsConstructor
public class CoreCityServiceImpl extends BaseServiceImpl<TbCoreCityMapper,TbCoreCity> implements CoreCityService {

    private TbCoreCityDao coreCityDao;

    @Override
    public List<Map<String, Object>> listChild(Map<String, Object> city) {
        return coreCityDao.listMaps(Condition.getQueryWrapper(city,TbCoreCity.class).lambda()
                .select(TbCoreCity::getCode,TbCoreCity::getName)
                .orderByAsc(TbCoreCity::getSortNum));
    }

    @Override
    public List<TbCoreCity> list(TbCoreCity coreCity) {
        return coreCityDao.list(Condition.getQueryWrapper(coreCity).lambda().orderByAsc(TbCoreCity::getSortNum));
    }

    @Override
    public boolean save(TbCoreCity coreCity) {
        coreCity.setCode(RandomUtil.createCityCode(coreCity.getPcode(),coreCity.getCode()));

        TbCoreCity city = queryByCode(coreCity.getCode());

        if(Fc.isBlank(coreCity.getId())){
            coreCity.setFullname(Fc.isNotBlank(coreCity.getFullname())?coreCity.getFullname():coreCity.getName());
            coreCity.setCountryCode(Fc.isNotBlank(coreCity.getCountryCode())?coreCity.getCountryCode():JpowerConstants.COUNTRY_CODE);
            coreCity.setPcode(Fc.isNotBlank(coreCity.getPcode())?coreCity.getPcode():JpowerConstants.TOP_CODE);

            JpowerAssert.notTrue(city != null, JpowerError.BUSINESS,"该编号已存在");
        }else{
            JpowerAssert.notTrue(city != null && !StringUtil.equals(coreCity.getId(),city.getId()), JpowerError.BUSINESS,"该编号已存在");
        }

        Boolean is = coreCityDao.saveOrUpdate(coreCity);
        return is;
    }

    @Override
    public TbCoreCity queryByCode(String cityCode) {
        return coreCityDao.getOne(Condition.<TbCoreCity>getQueryWrapper().lambda().eq(TbCoreCity::getCode,cityCode));
    }

    @Override
    public Boolean deleteBatch(List<String> ids) {

        List<Object> listCode = coreCityDao.listObjs(Condition.<TbCoreCity>getQueryWrapper().lambda().select(TbCoreCity::getCode).in(TbCoreCity::getId,ids));
        if(listCode.size()>0){
            Integer count = coreCityDao.count(Condition.<TbCoreCity>getQueryWrapper().lambda().in(TbCoreCity::getPcode,listCode));
            JpowerAssert.notTrue(count>0,JpowerError.BUSINESS,"请先删除子区域");
        }

        return coreCityDao.removeRealByIds(ids);
    }

    @Override
    public List<Node> lazyTree(String pcode) {
        return coreCityDao.tree(Condition.getTreeWrapper(TbCoreCity::getCode,TbCoreCity::getPcode,TbCoreCity::getName).lazy(pcode).lambda().orderByAsc(TbCoreCity::getSortNum));
    }

}
