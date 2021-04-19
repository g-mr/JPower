package com.wlcb.jpower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wlcb.jpower.dbs.dao.LogMonitorSettingDao;
import com.wlcb.jpower.dbs.dao.LogMonitorSettingParamDao;
import com.wlcb.jpower.dbs.dao.mapper.LogMonitorSettingMapper;
import com.wlcb.jpower.dbs.entity.TbLogMonitorSetting;
import com.wlcb.jpower.dbs.entity.TbLogMonitorSettingParam;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.MonitorSettingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author mr.g
 * @Date 2021/4/18 0018 0:33
 */
@Service
@AllArgsConstructor
public class MonitorSettingServiceImpl extends BaseServiceImpl<LogMonitorSettingMapper, TbLogMonitorSetting> implements MonitorSettingService {

    private final LogMonitorSettingDao monitorSettingDao;
    private final LogMonitorSettingParamDao settingParamDao;

    @Override
    public boolean save(TbLogMonitorSetting setting) {
        setting.setId(Fc.randomUUID());
        setting.setTag(Fc.isNotBlank(setting.getTag())?setting.getTag():null);
        setting.setPath(Fc.isNotBlank(setting.getPath())?setting.getPath():null);
        setting.setMethod(Fc.isNotBlank(setting.getMethod())?setting.getMethod():null);
        setting.setIsMonitor(Fc.isNull(setting.getIsMonitor())?ConstantsEnum.YN01.Y.getValue():setting.getIsMonitor());
        setting.setCode(Fc.isBlank(setting.getCode())?"200":setting.getCode());
        return monitorSettingDao.save(setting);
    }

    @Override
    public TbLogMonitorSetting setting(String server, String tag, String path, String method) {

        LambdaQueryWrapper<TbLogMonitorSetting> queryWrapper = Condition.getQueryWrapper(TbLogMonitorSetting.class).lambda();
        queryWrapper.eq(TbLogMonitorSetting::getName,server);

        if (Fc.isNotBlank(tag)){
            queryWrapper.eq(TbLogMonitorSetting::getTag,tag);
        }else {
            queryWrapper.isNull(TbLogMonitorSetting::getTag);
        }

        if (Fc.isNotBlank(path)){
            queryWrapper.eq(TbLogMonitorSetting::getPath,path);
        }else {
            queryWrapper.isNull(TbLogMonitorSetting::getPath);
        }

        if (Fc.isNotBlank(method)){
            queryWrapper.eq(TbLogMonitorSetting::getMethod,method);
        }else {
            queryWrapper.isNull(TbLogMonitorSetting::getMethod);
        }
        TbLogMonitorSetting setting = monitorSettingDao.getOne(queryWrapper,false);
        if (Fc.isNull(setting)){
            setting = new TbLogMonitorSetting();
            setting.setName(server);
            setting.setTag(tag);
            setting.setPath(path);
            setting.setMethod(method);
            save(setting);
        }
        return setting;
    }

    @Override
    public List<Map<String, Object>> queryParam(String settingId) {
        List<TbLogMonitorSettingParam> paramList = settingParamDao.list(Condition.<TbLogMonitorSettingParam>getQueryWrapper().lambda().eq(TbLogMonitorSettingParam::getSettingId,settingId));
        return Fc.isNull(paramList) ?
                null :
                paramList.stream().map(param -> ChainMap.init().set("name",param.getName()).set("value",param.getValue()).set("type",param.getType())).collect(Collectors.toList());
    }

    @Override
    public Boolean saveParams(String settingId, List<TbLogMonitorSettingParam> settingParams) {
        //先删除原来的参数
        settingParamDao.removeReal(Condition.<TbLogMonitorSettingParam>getQueryWrapper().lambda().eq(TbLogMonitorSettingParam::getSettingId,settingId));
        settingParams.stream().peek(param -> param.setSettingId(settingId)).collect(Collectors.toList());
        return settingParamDao.saveBatch(settingParams);
    }
}
