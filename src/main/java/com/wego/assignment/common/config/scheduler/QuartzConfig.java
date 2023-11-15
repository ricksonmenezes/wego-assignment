package com.wego.assignment.common.config.scheduler;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Calendar;

@Configuration
public class QuartzConfig {

    @Autowired
    ApplicationContext applicationContext;


    @Bean
    public SpringBeanJobFactory springBeanJobFactory () {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory ();
        jobFactory.setApplicationContext (applicationContext);
        return jobFactory;
    }

    public static SimpleTriggerFactoryBean createTrigger (JobDetail jobDetail, long pollFrequencyMs, String triggerName) {
        //log.debug ("createTrigger (jobDetail = {}, pollFrequencyMs = {}, triggerName = {})", jobDetail.toString (), pollFrequencyMs, triggerName);
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean ();
        factoryBean.setJobDetail (jobDetail);
        factoryBean.setStartDelay (0L);
        factoryBean.setRepeatInterval (pollFrequencyMs);
        factoryBean.setName (triggerName);
        factoryBean.setRepeatCount (SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction (SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

    public static CronTriggerFactoryBean createCronTrigger (JobDetail jobDetail, String cronExpression, String triggerName) {
        // log.debug ("createCronTrigger (jobDetail = {}, cronExpression = {}, triggerName = {})", jobDetail.toString (), cronExpression, triggerName);
        // To fix an issue with time-based cron jobs
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean ();
        factoryBean.setJobDetail (jobDetail);
        factoryBean.setCronExpression (cronExpression);
        factoryBean.setStartTime (calendar.getTime ());
        factoryBean.setStartDelay (0L);
        factoryBean.setName (triggerName);
        factoryBean.setMisfireInstruction (CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factoryBean;
    }

    public static JobDetailFactoryBean createJobDetail (Class jobClass, String jobName) {

        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean ();
        factoryBean.setName (jobName);
        factoryBean.setJobClass (jobClass);
        factoryBean.setDurability (true);
        return factoryBean;
    }
}
