package com.wlcb.jpower.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import java.util.*;
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
        setting.setStatus(0);
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
            setting.setStatus(0);
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

    @Override
    public void deleteSetting(Map<String, Object> restFulPaths, JSONArray restFulTags) {
        Set<String> paths = restFulPaths.keySet();
        Set<String> tags = restFulTags.stream().map(json -> ((JSONObject)json).getString("name")).collect(Collectors.toCollection(HashSet::new));

        List<String> ids = monitorSettingDao.listObjs(Condition.<TbLogMonitorSetting>getQueryWrapper().lambda().select(TbLogMonitorSetting::getId).notIn(TbLogMonitorSetting::getPath,paths),Fc::toStr);
        ids.addAll(monitorSettingDao.listObjs(Condition.<TbLogMonitorSetting>getQueryWrapper().lambda().select(TbLogMonitorSetting::getId).notIn(TbLogMonitorSetting::getTag,tags),Fc::toStr));

        monitorSettingDao.removeRealByIds(ids);
        settingParamDao.removeReal(Condition.<TbLogMonitorSettingParam>getQueryWrapper().lambda().in(TbLogMonitorSettingParam::getSettingId,ids));
    }

    @Override
    public TbLogMonitorSetting getSetting(String name, List<String> tags, String url, String method) {

        LambdaQueryWrapper<TbLogMonitorSetting> queryWrapper = Condition.<TbLogMonitorSetting>getQueryWrapper().lambda().eq(TbLogMonitorSetting::getStatus,ConstantsEnum.YN01.Y.getValue()).eq(TbLogMonitorSetting::getName,name);

        //查询这个接口这个请求方式得设置（优先级最高）
        TbLogMonitorSetting setting = monitorSettingDao.getOne(queryWrapper.in(TbLogMonitorSetting::getTag,tags).eq(TbLogMonitorSetting::getPath,url).eq(TbLogMonitorSetting::getMethod,method),false);
        if (Fc.notNull(setting)){
//            //获取所有参数
//            setting.setSettingParams(queryParam(setting.getId()));
            return setting;
        }

        //查询这个接口得设置（优先级第二）
        setting = monitorSettingDao.getOne(queryWrapper.in(TbLogMonitorSetting::getTag,tags).eq(TbLogMonitorSetting::getPath,url).isNull(TbLogMonitorSetting::getMethod),false);
        if (Fc.notNull(setting)){
            //去除ID方便判断是否是接口得设置，只有接口得设置才会用id去获取参数
            setting.setId(null);
            return setting;
        }

        //查询这个分组得设置（优先级第三）
        setting = monitorSettingDao.getOne(queryWrapper.in(TbLogMonitorSetting::getTag,tags).isNull(TbLogMonitorSetting::getPath).isNull(TbLogMonitorSetting::getMethod),false);
        if (Fc.notNull(setting)){
            //去除ID方便判断是否是接口得设置，只有接口得设置才会用id去获取参数
            setting.setId(null);
            return setting;
        }

        //查询这个服务得设置（优先级最低）
        setting = monitorSettingDao.getOne(queryWrapper.isNull(TbLogMonitorSetting::getTag).isNull(TbLogMonitorSetting::getPath).isNull(TbLogMonitorSetting::getMethod),false);
        if (Fc.notNull(setting)){
            //服务不可设置是否监控
            setting.setIsMonitor(null);
            //去除ID方便判断是否是接口得设置，只有接口得设置才会用id去获取参数
            setting.setId(null);
            return setting;
        }

        //默认情况
        setting =  new TbLogMonitorSetting();
        setting.setIsMonitor(ConstantsEnum.YN01.Y.getValue());
        setting.setStatus(ConstantsEnum.YN01.Y.getValue());
        return setting;
    }
}
