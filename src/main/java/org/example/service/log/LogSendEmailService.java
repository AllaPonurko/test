package org.example.service.log;

import jakarta.transaction.Transactional;
import org.example.dto.EmailReq;
import org.example.entity.log.LogSendEmail;
import org.example.event.EmailSendEvent;
import org.example.repository.LogSendEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class LogSendEmailService {
    @Autowired
    private final LogSendEmailRepository logSendEmailRepository;

    public LogSendEmailService(LogSendEmailRepository logSendEmailRepository) {
        this.logSendEmailRepository = logSendEmailRepository;
    }
    @EventListener()
    public void wrightLog(EmailSendEvent event){
        String status= event.getStatus();
        String type= event.getType();
        EmailReq emailReq= (EmailReq) event.getSource();
        LogSendEmail logSendEmail=new LogSendEmail();
        logSendEmail.setToSend(emailReq.sendTo());
        logSendEmail.setTheme(emailReq.theme());
        logSendEmail.setStatus(status);
        logSendEmail.setType(type);
        logSendEmailRepository.save(logSendEmail);
    }
}
