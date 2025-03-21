package org.example.service.notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.EmailReq;
import org.example.enums.EmailSendStatusEnum;
import org.example.event.EmailSendEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MailtrapClient {
    @Value("${mailtrapClient.api.token}")
    private String API_TOKEN;
    @Value("${mailtrapClient.base.url}")
    private String BASE_URL;
    @Value("${inboxId}")
    private long inboxId;
    private static final Logger LOGGER = LogManager.getLogger();
    private final RestTemplate restTemplate;
    @Value("${own.email}")
    private String myEmail;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;

    public MailtrapClient(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.restTemplate = new RestTemplate();
    }
    public String getMessages() {
        String url = BASE_URL + "/inboxes/" + inboxId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Token", API_TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public void sendMessage(EmailReq emailReq, String type) {
        String url = BASE_URL+"/send/3536037";
        HttpHeaders headers = createHttpHeaders();
        Map<String, Object> requestBody = createRequestBody(emailReq);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        try {
            //  commit POST-request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            // control of response's status
            if (response.getStatusCode() == HttpStatus.OK) {
                eventPublisher.publishEvent(new EmailSendEvent(emailReq, EmailSendStatusEnum.SENT.getValue(),type));
                LOGGER.info("Email sent successfully"+ response.getBody());
            } else {
                eventPublisher.publishEvent(new EmailSendEvent(emailReq, EmailSendStatusEnum.FAILED.getValue(),type));
                LOGGER.error("Failed to send email: " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            // log of error
            LOGGER.error("Error: " + e.getResponseBodyAsString());
        }
    }

    private Map<String, Object> createRequestBody(EmailReq emailReq) {
        // prepare of request's body
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> from = new HashMap<>();
        from.put("email", myEmail);
        from.put("name", "My Name");

        List<Map<String, String>> toList = new ArrayList<>();
        Map<String, String> to = new HashMap<>();
        to.put("email", emailReq.sendTo());
        to.put("name", "Recipient Name");
        toList.add(to);
        requestBody.put("from", from);
        requestBody.put("to", toList);
        requestBody.put("subject", emailReq.theme());
        requestBody.put("text", emailReq.body());
        return requestBody;
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_TOKEN);
        headers.set("Content-Type", "application/json");
        return headers;
    }
}