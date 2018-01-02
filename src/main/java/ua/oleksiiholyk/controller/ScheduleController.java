package ua.oleksiiholyk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by Oleksii on 28.12.2017.
 */


//"3 times a day" "Twice a day" "Once a day"

    /*
    second, minute, hour, day of month, month, day(s) of week
    "0 0 * * * *" = the top of every hour of every day.
    "10 * * * * *" = every ten seconds.
    "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
    "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
    "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
    "0 0 0 25 12 ?" = every Christmas Day at midnight
    */
@Controller
public class ScheduleController {
    private static Logger logger = LoggerFactory.getLogger(WaterbotrainerController.class);

    public static final long FIXED_RATE = 5000;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    TaskScheduler taskScheduler;
    ScheduledFuture<?> scheduledFuture;

    @RequestMapping("start")
    ResponseEntity<Void> start() {
        scheduledFuture = taskScheduler.scheduleAtFixedRate(printHour(), FIXED_RATE);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping("stop")
    ResponseEntity<Void> stop() {
        scheduledFuture.cancel(false);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private Runnable printHour() {
        return () ->         logger.info("printHour :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));

    }

    private Trigger setCronTrigger(String cronValue) {
        return triggerContext -> {
            CronTrigger trigger1 = new CronTrigger(cronValue);
            return trigger1.nextExecutionTime(triggerContext);
        };
    }

}