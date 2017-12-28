package ua.oleksiiholyk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by Oleksii on 28.12.2017.
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

}