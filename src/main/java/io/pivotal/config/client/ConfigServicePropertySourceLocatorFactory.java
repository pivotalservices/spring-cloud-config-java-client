package io.pivotal.config.client;


import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ConfigServicePropertySourceLocatorFactory {

    private static final String HTTPS_SCHEME = "https://";

    private static final String HTTP_SCHEME = "http://";

    private ConfigClientProperties defaults;

    public final ConfigServicePropertySourceLocator newConfigServicePropertySourceLocator(final RestTemplate restTemplate, final String configServerUrl, final String app, String[] profiles, ConfigurableEnvironment environment) {
        Assert.hasLength(configServerUrl, "You MUST set the config server URI");
        if (!configServerUrl.startsWith(HTTP_SCHEME) && !configServerUrl.startsWith(HTTPS_SCHEME)) {
            throw new RuntimeException("You MUST put the URI scheme in front of the config server URI");
        }
        if (profiles == null || "".equals(profiles)) {
            profiles = new String[]{"default"};
        }
        Map<String, Object> defaultProperties = new HashMap<>();
                defaultProperties.put("spring.application.name", app != null ? app : System.getenv("SPRING_APPLICATION_NAME") != null ? System.getenv("SPRING_APPLICATION_NAME") : "application");
        environment.getPropertySources()
                .addLast(new MapPropertySource(ConfigClientTemplate.ConfigFileEnvironmentProcessor.DEFAULT_PROPERTIES, defaultProperties));
        for (String profile : profiles) {
            environment.addActiveProfile(profile);
        }
        this.defaults = new ConfigClientProperties(environment);
        this.defaults.setUri(configServerUrl);
        ConfigServicePropertySourceLocator locator = new ConfigServicePropertySourceLocator(defaults);
        locator.setRestTemplate(restTemplate);
        return locator;
    }

    public ConfigClientProperties getConfigClientProperties() {
        return defaults;
    }

}
