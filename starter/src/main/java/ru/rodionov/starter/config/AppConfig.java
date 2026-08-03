package ru.rodionov.starter.config;


import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.rodionov.starter.util.DateUtil;

import java.util.Optional;

@Configuration
@ComponentScan(basePackages = "ru.rodionov.starter")
@ConfigurationPropertiesScan(basePackages = "ru.rodionov.starter")
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class AppConfig {

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(DateUtil.truncatedNow());
    }
}
