package ua.oleksiiholyk;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.common.SupportedLocale;
import com.github.messenger4j.common.WebviewHeightRatio;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.messengerprofile.MessengerSettings;
import com.github.messenger4j.messengerprofile.getstarted.StartButton;
import com.github.messenger4j.messengerprofile.persistentmenu.LocalizedPersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.PersistentMenu;
import com.github.messenger4j.messengerprofile.persistentmenu.action.NestedCallToAction;
import com.github.messenger4j.messengerprofile.persistentmenu.action.PostbackCallToAction;
import com.github.messenger4j.messengerprofile.persistentmenu.action.UrlCallToAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Created by Oleksii on 28.12.2017.
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class AppConfig {
    /*@Bean
    TaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }*/

    @Bean
    public Messenger messenger(
            @Value("${messenger4j.appSecret}") String appSecret,
            @Value("${messenger4j.verifyToken}") String verifyToken,
            @Value("${messenger4j.pageAccessToken}") String pageAccessToken) throws MessengerApiException, MessengerIOException, MalformedURLException {
        Messenger messenger;

        PostbackCallToAction actionStart = PostbackCallToAction.create("Start", "Start");

        PostbackCallToAction callReminderFrequency = PostbackCallToAction.create("Reminder Frequency", "changeFrequency");
        UrlCallToAction gotoVisitSpartans = UrlCallToAction.create("Visit Spartans", new URL("https://www.spartans.tech/"), of(WebviewHeightRatio.FULL), empty(), empty());
        NestedCallToAction actionMore = NestedCallToAction.create("More", Arrays.asList(callReminderFrequency, gotoVisitSpartans));

        UrlCallToAction actionMessenger4j = UrlCallToAction.create("Create a bot with messenger4j", new URL("https://github.com/messenger4j/messenger4j"), of(WebviewHeightRatio.FULL), empty(), empty());

        PersistentMenu persistentMenu = PersistentMenu.create(false, of(Arrays.asList(actionStart, actionMore, actionMessenger4j)), LocalizedPersistentMenu.create(SupportedLocale.zh_CN, false, empty()));

        MessengerSettings messengerSettings = MessengerSettings.create(of(StartButton.create("Start")), empty(), of(persistentMenu), empty());
        messenger = Messenger.create(pageAccessToken, appSecret, verifyToken);
        messenger.updateSettings(messengerSettings);
        return messenger;
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
