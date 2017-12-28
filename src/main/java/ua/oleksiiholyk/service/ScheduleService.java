package ua.oleksiiholyk.service;

/**
 * Created by Oleksii on 28.12.2017.
 */
public interface ScheduleService {
    void start(String recipientId, String text);
    void stop();
}
