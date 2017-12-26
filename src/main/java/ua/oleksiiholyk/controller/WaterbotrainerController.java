package ua.oleksiiholyk.controller;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.webhook.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.oleksiiholyk.service.EventHandlerActions;

import static com.github.messenger4j.Messenger.*;

import static java.util.Optional.of;

/**
 * Created by Oleksii on 22.12.2017.
 */
@RestController
@RequestMapping("/water")
public class WaterbotrainerController {
    private static Logger logger = LoggerFactory.getLogger(WaterbotrainerController.class);

    private final Messenger messenger;

    private final EventHandlerActions eventHandlerActions;

    public WaterbotrainerController(Messenger messenger, EventHandlerActions eventHandlerActions) {
        this.messenger = messenger;
        this.eventHandlerActions = eventHandlerActions;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> verifyWebhook(
            @RequestParam(MODE_REQUEST_PARAM_NAME) String mode,
            @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) String verifyToken,
            @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) String challenge) {
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
    public ResponseEntity<Void> handleCallback(@RequestBody String payload, @RequestHeader(SIGNATURE_HEADER_NAME) String signature) {
        try {
            messenger.onReceiveEvents(payload, of(signature), this::eventHandler);
            logger.info("Processed callback payload successfully");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessengerVerificationException e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private void eventHandler(Event event) {
        try {
            if (event.isTextMessageEvent()) {
                eventHandlerActions.textMessageEventHandler(event);
            } else if (event.isPostbackEvent()) {
                eventHandlerActions.postbackEventHandler(event);
            } else if (event.isQuickReplyMessageEvent()) {
                eventHandlerActions.quickReplyMessageEventHandler(event);
            }
        } catch (MessengerApiException | MessengerIOException e) {
            logger.warn("eventHandler exception: {}", e.getMessage());
        }
    }
}
