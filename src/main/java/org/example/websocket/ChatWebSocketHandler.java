package org.example.websocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        sessions.add(session);
    }
}
