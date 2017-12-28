package ua.oleksiiholyk.service;

import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import ua.oleksiiholyk.controller.WaterbotrainerController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by Oleksii on 28.12.2017.
 */
public class ScheduleService {
    private static Logger logger = LoggerFactory.getLogger(WaterbotrainerController.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    TaskScheduler taskScheduler;

    ScheduledFuture<?> scheduledFuture;

    MessengerActions messengerActions;

    void start(String recipientId, String text) {
        scheduledFuture = taskScheduler.schedule(sendMessageSchedule(recipientId, text), setCronTrigger("0 * * * * *"));
    }

    public void stop() {
        scheduledFuture.cancel(false);
    }

    private Runnable sendMessageSchedule(String recipientId, String text) {
        return () -> {
            try {
                messengerActions.sendTextMessage(recipientId, text);
            } catch (MessengerApiException | MessengerIOException e) {
                e.printStackTrace();
            }
        };
    }

    private Trigger setCronTrigger(String cronValue) {
        return triggerContext -> {
            CronTrigger trigger1 = new CronTrigger(cronValue);
            return trigger1.nextExecutionTime(triggerContext);
        };
    }
}
