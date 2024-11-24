package pl.so5dz.aprj2.plugin.dashboard.spring.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {

    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newFixedThreadPool(2);
    }
}