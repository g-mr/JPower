package com.wlcb.jpower.sentinel.datascource;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName NacosDataSource
 * @Description TODO sentinel规则持久化配置
 * @Author 郭丁志
 * @Date 2020/9/13 0013 0:53
 * @Version 1.0
 */
@Component
public class SentinelNacosDataSource {//implements InitFunc {

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String remoteAddress;
    @Value("${spring.cloud.nacos.config.namespace:}")
    private String nacosNamespace;
    private String groupId = "SENTINEL_GROUP";
    @Value ("#{'${spring.application.name}'.concat('-sentinel')}")
    private String dataId;

    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, remoteAddress);
        properties.put(PropertyKeyConst.NAMESPACE, nacosNamespace);

        //流控
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(properties, groupId, dataId.concat("-flow"),
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

//        WritableDataSource<List<FlowRule>> wds = new FileWritableDataSource<>("", JSON::toJSONString);
//        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
//        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
//        WritableDataSourceRegistry.registerFlowDataSource(wds);


        //降级
        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new NacosDataSource<>(properties, groupId, dataId.concat("-degrade"),
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                }));
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

        //热点参数
        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = new NacosDataSource<>(properties, groupId, dataId.concat("-paramFlow"),
                source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
                }));
        ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());

        //系统规则
        ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new NacosDataSource<>(properties, groupId, dataId.concat("-system"),
                source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {
                }));
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

        //授权规则
        ReadableDataSource<String, List<AuthorityRule>> authorityRuleDataSource = new NacosDataSource<>(properties, groupId, dataId.concat("-authority"),
                source -> JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {
                }));
        AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());
    }

}
