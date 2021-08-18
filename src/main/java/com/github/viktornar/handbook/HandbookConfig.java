package com.github.viktornar.handbook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class HandbookConfig {
    @Bean
    public static ExecutorService executorService() {
        final ThreadFactory threadFactory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(false);

            return thread;
        };

        return Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors(), threadFactory);
    }
}
