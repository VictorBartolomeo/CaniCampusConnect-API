
package org.example.canicampusconnectapi.scheduler;

import org.example.canicampusconnectapi.service.registration.RegistrationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final RegistrationService registrationService;

    public ScheduledTasks(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Expire automatiquement les registrations des cours passés tous les jours à minuit (voir cron)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void expireOldPendingRegistrations() {
        registrationService.expirePastPendingRegistrations();
        System.out.println("Tâche d'expiration des registrations passées exécutée à " +
                java.time.Instant.now());
    }
}