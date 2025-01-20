package org.example.services.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BackgroundTaskService {
    @Scheduled(fixedRate = 10000)
    public void performBackgroundTask() {
        System.out.println("Running background task...");
    }
}
