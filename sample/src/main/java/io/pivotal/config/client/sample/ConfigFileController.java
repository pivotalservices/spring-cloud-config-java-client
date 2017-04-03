package io.pivotal.config.client.sample;

import io.pivotal.config.client.ConfigClientTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ConfigFileController {

    private ConfigClientTemplate configClient;

    @Autowired
    public ConfigFileController(ConfigClientTemplate configClientTemplate) {
        this.configClient = configClientTemplate;
    }

    @RequestMapping(value = "/allprops", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompositePropertySource propertySources() {
        return (CompositePropertySource) configClient.getPropertySource();
    }

    @RequestMapping(value = "/possibles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List possibleValuesByName(@RequestParam(value="name", defaultValue="foo") String name) {
        CompositePropertySource source = (CompositePropertySource) configClient.getPropertySource();
        return source.getPropertySources().stream()
                .map(s -> s.getProperty(name))
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/props", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object sourcesByName(@RequestParam(value="name", defaultValue="foo") String name) {
        CompositePropertySource source = (CompositePropertySource) configClient.getPropertySource();
        return source.getProperty(name);
    }
}
