package org.example.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler handler;

    public WebSocketConfig(@Lazy WebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandlerRegistration registration =
                registry.addHandler(new ExceptionWebSocketHandlerDecorator(handler), "/init");
        registration.setAllowedOriginPatterns("*");
    }
}


