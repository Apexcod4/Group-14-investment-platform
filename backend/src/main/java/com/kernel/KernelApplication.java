package com.kernel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point of the Kernel API.
 *
 * <p>Packages are organised by feature. Each team member owns one:
 * auth, wallet, recommendation, portfolio, automation (see work-breakdown.md).
 */
@SpringBootApplication
@EnableScheduling // used by the automation package (PlanScheduler)
public class KernelApplication {

    public static void main(String[] args) {
        SpringApplication.run(KernelApplication.class, args);
    }
}
