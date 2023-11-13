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
        return QuartzConfig.createJobDetail(RetryCarParkInfoJob.class, "retry car parks static data job");
    }


    @Bean (name = "retryCarParkInfoTrigger")
    public CronTriggerFactoryBean triggerRetryCarParkInfo (@Qualifier("retryCarParkInfo") JobDetail jobDetail) {

        return  QuartzConfig.createCronTrigger(jobDetail, "0 */30 * ? * *", "retry car parks static data trigger");
    }


    @Bean("retryCarParkLiveData")
    public JobDetailFactoryBean jobRetryCarParkLiveData() {

        return QuartzConfig.createJobDetail(RetryCarParkInfoJob.class, "retry car park live data job");
    }


    @Bean (name = "retryCarParkLiveDataTrigger")
    public CronTriggerFactoryBean triggerRetryCarParkLiveData (@Qualifier("retryCarParkLiveData") JobDetail jobDetail) {

        return  QuartzConfig.createCronTrigger(jobDetail, "0 */1 * ? * *", "retry car park live data trigger");
    }
}
