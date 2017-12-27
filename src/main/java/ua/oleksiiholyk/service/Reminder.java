package ua.oleksiiholyk.service;

import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;


/**
 * Created by Oleksii on 27.12.2017.
 */

//"3 times a day" "Twice a day" "Once a day"
@Component
public class Reminder {
    private static final Logger logger = LoggerFactory.getLogger(Reminder.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    MessengerActions messengerActions;
    /*
    second, minute, hour, day of month, month, day(s) of week
    "0 0 * * * *" = the top of every hour of every day.
    "10 * * * * *" = every ten seconds.
    "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
    "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
    "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
    "0 0 0 25 12 ?" = every Christmas Day at midnight
    */
   /* @Scheduled(cron = "0 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }

    @Scheduled(fixedRate = 2000)
    public void scheduleTaskWithFixedRate() {
        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
    }

    @Scheduled(fixedDelay = 2000)
    public void scheduleTaskWithFixedDelay() {
        logger.info("Fixed Delay Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            logger.error("Ran into an error {}", ex);
            throw new IllegalStateException(ex);
        }
    }

    @Scheduled(fixedRate = 2000, initialDelay = 5000)
    public void scheduleTaskWithInitialDelay() {
        logger.info("Fixed Rate Task with Initial Delay :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }*/


    @Scheduled(fixedRate = 60000)
    public void scheduleOnceMinute(String recipientId, String text) throws MessengerApiException, MessengerIOException {
        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
        messengerActions.sendTextMessage(recipientId,text);
    }
}
