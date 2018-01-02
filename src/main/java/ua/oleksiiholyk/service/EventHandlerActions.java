package ua.oleksiiholyk.service;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.userprofile.UserProfile;
import com.github.messenger4j.webhook.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by Oleksii on 26.12.2017.
 */
@Service
public class EventHandlerActions {
    private final Messenger messenger;
    private final MessengerActions messengerActions;

    public EventHandlerActions(Messenger messenger, MessengerActions messengerActions) {
        this.messenger = messenger;
        this.messengerActions = messengerActions;
    }

    @Value("${cronValue.threeTimesDay}")
    private String threeTimesDay;

    @Value("${cronValue.twiceDay}")
    private String twiceDay;

    @Value("${cronValue.onceDay}")
    private String onceDay;

    @Value("${cronValue.onceMinute}")
    private String onceMinute;

    @Value("${cronValue.onceTenSec}")
    private String once10Sec;


    public void textMessageEventHandler(Event event) {
        String senderId = event.senderId();
        String text = event.asTextMessageEvent().text();
        try {
            messengerActions.sendTextMessage(senderId, text);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    public void postbackEventHandler(Event event) throws MessengerApiException, MessengerIOException {
        String messageText = event.asPostbackEvent().payload().get();
        String senderId = event.senderId();
        UserProfile userProfile = messenger.queryUserProfile(senderId);
        switch (messageText.toLowerCase()) {
            case "quick reply":
//                sendQuickReply(senderId);
                break;

            case "read receipt":
//                sendReadReceipt(senderId);
                break;

            case "typing on":
//                sendTypingOn(senderId);
                break;

            case "typing off":
//                sendTypingOff(senderId);
                break;
            case "start":
                messengerActions.sendTextMessage(senderId, "Hi " + userProfile.firstName() + "! I am your personal water trainer :-)");
                String quickReplyText = "\u2611  Daily water reminders \n\u2611  Personalized AI recommendations \n\u2611  Number of cups of water drank this week \n\u2611  Tips about water drinking";
                TextQuickReply quickReplyA = TextQuickReply.create("Let's start", "<POSTBACK_PAYLOAD>");
                List<QuickReply> quickReplies = Collections.singletonList(quickReplyA);
                messengerActions.sendTextMessageWithQuickReplies(senderId, quickReplyText, quickReplies);
                break;
            case "changefrequency":
                String changeFrQuickReplyDuickReplyText = "Changing frequency is super easy. Select new frequency:";
                TextQuickReply changeFrQuickReplyA = TextQuickReply.create("3 times a day", "<POSTBACK_PAYLOAD>");
                TextQuickReply changeFrQuickReplyB = TextQuickReply.create("Twice a day", "<POSTBACK_PAYLOAD>");
                TextQuickReply changeFrQuickReplyC = TextQuickReply.create("Once a day", "<POSTBACK_PAYLOAD>");
                TextQuickReply changeFrQuickReplyD = TextQuickReply.create("Stop Reminders", "<POSTBACK_PAYLOAD>");
                TextQuickReply quickReplyD = TextQuickReply.create("Once a minute", "<POSTBACK_PAYLOAD>");
                TextQuickReply quickReplyE = TextQuickReply.create("Once in ten sec", "<POSTBACK_PAYLOAD>");


                List<QuickReply> cupsQuickReplies = Arrays.asList(changeFrQuickReplyA, changeFrQuickReplyB, changeFrQuickReplyC, changeFrQuickReplyD, quickReplyD, quickReplyE);
                messengerActions.sendTextMessageWithQuickReplies(senderId, changeFrQuickReplyDuickReplyText, cupsQuickReplies);
                break;
            default:
                messengerActions.sendTextMessage(senderId, messageText);
        }
    }

    public void quickReplyMessageEventHandler(Event event) throws MessengerApiException, MessengerIOException {
        String messageText = event.asQuickReplyMessageEvent().text();
        String senderId = event.senderId();
        UserProfile userProfile = messenger.queryUserProfile(senderId);

        switch (messageText.toLowerCase()) {
            case "quick reply":
                break;

            case "read receipt":
                break;

            case "typing on":
                break;

            case "typing off":
                break;

            case "let's start":
                messengerActions.sendTextMessage(senderId, "Before we begin...");
                String cupsQuickReplyText = "How many cups of water do you drink a day?";
                TextQuickReply cupsQuickReplyA = TextQuickReply.create("1-2 cups", "<POSTBACK_PAYLOAD>");
                TextQuickReply cupsQuickReplyB = TextQuickReply.create("3-5 cups", "<POSTBACK_PAYLOAD>");
                TextQuickReply cupsQuickReplyC = TextQuickReply.create("6 and more", "<POSTBACK_PAYLOAD>");
                TextQuickReply cupsQuickReplyD = TextQuickReply.create("I don't count", "<POSTBACK_PAYLOAD>");

                List<QuickReply> cupsQuickReplies = Arrays.asList(cupsQuickReplyA, cupsQuickReplyB, cupsQuickReplyC, cupsQuickReplyD);
                messengerActions.sendTextMessageWithQuickReplies(senderId, cupsQuickReplyText, cupsQuickReplies);
                break;

            case "1-2 cups":
                String cup1ImgUrl = "https://scontent-arn2-1.xx.fbcdn.net/v/t34.0-0/p280x280/17198292_263277337448448_1721647584_n.jpg?_nc_ad=z-m&_nc_cid=0&oh=064609d5574582ddd760805fc0bcd62e&oe=5A482826";
                chooseReminder(senderId, cup1ImgUrl);
                break;

            case "3-5 cups":
                String cup2ImgUrl = "https://scontent-arn2-1.xx.fbcdn.net/v/t34.0-0/p280x280/17821593_277151852727663_561650605_n.jpg?_nc_ad=z-m&_nc_cid=0&oh=df46d34ed62f708ebe4e2133bfb005f3&oe=5A481796";
                chooseReminder(senderId, cup2ImgUrl);
                break;

            case "6 and more":
                String cup3ImgUrl = "https://scontent-arn2-1.xx.fbcdn.net/v/t34.0-0/p280x280/16977125_258724977903684_188539519_n.jpg?_nc_ad=z-m&_nc_cid=0&oh=b6fde18be841dee25cff10101abe87cc&oe=5A47B2D1";
                messengerActions.sendImageMessageByURL(senderId, cup3ImgUrl);

                String cup3TextMsg = "Your'e a real champ \uD83E\uDD42 8 cups is the recommended amount.";
                messengerActions.sendTextMessage(senderId, cup3TextMsg);

                String cup3QuickReplyText = "Set a daily reminder to keep track with your good work";
                TextQuickReply cup3QuickReplyA = TextQuickReply.create("Once a day", "<POSTBACK_PAYLOAD>");

                List<QuickReply> cup3QuickReplies = Arrays.asList(cup3QuickReplyA);
                messengerActions.sendTextMessageWithQuickReplies(senderId, cup3QuickReplyText, cup3QuickReplies);
                break;

            case "i don't count":
                String cup4ImgUrl = "https://scontent-arn2-1.xx.fbcdn.net/v/t34.0-0/p280x280/17198292_263277337448448_1721647584_n.jpg?_nc_ad=z-m&_nc_cid=0&oh=064609d5574582ddd760805fc0bcd62e&oe=5A482826";
                chooseReminder(senderId, cup4ImgUrl);
                break;

            case "3 times a day":
                start(senderId, "Water time!", threeTimesDay);
                changeReminderFrequency(senderId, userProfile.firstName());
                break;

            case "twice a day":
                start(senderId, "Water time!", twiceDay);
                changeReminderFrequency(senderId, userProfile.firstName());
                break;

            case "once a day":
                start(senderId, "Water time!", onceDay);
                changeReminderFrequency(senderId, userProfile.firstName());
                break;

            case "once a minute":
                start(senderId, "Water time!", onceMinute);
                changeReminderFrequency(senderId, userProfile.firstName());
                break;

            case "once in ten sec":
                start(senderId, "Water time!", onceMinute);
                changeReminderFrequency(senderId, userProfile.firstName());
                break;

            case "stop reminders":
                changeReminderFrequency(senderId, userProfile.firstName());
                stop();
                break;

            case "setfr":
                String setFrText = userProfile.firstName() + " new frequency was set succesfuly \uD83D\uDE42 thanks";
                messengerActions.sendTextMessage(senderId, setFrText);
                break;

            case "done":
                String doneImgUrl = "https://scontent.fiev2-1.fna.fbcdn.net/v/t34.0-0/p280x280/17806944_277152209394294_1375427760_n.jpg?oh=e22479e7f72db2850b33b135724dd835&oe=5A43E56C";
                messengerActions.sendImageMessageByURL(senderId, doneImgUrl);

                String doneText1 = "Well done " + userProfile.firstName() + "! Keep it up!";
                messengerActions.sendTextMessage(senderId, doneText1);

                String doneText2 = "You can always get to the menu by asking for \"Menu\" \uD83D\uDE42";
                messengerActions.sendTextMessage(senderId, doneText2);
                break;

            default:
                messengerActions.sendTextMessage(senderId, messageText);
        }
    }

    private void chooseReminder(String senderId, String chooserReminderImgUrl) {
        try {
            messengerActions.sendImageMessageByURL(senderId, chooserReminderImgUrl);

            String chooserReminderText = "Recommended amount of water per day is eight 8-ounce glasses, equals to about 2 liters, or half a gallon.";
            messengerActions.sendTextMessage(senderId, chooserReminderText);

            String quickReplyText = "Choose the frequency for water break reminders";
            TextQuickReply quickReplyA = TextQuickReply.create("3 times a day", "<POSTBACK_PAYLOAD>");
            TextQuickReply quickReplyB = TextQuickReply.create("Twice a day", "<POSTBACK_PAYLOAD>");
            TextQuickReply quickReplyC = TextQuickReply.create("Once a day", "<POSTBACK_PAYLOAD>");
            TextQuickReply quickReplyD = TextQuickReply.create("Once a minute", "<POSTBACK_PAYLOAD>");
            TextQuickReply quickReplyE = TextQuickReply.create("Once in ten sec", "<POSTBACK_PAYLOAD>");

            List<QuickReply> cupsQuickReplies = Arrays.asList(quickReplyA, quickReplyB, quickReplyC, quickReplyD, quickReplyE);
            messengerActions.sendTextMessageWithQuickReplies(senderId, quickReplyText, cupsQuickReplies);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private void remindersDone(String senderId) {
        try {
            String remDoneQuickReplyText1 = "Noted \uD83D\uDE42 Let's give it a try now";

            messengerActions.sendTextMessage(senderId, remDoneQuickReplyText1);

            String remDoneQuickReplyText2 = "Drink 1 cup of water and press the button";
            TextQuickReply remDoneQuickReply1 = TextQuickReply.create("Done", "<POSTBACK_PAYLOAD>");
            List<QuickReply> remDoneQuickReplyList = Arrays.asList(remDoneQuickReply1);
            messengerActions.sendTextMessageWithQuickReplies(senderId, remDoneQuickReplyText2, remDoneQuickReplyList);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private void changeReminderFrequency(String senderId, String userName) {
//        Oleksii new frequency was set succesfuly \uD83D\uDE42 thanks
        try {
            String msg = userName + " new frequency was set succesfuly \uD83D\uDE42 thanks";
            messengerActions.sendTextMessage(senderId, msg);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }


    }

   /* private void start(String recipientId, String text, String cronValue) {
        scheduledFuture = taskScheduler.schedule(sendMessageSchedule(recipientId, text), setCronTrigger(cronValue));
    }

    private void stop() {
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
    }*/

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private void start(String recipientId, String text, String cronValue) {
        taskScheduler.initialize();
        taskScheduler.schedule(new RunnableTask(recipientId, text), setCronTrigger(cronValue));
    }

    private void stop() {
        taskScheduler.shutdown();
    }

    private Trigger setCronTrigger(String cronValue) {
        return triggerContext -> {
            CronTrigger trigger1 = new CronTrigger(cronValue);
            return trigger1.nextExecutionTime(triggerContext);
        };
    }


    class RunnableTask implements Runnable {
        private String recipientId;
        private String text;

        RunnableTask(String recipientId, String text) {
            this.recipientId = recipientId;
            this.text = text;
        }

        @Override
        public void run() {
            try {
                messengerActions.sendTextMessage(recipientId, text);
            } catch (MessengerApiException | MessengerIOException e) {
                e.printStackTrace();
            }
        }
    }
}