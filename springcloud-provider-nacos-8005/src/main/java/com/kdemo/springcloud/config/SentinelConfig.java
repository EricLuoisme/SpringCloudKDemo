package com.kdemo.springcloud.config;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
public class SentinelConfig {

    @PostConstruct
    public void postConstruct() {
        initFlowRule();
    }

    private void initFlowRule() {
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("/department/list");
        flowRule.setGrade(5); // QPS limit
        FlowRuleManager.loadRules(Collections.singletonList(flowRule));
    }

}
