package com.kitchen.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class OrderUtil {
    private static final String CRON = "0/1 * * * * ?";
    private static final JobDetail JOB;

    static {
        JOB = JobBuilder.newJob(OrderJob.class)
                .storeDurably()
                .build();
    }

    public static void stop() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.deleteJob(JOB.getKey());
        } catch (SchedulerException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void start() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule(CRON)
                .withMisfireHandlingInstructionDoNothing();

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 配置任务
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(JOB)
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(JOB, trigger);

            // 开启任务
            scheduler.start();
        } catch (SchedulerException e) {
            System.err.println(e.getMessage());
        }
    }
}
