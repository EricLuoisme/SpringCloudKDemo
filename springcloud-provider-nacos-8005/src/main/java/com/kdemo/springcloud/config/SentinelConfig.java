package com.kdemo.springcloud.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
public class SentinelConfig {


    @PostConstruct
    public void postConstruct() {
        initFlowRules();
        initDegradeRules();
    }

    private static void initFlowRules() {
        final FlowRule flowRule = new FlowRule();
        flowRule.setResource("/department/list");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS); // set qps as measurement
        flowRule.setCount(10);
        FlowRuleManager.loadRules(Collections.singletonList(flowRule));
    }

    private static void initDegradeRules() {
        final DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("/department/list");
        degradeRule.setGrade(RuleConstant.FLOW_GRADE_QPS); // set exception count as measurement
        degradeRule.setCount(5);
        degradeRule.setTimeWindow(1); // recover time to half-open window
        DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));
    }

}
