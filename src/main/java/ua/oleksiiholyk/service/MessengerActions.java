package ua.oleksiiholyk.service;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.message.RichMediaMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.richmedia.UrlRichMediaAsset;
import com.github.messenger4j.send.recipient.IdRecipient;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.github.messenger4j.send.message.richmedia.RichMediaAsset.Type.IMAGE;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Created by Oleksii on 26.12.2017.
 */
@Service
public class MessengerActions {
    private final Messenger messenger;

    public MessengerActions(Messenger messenger) {
        this.messenger = messenger;
    }

    void sendTextMessage(String recipientId, String text) throws MessengerApiException, MessengerIOException {
        try {
            MessagePayload payload = MessagePayload.create(recipientId, TextMessage.create(text));
            messenger.send(payload);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    void sendTextMessageWithQuickReplies(String recipientId, String quickReplyText, List<QuickReply> quickReplies) {
        try {
            IdRecipient recipient = IdRecipient.create(recipientId);
            TextMessage message = TextMessage.create(quickReplyText, of(quickReplies), empty());
            MessagePayload payload = MessagePayload.create(recipient, message, empty());
            messenger.send(payload);
        } catch (MessengerIOException | MessengerApiException e) {
            e.printStackTrace();
        }
    }

    void sendImageMessageByURL(String recipientId, String imageUrl) {
        try {
            final UrlRichMediaAsset richMediaAsset;
            richMediaAsset = UrlRichMediaAsset.create(IMAGE, new URL(imageUrl));
            final RichMediaMessage richMediaMessage = RichMediaMessage.create(richMediaAsset);
            final MessagePayload payload = MessagePayload.create(recipientId, richMediaMessage);
            messenger.send(payload);
        } catch (MalformedURLException | MessengerIOException | MessengerApiException e) {
            e.printStackTrace();
        }
    }


}