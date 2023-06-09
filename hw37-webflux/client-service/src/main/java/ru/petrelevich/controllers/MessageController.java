package ru.petrelevich.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.petrelevich.domain.MessageWithClient;
import ru.petrelevich.domain.MessageDto;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private static final String TOPIC_TEMPLATE = "/topic/response.";
    private final WebClient datastoreClient;
    private final SimpMessagingTemplate template;
    private final long MAGIC_ROOM;

    public MessageController(
            WebClient datastoreClient,
            SimpMessagingTemplate template,
            @Value("${rooms.magic_number}") long magicRoom) {
        this.datastoreClient = datastoreClient;
        this.template = template;
        MAGIC_ROOM = magicRoom;
    }

    @MessageMapping("/message.{roomId}")
    public void getMessage(@DestinationVariable long roomId, MessageWithClient message) {
        logger.info("get message from client:{}, roomId:{}", message, roomId);
        saveMessage(roomId, message)
                .subscribe(msgId -> logger.info("message send id:{}", msgId));
        sendMessage(roomId, new MessageWithClient(HtmlUtils.htmlEscape(message.getMessageStr())));
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        var genericMessage = (GenericMessage<byte[]>) event.getMessage();
        var simpDestination = (String) genericMessage.getHeaders().get("simpDestination");
        if (simpDestination == null) {
            logger.error("Can not get simpDestination header, headers:{}", genericMessage.getHeaders());
            throw new ChatException("Can not get simpDestination header");
        }
        var roomId = parseRoomId(simpDestination);
        var sessionId = (String) genericMessage.getHeaders().get("simpSessionId");
        if (roomId == MAGIC_ROOM) {
            sendMessage(roomId, new MessageWithClient("It's Special Room!!!", sessionId, true));
        }

        getMessagesByRoomId(roomId)
                .doOnError(ex -> logger.error("getting messages for roomId:{} failed", roomId, ex))
                .subscribe(
                        message -> sendMessage(
                                message.roomId(),
                                new MessageWithClient(message.messageStr(), sessionId)
                        )
                );
    }

    private void sendMessage(long roomId, MessageWithClient message) {
        String target = String.format("%s%s", TOPIC_TEMPLATE, roomId);
        template.convertAndSend(target, message);
        logger.info("send to room {}: {} for client {}", roomId, message.getMessageStr(), message.getClientId());
        if (roomId != MAGIC_ROOM) {
            target = String.format("%s%s", TOPIC_TEMPLATE, MAGIC_ROOM);
            var newMsg = new MessageWithClient(
                    String.format("(room %d) %s", roomId, message.getMessageStr()),
                    message.getClientId());
            template.convertAndSend(target, newMsg);
            logger.info("send to magic room {}: {} for client {}", roomId, newMsg.getMessageStr(), newMsg.getClientId());
        }
    }

    private long parseRoomId(String simpDestination) {
        try {
            return Long.parseLong(simpDestination.replace(TOPIC_TEMPLATE, ""));
        } catch (Exception ex) {
            logger.error("Can not get roomId", ex);
            throw new ChatException("Can not get roomId");
        }
    }

    private Mono<Long> saveMessage(long roomId, MessageWithClient message) {
        logger.info("start saveMessage()");
        return datastoreClient.post().uri("/msg")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new MessageDto(roomId, message.getMessageStr()))
                .exchangeToMono(response -> response.bodyToMono(Long.class));
    }

    private Flux<MessageDto> getMessagesByRoomId(long roomId) {
        String uri = String.format("/msg/%s", roomId);
        if (roomId == MAGIC_ROOM) {
            uri = "/msg";
        }
        return datastoreClient.get().uri(uri)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(MessageDto.class);
                    } else {
                        return response.createException().flatMapMany(Mono::error);
                    }
                });
    }
}
