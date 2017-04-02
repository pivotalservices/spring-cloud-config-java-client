package io.pivotal.config.client.sample;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ConfigClientSampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ConfigClientSampleApplication.class).properties(
                "spring.cloud.config.enabled=true").run(args);
    }
}