package org.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.web.socket.TextMessage;

@Slf4j
@Component
public class ShoWebSocketHandler extends TextWebSocketHandler implements WebSocketHandler {
    private static final Set<WebSocketSession> activeSessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        activeSessions.add(session);
        log.info("Connection established: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Got message={}", message.getPayload(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        session.sendMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Error during WebSocket communication", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        activeSessions.remove(session);
        log.info("Connection closed: " + session.getId());
    }

    public void sendNewBookNotification(String bookDetails) throws IOException {
        // Надсилає повідомлення всім підключеним клієнтам
        // Потрібно зберігати всі активні WebSocketSession, щоб відправляти повідомлення всім
        for (WebSocketSession session : activeSessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage("New book added: " + bookDetails));
                }
            } catch (Exception e) {
                log.error("Error sending WebSocket message", e);
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
