package com.wego.assignment.common.config.scheduler;

import com.wego.assignment.common.config.scheduler.jobs.RetryCarParkInfoJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartSubmitJobs {


    @Bean("retryCarParkInfo")
    public JobDetailFactoryBean jobRetryCarParkInfo() {
        return QuartzConfig.createJobDetail(RetryCarParkInfoJob.class, "retry change events");
    }


    @Bean (name = "retryCarParkInfoTrigger")
    public CronTriggerFactoryBean triggerRetryChangeEvents (@Qualifier("retryCarParkInfo") JobDetail jobDetail) {
        //return createTrigger (jobDetail, 60000, "retry change events Trigger");
        return  QuartzConfig.createCronTrigger(jobDetail, "0 */2 * ? * *", "retry change events trigger");
    }
}
