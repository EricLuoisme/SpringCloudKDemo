package com.kdemo.springcloud.scheduler;

import com.kdemo.springcloud.config.DynamicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 动态配置cron
 *
 * @author Roylic
 * 2024/3/2
 */
@Slf4j
@Component
public class DynamicScheduleHandler implements SchedulingConfigurer {

    @Autowired
    private DynamicConfig dynamicConfig;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                // runnable task
                () -> log.info("Current time： {}", LocalDateTime.now()),
                // trigger cron
                triggerContext -> {
                    CronTrigger cronTrigger = new CronTrigger(dynamicConfig.getCron());
                    return cronTrigger.nextExecutionTime(triggerContext);
                });
    }


}
