package org.example.controller;

import org.example.dto.EmailReq;
import org.example.enums.EmailSendTypeEnum;
import org.example.service.notification.EmailService;
import org.example.service.notification.MailtrapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final MailtrapClient mailtrapClient;

    @Autowired
    public EmailController(MailtrapClient mailtrapClient) {
        this.mailtrapClient = mailtrapClient;

    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody EmailReq emailReq) {
        String type = EmailSendTypeEnum.MANUAL.getValue();
        mailtrapClient.sendMessage(emailReq,type);
    }
}
