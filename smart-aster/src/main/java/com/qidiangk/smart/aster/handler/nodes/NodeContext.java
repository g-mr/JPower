package com.qidiangk.smart.aster.handler.nodes;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.expression.EvaluationContext;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.asterisk.utils.AgiContext;
import top.jpower.core.asterisk.utils.support.FutureConcurrentHashMap;
import top.jpower.core.asterisk.utils.support.FutureEvaluationContext;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.utils.IdcardUtil;

import javax.script.SimpleBindings;
import java.util.Map;

import static com.qidiangk.smart.aster.constants.ConstantUtil.IVR_LAST_RESULT;

@Data
public class NodeContext {

    private final EvaluationContext context = new FutureEvaluationContext();
    private final SimpleBindings bindings = new SimpleBindings(new FutureConcurrentHashMap<>());
    private final Map<String, Object> params;
    private final AgiSupport support;
    private final String callerNum;

    public NodeContext(AgiSupport agiSupport){
        this.support = agiSupport;
        this.callerNum = agiSupport.getPhone();

        params = AgiContext.cache(support.channel()).all();
        bindings.putAll(AgiContext.cache(support.channel()).all());

        // 注册一些工具类
        context.setVariable("Fc", Fc.class);
        context.setVariable("ObjectUtil", ObjectUtil.class);
        context.setVariable("StrUtil", StrUtil.class);
        context.setVariable("NumberUtil", NumberUtil.class);
        context.setVariable("PhoneUtil", PhoneUtil.class);
        context.setVariable("IdcardUtil", IdcardUtil.class);
        context.setVariable("CollUtil", CollUtil.class);
        AgiContext.cache(support.channel()).all().forEach(context::setVariable);
    }

    /**
     * 刷新内容
     */
    public void flushed(String lastKey, Object lastResult){
        if (Fc.notNull(lastResult)){
            params.put(lastKey, lastResult);
            bindings.put(lastKey, lastResult);
            context.setVariable(lastKey, lastResult);

            params.put(IVR_LAST_RESULT, lastResult);
            bindings.put(IVR_LAST_RESULT, lastResult);
            context.setVariable(IVR_LAST_RESULT, lastResult);
        }
    }

    /**
     * 新增内容
     */
    public void put(String key, Object value){
        if (Fc.isNotBlank(key) && Fc.isNotEmpty(value)){
            params.put(key, value);
            bindings.put(key, value);
            context.setVariable(key, value);
        }
    }
}
