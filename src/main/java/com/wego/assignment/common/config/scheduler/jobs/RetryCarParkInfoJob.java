package com.wego.assignment.common.config.scheduler.jobs;

import com.wego.assignment.common.service.CarParkInfoCSVService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@DisallowConcurrentExecution
public class RetryCarParkInfoJob implements Job {


   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   @Autowired
    CarParkInfoCSVService carParkInfoCSVService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {

            System.out.print("job came in at " + new Date());
            //HIFILogger.logError("*********************** START OF IMS retry change events ********************************");



            //HIFILogger.logError("*********************** END OF IMS exports retry change events :: " + (new Date().getTime() - startT.getTime()) + " milliseconds ******************");

        }
        catch(Exception e)
        {

            throw new JobExecutionException(e.getMessage(),e);
        }

    }
}