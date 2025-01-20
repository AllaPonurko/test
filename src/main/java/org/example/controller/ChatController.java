package org.example.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ChatController {

    @MessageMapping("test/chat")
    @SendTo("/test/topic/messages")
    public String send(String message) throws Exception {
        return message;
    }
}