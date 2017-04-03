package io.pivotal.config.client.sample;

import io.pivotal.config.client.ConfigClientTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ConfigClientTemplateProperties.class)
public class ClientConfiguration {

    @Bean
    public ConfigClientTemplate configClientTemplate(ConfigClientTemplateProperties properties) {
        return new ConfigClientTemplate<>(properties.getConfigServerUrl(), properties.getName(), properties.getProfiles(), properties.isFailFast());
    }
}


