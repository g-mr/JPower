package com.qidiangk.smart.aster.handler.nodes.granter;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.script.ScriptUtil;
import jakarta.validation.constraints.NotNull;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiException;
import org.springframework.stereotype.Component;
import com.qidiangk.smart.aster.handler.nodes.NodeContext;
import com.qidiangk.smart.aster.handler.nodes.NodeGranter;
import com.qidiangk.smart.aster.handler.nodes.NodeResult;
import com.qidiangk.smart.aster.pojo.UserIntent;

import javax.script.ScriptException;

import static com.qidiangk.smart.aster.constants.ConstantUtil.MUSIC;

/**
 * 脚本执行
 */
@Slf4j
@Component(ScriptGranter.GRANT_TYPE)
public class ScriptGranter implements NodeGranter<UserIntent.Node.ScriptNode> {
    public static final String GRANT_TYPE = "script";

    @Override
    public @NotNull NodeResult grant(NodeContext nodeContext, Object lastResult, UserIntent.Node.ScriptNode node) {
        NodeResult.NodeResultBuilder builder = NodeResult.builder().nextId(node.getNextNode());
        try {

            if (node.getPlayMusic()){
                try {
                    nodeContext.getSupport().playMusicOnHold(MUSIC);
                } catch (AgiException e) {
                    log.error("播放音乐失败=={}", ExceptionUtil.stacktraceToString(e));
                }
            }

            return builder.result(ObjectUtil.defaultIfNull(ScriptUtil.getScript(node.getScriptType()).eval(node.getScript(), nodeContext.getBindings()), StringPool.EMPTY)).build();
        } catch (ScriptException e){
            log.error("脚本执行{}{}{}错误==>>{}", StrPool.LF, node.getScript(), StrPool.LF, ExceptionUtil.stacktraceToString(e));
            nodeContext.getSupport().streamFile("脚本执行异常", false);
            return builder.result("脚本执行异常").build();
        }
    }

}
