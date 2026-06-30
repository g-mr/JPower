package com.qidiangk.smart.log.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.qidiangk.smart.common.enums.YN01Enum;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.log.dbs.dao.LogMonitorParamDao;
import com.qidiangk.smart.log.dbs.dao.LogMonitorSettingDao;
import com.qidiangk.smart.log.dbs.dao.mapper.LogMonitorSettingMapper;
import com.qidiangk.smart.log.dbs.entity.LogMonitorParam;
import com.qidiangk.smart.log.dbs.entity.LogMonitorSetting;
import com.qidiangk.smart.log.service.MonitorSettingService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author mr.g
 * @Date 2021/4/18 0018 0:33
 */
@Service
@AllArgsConstructor
public class MonitorSettingServiceImpl extends BaseServiceImpl<LogMonitorSettingMapper, LogMonitorSetting> implements MonitorSettingService {

    private final LogMonitorSettingDao monitorSettingDao;
    private final LogMonitorParamDao settingParamDao;

    @Override
    public boolean save(LogMonitorSetting setting) {
        setting.setTag(Fc.isNotBlank(setting.getTag())?setting.getTag():null);
        setting.setPath(Fc.isNotBlank(setting.getPath())?setting.getPath():null);
        setting.setMethod(Fc.isNotBlank(setting.getMethod())?setting.getMethod():null);
        setting.setIsMonitor(Fc.isNull(setting.getIsMonitor())? YN01Enum.Y.getValue():setting.getIsMonitor());
        return monitorSettingDao.saveOrUpdate(setting);
    }

    @Override
    public LogMonitorSetting getOneSetting(LogMonitorSetting setting) {

        QueryWrapper queryWrapper = Wrappers.getQueryWrapper();
        queryWrapper.eq(LogMonitorSetting::getServer,setting.getServer());

        if (Fc.isNotBlank(setting.getTag())){
            queryWrapper.eq(LogMonitorSetting::getTag,setting.getTag());
        }else {
            queryWrapper.isNull(LogMonitorSetting::getTag);
        }

        if (Fc.isNotBlank(setting.getPath())){
            queryWrapper.eq(LogMonitorSetting::getPath,setting.getPath());
        }else {
            queryWrapper.isNull(LogMonitorSetting::getPath);
        }

        if (Fc.isNotBlank(setting.getMethod())){
            queryWrapper.eq(LogMonitorSetting::getMethod,setting.getMethod());
        }else {
            queryWrapper.isNull(LogMonitorSetting::getMethod);
        }
        return monitorSettingDao.getOne(queryWrapper.limit(1));
    }

    @Override
    public List<LogMonitorParam> queryParamByPath(String server, String path) {
        return settingParamDao.list(Wrappers.getQueryWrapper()
                .eq(LogMonitorParam::getServer,server)
                .eq(LogMonitorParam::getPath,path));
    }

    @Override
    public Boolean saveParams(String server, String path, String method, List<LogMonitorParam> settingParams) {
        //先删除原来的参数
        boolean is = settingParamDao.removeReal(Wrappers.getQueryWrapper()
                .eq(LogMonitorParam::getServer,server)
                .eq(LogMonitorParam::getPath,path)
                .eq(LogMonitorParam::getMethod,method));
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

        monitorSettingDao.removeReal(Wrappers.getQueryWrapper().eq(LogMonitorSetting::getServer,server).notIn(LogMonitorSetting::getPath,paths));
        monitorSettingDao.removeReal(Wrappers.getQueryWrapper().eq(LogMonitorSetting::getServer,server).notIn(LogMonitorSetting::getTag,tags));

        settingParamDao.removeReal(Wrappers.getQueryWrapper().eq(LogMonitorParam::getServer,server).notIn(LogMonitorParam::getPath,paths));
    }

    @Override
    public LogMonitorSetting getSetting(String name, List<String> tags, String url, String method) {

        QueryWrapper queryWrapper = Wrappers.getQueryWrapper().eq(LogMonitorSetting::getServer,name);

        //查询这个接口这个请求方式得设置（优先级最高）
        LogMonitorSetting setting = monitorSettingDao.getOne(queryWrapper.in(LogMonitorSetting::getTag,tags).eq(LogMonitorSetting::getPath,url).eq(LogMonitorSetting::getMethod,method).limit(1));
        if (Fc.notNull(setting)){
            return setting;
        }

        //查询这个接口得设置（优先级第二）
        setting = monitorSettingDao.getOne(queryWrapper.in(LogMonitorSetting::getTag,tags).eq(LogMonitorSetting::getPath,url).isNull(LogMonitorSetting::getMethod).limit(1));
        if (Fc.notNull(setting)){
            return setting;
        }

        //查询这个分组得设置（优先级第三）
        setting = monitorSettingDao.getOne(queryWrapper.in(LogMonitorSetting::getTag,tags).isNull(LogMonitorSetting::getPath).isNull(LogMonitorSetting::getMethod).limit(1));
        if (Fc.notNull(setting)){
            return setting;
        }

        //查询这个服务得设置（优先级最低）
        setting = monitorSettingDao.getOne(queryWrapper.isNull(LogMonitorSetting::getTag).isNull(LogMonitorSetting::getPath).isNull(LogMonitorSetting::getMethod).limit(1));
        if (Fc.notNull(setting)){
            //服务不可设置是否监控
            setting.setIsMonitor(null);
            return setting;
        }

        //默认情况
        setting =  new LogMonitorSetting();
        setting.setIsMonitor(YN01Enum.Y.getValue());
        return setting;
    }
}
