package org.example.controller;

import org.example.handler.ShoWebSocketHandler;
import org.example.entity.product.Book;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;


@Controller
public class ChatController {
    private final ShoWebSocketHandler webSocketHandler;

    public ChatController(ShoWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    public void sendNewBookNotification(Book book) throws IOException {
        String notificationMessage = "New book added: " + book.getName() + " by " + book.getAuthor();
        webSocketHandler.sendNewBookNotification(notificationMessage);
    }

    @MessageMapping("/test/ws")
    @SendTo("/test/topic/messages")
    public Message sendMessage(@Payload Message message) {
        return message;
    }
}