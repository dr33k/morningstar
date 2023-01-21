package com.seven.ije.config.flyway;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig implements FlywayConfigurationCustomizer {

    @Bean
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer(){
        return this;
    }

    @Override
    public void customize(FluentConfiguration configuration) {

        configuration
                .baselineOnMigrate(true)
                .defaultSchema("flyway_history_schema");
    }
}
