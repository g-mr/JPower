package com.wlcb.jpower.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wlcb.jpower.dbs.dao.LogMonitorParamDao;
import com.wlcb.jpower.dbs.dao.LogMonitorSettingDao;
import com.wlcb.jpower.dbs.dao.mapper.LogMonitorSettingMapper;
import com.wlcb.jpower.dbs.entity.TbLogMonitorParam;
import com.wlcb.jpower.dbs.entity.TbLogMonitorSetting;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
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
    private final LogMonitorParamDao settingParamDao;

    @Override
    public boolean save(TbLogMonitorSetting setting) {
        setting.setTag(Fc.isNotBlank(setting.getTag())?setting.getTag():null);
        setting.setPath(Fc.isNotBlank(setting.getPath())?setting.getPath():null);
        setting.setMethod(Fc.isNotBlank(setting.getMethod())?setting.getMethod():null);
        setting.setIsMonitor(Fc.isNull(setting.getIsMonitor())?ConstantsEnum.YN01.Y.getValue():setting.getIsMonitor());
        return monitorSettingDao.saveOrUpdate(setting);
    }

    @Override
    public TbLogMonitorSetting getOneSetting(TbLogMonitorSetting setting) {

        LambdaQueryWrapper<TbLogMonitorSetting> queryWrapper = Condition.getQueryWrapper(TbLogMonitorSetting.class).lambda();
        queryWrapper.eq(TbLogMonitorSetting::getServer,setting.getServer());

        if (Fc.isNotBlank(setting.getTag())){
            queryWrapper.eq(TbLogMonitorSetting::getTag,setting.getTag());
        }else {
            queryWrapper.isNull(TbLogMonitorSetting::getTag);
        }

        if (Fc.isNotBlank(setting.getPath())){
            queryWrapper.eq(TbLogMonitorSetting::getPath,setting.getPath());
        }else {
            queryWrapper.isNull(TbLogMonitorSetting::getPath);
        }

        if (Fc.isNotBlank(setting.getMethod())){
            queryWrapper.eq(TbLogMonitorSetting::getMethod,setting.getMethod());
        }else {
            queryWrapper.isNull(TbLogMonitorSetting::getMethod);
        }
        return monitorSettingDao.getOne(queryWrapper,false);
    }

    @Override
    public List<TbLogMonitorParam> queryParamByPath(String server, String path) {
        return settingParamDao.list(Condition.<TbLogMonitorParam>getQueryWrapper().lambda()
                .eq(TbLogMonitorParam::getServer,server)
                .eq(TbLogMonitorParam::getPath,path));
    }

    @Override
    public Boolean saveParams(String server, String path, String method, List<TbLogMonitorParam> settingParams) {
        //先删除原来的参数
        boolean is = settingParamDao.removeReal(Condition.<TbLogMonitorParam>getQueryWrapper().lambda()
                .eq(TbLogMonitorParam::getServer,server)
                .eq(TbLogMonitorParam::getPath,path)
                .eq(TbLogMonitorParam::getMethod,method));
        if (settingParams.size() > 0){
            settingParams = settingParams.stream().peek(param -> {
                param.setServer(server);
                param.setPath(path);
                param.setMethod(method);
            }).collect(Collectors.toList());
            return settingParamDao.saveBatch(settingParams);
        }else {
            return is;
        }
    }

    @Override
    public void deleteSetting(String server, Map<String, Object> restFulPaths, JSONArray restFulTags) {
        Set<String> paths = restFulPaths.keySet();
        Set<String> tags = restFulTags.stream().map(json -> ((JSONObject)json).getString("name")).collect(Collectors.toCollection(HashSet::new));

        monitorSettingDao.removeReal(Condition.<TbLogMonitorSetting>getQueryWrapper().lambda().eq(TbLogMonitorSetting::getServer,server).notIn(TbLogMonitorSetting::getPath,paths));
        monitorSettingDao.removeReal(Condition.<TbLogMonitorSetting>getQueryWrapper().lambda().eq(TbLogMonitorSetting::getServer,server).notIn(TbLogMonitorSetting::getTag,tags));

        settingParamDao.removeReal(Condition.<TbLogMonitorParam>getQueryWrapper().lambda().eq(TbLogMonitorParam::getServer,server).notIn(TbLogMonitorParam::getPath,paths));
    }

    @Override
    public TbLogMonitorSetting getSetting(String name, List<String> tags, String url, String method) {

        LambdaQueryWrapper<TbLogMonitorSetting> queryWrapper = Condition.<TbLogMonitorSetting>getQueryWrapper().lambda().eq(TbLogMonitorSetting::getServer,name);

        //查询这个接口这个请求方式得设置（优先级最高）
        TbLogMonitorSetting setting = monitorSettingDao.getOne(queryWrapper.in(TbLogMonitorSetting::getTag,tags).eq(TbLogMonitorSetting::getPath,url).eq(TbLogMonitorSetting::getMethod,method),false);
        if (Fc.notNull(setting)){
            return setting;
        }

        //查询这个接口得设置（优先级第二）
        setting = monitorSettingDao.getOne(queryWrapper.in(TbLogMonitorSetting::getTag,tags).eq(TbLogMonitorSetting::getPath,url).isNull(TbLogMonitorSetting::getMethod),false);
        if (Fc.notNull(setting)){
            return setting;
        }

        //查询这个分组得设置（优先级第三）
        setting = monitorSettingDao.getOne(queryWrapper.in(TbLogMonitorSetting::getTag,tags).isNull(TbLogMonitorSetting::getPath).isNull(TbLogMonitorSetting::getMethod),false);
        if (Fc.notNull(setting)){
            return setting;
        }

        //查询这个服务得设置（优先级最低）
        setting = monitorSettingDao.getOne(queryWrapper.isNull(TbLogMonitorSetting::getTag).isNull(TbLogMonitorSetting::getPath).isNull(TbLogMonitorSetting::getMethod),false);
        if (Fc.notNull(setting)){
            //服务不可设置是否监控
            setting.setIsMonitor(null);
            return setting;
        }

        //默认情况
        setting =  new TbLogMonitorSetting();
        setting.setIsMonitor(ConstantsEnum.YN01.Y.getValue());
        return setting;
    }
}
