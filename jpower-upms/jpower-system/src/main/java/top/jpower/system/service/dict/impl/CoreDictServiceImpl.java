package top.jpower.system.service.dict.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jpower.common.enums.YNEnum;
import top.jpower.common.enums.YYZLEnum;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.WebUtil;
import top.jpower.system.dbs.dao.dict.CoreDictDao;
import top.jpower.system.dbs.dao.dict.mapper.CoreDictMapper;
import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.system.dbs.entity.dict.TbCoreDict;
import top.jpower.system.service.dict.CoreDictService;
import top.jpower.system.vo.DictVo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static top.jpower.common.enums.YYZLEnum.CHINA;
import static top.jpower.core.dbs.tenant.TenantConstant.DEFAULT_TENANT_CODE;
import static top.jpower.core.util.constants.JpowerConstants.I18N_KEY;
import static top.jpower.core.util.constants.JpowerConstants.TOP_CODE;

/**
 * 字典服务实现
 * 
 * @author mr.g
 */
@Service("coreDictService")
public class CoreDictServiceImpl extends BaseServiceImpl<CoreDictMapper, CoreDict> implements CoreDictService {

    @Autowired
    private CoreDictDao dictDao;

    @Override
    public CoreDict queryDictTypeByCode(String dictTypeCode, String code) {
        LambdaQueryWrapper<TbCoreDict> wrapper = Condition.<TbCoreDict>getQueryWrapper().lambda()
                .eq(TbCoreDict::getDictTypeCode,dictTypeCode)
                .eq(TbCoreDict::getCode,code);
        if(ShieldUtil.isRoot()){
            wrapper.eq(TbCoreDict::getTenantCode,DEFAULT_TENANT_CODE);
        }
        return dictDao.getOne(wrapper);
    }

    @Override
    public Boolean saveDict(CoreDict dict) {
        TbCoreDict coreDictType = queryDictTypeByCode(dict.getDictTypeCode(),dict.getCode());
        if(Fc.isNull(dict.getId())){
            dict.setLocale(Fc.isBlank(dict.getLocale())? CHINA.getValue() :dict.getLocale());
            dict.setIsStop(Fc.isBlank(dict.getIsStop())? YNEnum.N.getValue() : dict.getIsStop());
            dict.setParentId(Fc.notNull(dict.getParentId())?dict.getParentId():Fc.toLong(TOP_CODE));
            JpowerAssert.notTrue(coreDictType != null, JpowerError.Business,"该字典已存在");
        }else {
            JpowerAssert.notTrue(coreDictType != null && !NumberUtil.equals(dict.getId(),coreDictType.getId()), JpowerError.Business,"该字典已存在");
        }

        return dictDao.saveOrUpdate(dict);
    }

    @Override
    public List<DictVo> listByType(CoreDict dict) {
        if (ShieldUtil.isRoot()) {
            dict.setTenantCode(Fc.isBlank(dict.getTenantCode()) ? DEFAULT_TENANT_CODE : dict.getTenantCode());
        }
        return dictDao.getBaseMapper().listByType(dict);
    }

    @Override
    public List<Map<String, Object>> listByTypeCode(String dictTypeCode) {
        String requestLocale = YYZLEnum.CHINA.getValue();
        if (Fc.notNull(WebUtil.getRequest())){
            requestLocale = Fc.toStr(Objects.requireNonNull(WebUtil.getRequest()).getHeader(I18N_KEY), YYZLEnum.CHINA.getValue());
        }
        //这里不能返回实体类，不然会造成字典回写的死循环
        return dictDao.listMaps(Condition.<TbCoreDict>getQueryWrapper().lambda()
                .select(TbCoreDict::getCode, TbCoreDict::getName, TbCoreDict::getLocale)
                .eq(TbCoreDict::getDictTypeCode, dictTypeCode)
                .eq(TbCoreDict::getLocale, requestLocale)
                .eq(ShieldUtil.isRoot(), TbCoreDict::getTenantCode, DEFAULT_TENANT_CODE));
    }

}
