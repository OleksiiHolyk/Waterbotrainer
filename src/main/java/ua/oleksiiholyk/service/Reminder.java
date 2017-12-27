package ua.oleksiiholyk.service;

import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Created by Oleksii on 27.12.2017.
 */

//"3 times a day" "Twice a day" "Once a day"
@Component
public class Reminder {
    private static final Logger logger = LoggerFactory.getLogger(Reminder.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Reminder() {
    }


}
