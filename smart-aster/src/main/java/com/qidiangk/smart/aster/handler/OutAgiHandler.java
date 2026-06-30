package com.qidiangk.smart.aster.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import top.jpower.core.asterisk.agi.annotation.Agi;
import top.jpower.core.asterisk.agi.fastagi.AgiAbstractScript;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.asterisk.properties.AsteriskProperties;
import top.jpower.core.asterisk.utils.AgiContext;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.constants.VariableNameEnum;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.service.CallService;
import com.qidiangk.smart.aster.service.asterisk.IPjSipService;

import java.io.File;

import static com.qidiangk.smart.aster.constants.ConstantUtil.ONLINE_AGENT;
import static com.qidiangk.smart.aster.constants.VariableNameEnum.LINE_ID;

/**
 * 分级外呼处理
 *
 * @author mr.g
 */
@Slf4j
@Agi("callout.agi")
@Component
@RequiredArgsConstructor
public class OutAgiHandler extends AgiAbstractScript {

    private final AsteriskProperties asteriskProperties;
    private final CallService callService;
    private final IPjSipService pjSipService;
    private final RedissonClient redissonClient;

    @Override
    public void hangup(AgiSupport agiSupport) {
    }

    @Override
    @SneakyThrows(AgiException.class)
    public void service(AgiSupport agiSupport) {

        AgiContext.cache(agiSupport.channel()).put("timestart", DateUtil.date());

        String file =  StrUtil.concat(true, StrUtil.appendIfMissing(asteriskProperties.getVoiceRootDir(), File.separator), agiSupport.getPhone(), File.separator, "recording_", DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss"),".wav");
        // 创建录音目录
        FileUtil.mkParentDirs(file);
        // 录音文件地址
        agiSupport.setVariable(VariableNameEnum.FILE_PATH.getName(), file);
        AgiContext.cache(agiSupport.channel()).put("recordingPath", file);

        // 开始录音
        agiSupport.mixMonitor(file);


        // 获取到线路ID
        String lineId = agiSupport.getVariable(LINE_ID.getName());
        EndpointsDO endpointsDO = Fc.isBlank(lineId) ? getLine(agiSupport) : pjSipService.getById(lineId);
        if (endpointsDO != null) {

            String phone = StrUtil.concat(true, endpointsDO.getPrefix(), agiSupport.getPhone());
            if (callService.isPhoneZero(agiSupport.getPhone(), endpointsDO)) {
                phone = "0"+phone;
            }

            // 外呼号码设置
            log.info("外呼号码=={}, 处理后的号码: {}, 外呼显示号码=={}", agiSupport.getPhone(), phone, endpointsDO.getCallerid());

            // 实现外呼
            agiSupport.channel().dial("PJSIP/"+phone+"@"+endpointsDO.getId(),30,"g");
        } else {
            log.error("未找到线路ID: {}", lineId);
        }

    }

    /**
     * 获取线路人工外呼时候的线路
     *
     * @param agiSupport
     * @return
     */
    private EndpointsDO getLine(AgiSupport agiSupport) {
        String attend = StrUtil.subBetween(agiSupport.channel().getName(), "/", "-");
        String lineId = Fc.toStr(redissonClient.getMap(ONLINE_AGENT).get(attend));
        if (Fc.isBlank(lineId)) {
            log.info("未获取到当前坐席的选中线路=={}, {}", attend, lineId);
            lineId = pjSipService.getLimit1Line();
            if (Fc.isBlank(lineId)) {
                log.error("未创建线路，请先创建线路...{}", attend);
                return null;
            }
        }
        return pjSipService.getById(lineId);
    }
}
