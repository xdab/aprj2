package pl.so5dz.aprj2.plugin.dashboard.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import pl.so5dz.aprj2.Aprj2Context;

@Configuration
@ComponentScan(basePackages = "pl.so5dz.aprj2.plugin.dashboard")
public class BeanConfig {

    @Bean
    public Aprj2Context aprj2Context() {
        return Aprj2Context.getInstance();
    }
}
