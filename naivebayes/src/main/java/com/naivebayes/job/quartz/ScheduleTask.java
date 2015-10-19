package com.naivebayes.job.quartz;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.naivebayes.service.NaiveBayesService;

@Component
public class ScheduleTask {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	@Autowired
	NaiveBayesService service;

    @Scheduled(fixedRate = 36000) // 1 hr
    //@Scheduled(cron = "0 15 10 15 * ?") executed at 10:15 AM 15th day of every month
    public void reportCurrentTime() {
        long start = System.currentTimeMillis();
        Long id = service.getLatestAnomalyId();
        	service.trainData(id);
        System.out.println("The total time took for the training to be completed " + ( System.currentTimeMillis() - start));
    }
}
