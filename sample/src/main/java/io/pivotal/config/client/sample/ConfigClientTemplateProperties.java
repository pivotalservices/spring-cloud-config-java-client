package io.pivotal.config.client.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("config.client")
public class ConfigClientTemplateProperties {

    @Value("${spring.application.name:application}")
    private String name;

    private String configServerUrl;

    @Value("${spring.profiles.active:default}")
    private String[] profiles;

    @Value("${spring.cloud.config.failFast:false}")
    private boolean failFast;

    public String getName() {
        return name;
    }

    public String getConfigServerUrl() {
        return configServerUrl;
    }

    public void setConfigServerUrl(String configServerUrl) {
        this.configServerUrl = configServerUrl;
    }

    public String[] getProfiles() {
        return profiles;
    }

    public boolean isFailFast() {
        return failFast;
    }
}
