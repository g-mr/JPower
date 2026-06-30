package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import top.jpower.core.asterisk.properties.AsteriskProperties;
import top.jpower.core.asterisk.utils.AgiContext;
import top.jpower.core.util.support.JpowerSpelExpressionParser;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.constants.CallRouteProcessEnum;
import com.qidiangk.smart.aster.constants.VariableNameEnum;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * 转接
 */
@Slf4j
@Component(TransferGranter.GRANT_TYPE)
@RequiredArgsConstructor
public class TransferGranter implements NodeGranter<UserIntent.Node.TransferNode> {

    public static final String GRANT_TYPE = "transfer";

    private final AsteriskProperties asteriskProperties;
    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    /**
     * QueueWithdrawCaller: 将呼叫者从队列中撤回至拨号计划。 <a href="https://docs.asterisk.org/Asterisk_22_Documentation/API_Documentation/AMI_Actions/QueueWithdrawCaller/">文档</a>
     */
    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.TransferNode node) {
        // 1=呼入 2=呼出
        Integer type = Fc.toInt(nodeContext.getSupport().getCallType(), CallRouteProcessEnum.INTEND.getValue());

        if (Fc.isNotBlank(node.getStartAnswer())){
            nodeContext.getSupport().streamFile(node.getStartAnswer(), false);
        }

        // 生成转人工以后的录音文件
        String dir = StrUtil.appendIfMissing(asteriskProperties.getVoiceRootDir(), File.separator) + "transfer" + File.separator + nodeContext.getSupport().getUniqueId() + File.separator;
        FileUtil.mkdir(dir);
        List<File> listFile = FileUtil.loopFiles(FileUtil.file(dir), 1, dirFile -> {
            return dirFile.getName().endsWith(".wav") && dirFile.getName().startsWith("transfer-");
        });
        Integer index = listFile.stream().map(File::getName).map(name -> {
            return name.replace("transfer-", "").replace(".wav", "");
        }).filter(Fc::notNull).filter(StrUtil::isNumeric).map(Fc::toInt).filter(Objects::nonNull).max(Integer::compareTo).orElse(0);
        String transferFile = dir + "transfer-"+(index+1)+".wav";
        String monitorId = nodeContext.getSupport().mixMonitor(transferFile);

        // 执行转接
        nodeContext.getSupport().exec("queue", node.getQueueName(), "cn", "", "", Fc.toStr(node.getRingTime()));
//            nodeContext.getSupport().getVariable("QUEUESTATUS")// 转接状态 如果接听了这个值是CONTINUE
        // 如果ABANDONED=TRUE,代表未接听电话
        boolean isSuccess = Fc.notEqualsValue(nodeContext.getSupport().getVariable("ABANDONED"), "TRUE");
        log.info("当前是否转接成功=={}", isSuccess);

        // 停止录音
        nodeContext.getSupport().stopMixMonitor(monitorId);

        if (isSuccess) {
            // 记录电话转接了人工
            AgiContext.cache(nodeContext.getSupport().channel()).put(GRANT_TYPE, Boolean.TRUE);
            // 将录音文件保存到通道中，监听会保存到数据库
            nodeContext.getSupport().setVariable(VariableNameEnum.TRANSFER_FILE.getName(), transferFile);

            return NodeResult.builder()
                    .result(Fc.isBlank(node.getThrough().getResult()) ? null : resultIfNull(parser.parseExpression(node.getThrough().getResult()).getValue(nodeContext.getContext())))
                    .nextId(node.getThrough().getNextNode())
                    .build();
        } else {
            // 转接失败，比如没人接，就删除录音文件
            FileUtil.del(transferFile);
            return NodeResult.builder()
                    .result(Fc.isBlank(node.getUnThrough().getResult()) ? null : resultIfNull(parser.parseExpression(node.getUnThrough().getResult()).getValue(nodeContext.getContext())))
                    .nextId(node.getUnThrough().getNextNode())
                    .build();
        }
    }

    private Object resultIfNull(Object result){
        if (result instanceof String){
            if (StrUtil.isBlank((String) result) || Fc.equalsValue(result, "null")){
                return null;
            }
        }
        return result;
    }

}
