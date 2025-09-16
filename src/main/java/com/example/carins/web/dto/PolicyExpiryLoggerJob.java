package com.example.carins.web.dto;

// com.example.carins.jobs.PolicyExpiryLoggerJob

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class PolicyExpiryLoggerJob {
    private static final Logger log = LoggerFactory.getLogger(PolicyExpiryLoggerJob.class);

    private final InsurancePolicyRepository policyRepo;

    public PolicyExpiryLoggerJob(InsurancePolicyRepository policyRepo) {
        this.policyRepo = policyRepo;
    }


    @Scheduled(cron = "0 */5 * * * *", zone = "Europe/Bucharest")
    @Transactional
    public void logJustExpiredPolicies() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<InsurancePolicy> toLog = policyRepo.findByEndDateAndExpiredNoticeLogged(yesterday, false);

        for (InsurancePolicy p : toLog) {
            Long carId = (p.getCar() != null) ? p.getCar().getId() : null;
            log.info("Policy {} for car {} expired on {}", p.getId(), carId, p.getEndDate());
            p.setExpiredNoticeLogged(true); // prevent future logs
        }
    }
}

