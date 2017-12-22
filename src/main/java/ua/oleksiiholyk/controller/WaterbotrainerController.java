package ua.oleksiiholyk.controller;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.message.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.messenger4j.Messenger.*;
import static java.util.Optional.of;

/**
 * Created by Oleksii on 22.12.2017.
 */
@RestController
@RequestMapping("/water")
public class WaterbotrainerController {
    private static final Logger logger = LoggerFactory.getLogger(WaterbotrainerController.class);

    private final Messenger messenger;

    @Autowired
    public WaterbotrainerController(
            @Value("${messenger4j.appSecret}") final String appSecret,
            @Value("${messenger4j.verifyToken}") final String verifyToken,
            @Value("${messenger4j.pageAccessToken}") final String pageAccessToken) {
        this.messenger = Messenger.create(pageAccessToken, appSecret, verifyToken);
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
                                                @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken,
                                                @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {
        try {
            messenger.verifyWebhook(mode, verifyToken);
            logger.info("Webhook verification completed: {}", challenge);
            return ResponseEntity.ok(challenge);
        } catch (MessengerVerificationException e) {
            logger.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
        logger.info("signature=" + signature);
        logger.info("payload=" + payload);
        logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
        try {
            messenger.onReceiveEvents(payload, of(signature), event -> {
                final String senderId = event.senderId();
                if (event.isTextMessageEvent()) {
                    final String text = event.asTextMessageEvent().text();
                    final TextMessage textMessage = TextMessage.create(text);
                    final MessagePayload messagePayload = MessagePayload.create(senderId, textMessage);
                    try {
                        logger.info("send msg");
                        messenger.send(messagePayload);
                    } catch (MessengerApiException | MessengerIOException e) {
                        logger.warn("msg doen't send");
                    }
                }
            });
            logger.info("Processed callback payload successfully");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessengerVerificationException e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
