package com.iot.iot_BE.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Log message từ client
        logger.info("Received message: " + message.getPayload());
        try {
            session.sendMessage(new TextMessage("Reply from server: " + message.getPayload()));
        } catch (Exception e) {
            logger.error("Error sending message", e);
        }
    }
}