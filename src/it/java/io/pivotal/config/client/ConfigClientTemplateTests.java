package io.pivotal.config.client;

import io.pivotal.config.server.TestConfigServer;
import io.pivotal.springcloud.ssl.CloudFoundryCertificateTruster;
import org.junit.*;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
// Explicitly enable config client because test classpath has config server on it
@SpringBootTest(properties = {"spring.cloud.config.enabled=true",
        "logging.level.org.springframework.retry=TRACE"},
        classes = TestConfigClientApplication.class)
@DirtiesContext
public class ConfigClientTemplateTests {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    private static ConfigurableApplicationContext context;

    @BeforeClass
    public static void testConfigServer() {
        context = TestConfigServer.start();
    }

    @AfterClass
    public static void shutdown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    public void testCoolDb() throws Exception {
        assertEquals("mycooldb", new ConfigClientTemplate<Object>("http://localhost:8888", "foo",
                new String[]{"db"}).getProperty("foo.db"));
    }

    @Test
    public void testOverrideSpringProfilesActive() throws Exception {
        environmentVariables.set("SPRING_PROFILES_ACTIVE", "foo,db");
        assertEquals("mycooldb", new ConfigClientTemplate("http://localhost:8888", "foo", null).getProperty("foo.db"));
    }

    @Test
    public void testConfigPrecedenceOrder() throws Exception {
        ConfigClientTemplate<?> configClientTemplate = new ConfigClientTemplate<CompositePropertySource>("http://localhost:8888", "foo",
                new String[]{"development, db"});
        CompositePropertySource source = (CompositePropertySource) configClientTemplate.getPropertySource();
        assertThat("property sources", source.getPropertySources().size(), equalTo(9));
        assertThat(source.getPropertySources().stream()
                        .map(PropertySource::getName)
                        .collect(toList()),
                contains("configClient",
                        "https://github.com/malston/config-repo/foo-db.properties",
                        "https://github.com/malston/config-repo/foo-development.properties",
                        "https://github.com/malston/config-repo/foo.properties",
                        "https://github.com/malston/config-repo/application.yml",
                        "systemProperties",
                        "systemEnvironment",
                        "applicationConfig: [profile=]",
                        "defaultProperties"));
    }

    @Test
    public void testDefaultProperties() throws Exception {
        ConfigClientTemplate<?> configClientTemplate = new ConfigClientTemplate<CompositePropertySource>("http://localhost:8888", "foo",
                new String[]{"default"});
        assertNotNull(configClientTemplate.getPropertySource());
        assertEquals("from foo props", configClientTemplate.getPropertySource().getProperty("foo"));
        assertEquals("test", configClientTemplate.getPropertySource().getProperty("testprop"));
    }

    @Test
    @Ignore
    public void testOauth() throws Exception {
        environmentVariables.set("TRUST_CERTS", "api.cf.markalston.net");
        CloudFoundryCertificateTruster.trustCertificates();
        ClientCredentialsResourceDetails ccrd = new ClientCredentialsResourceDetails();
        ccrd.setAccessTokenUri("https://p-spring-cloud-services.uaa.cf.markalston.net/oauth/token");
        ccrd.setClientId("p-config-server-295a481e-a710-426f-bae6-166a8b692ab9");
        ccrd.setClientSecret("ny7qIeAu7zFr");

        RestTemplate restTemplate = new OAuth2RestTemplate(ccrd);
        ConfigClientTemplate<?> configClientTemplate = new ConfigClientTemplate<CompositePropertySource>(restTemplate, "https://config-b03abf7e-8e74-4218-804a-306c2885f366.cf.markalston.net", "hello-tomcat",
                new String[]{"default"}, true);
        assertNotNull(configClientTemplate.getPropertySource());
        assertEquals("from foo props", configClientTemplate.getPropertySource().getProperty("foo"));
        assertEquals("test", configClientTemplate.getPropertySource().getProperty("testprop"));
    }

    @Test
    public void testLoadLocalConfigurationFromConfigServer() throws Exception {
        ConfigClientTemplate<?> configClientTemplate = new ConfigClientTemplate("http://localhost:8888", "application",
                new String[]{"default"});
        PropertySource<?> source = configClientTemplate.getPropertySource();
        assertNotNull(source);
        String foo = (String) configClientTemplate.getPropertySource().getProperty("foo");
        assertEquals("baz", foo);
    }

    @Test
    public void testLoadEnvironmentVariableFromConfigServer() throws Exception {
        ConfigClientTemplate<?> configClientTemplate = new ConfigClientTemplate("http://localhost:8888", "application",
                new String[]{"default"});
        environmentVariables.set("CONFIG_TEST", "foobar");
        assertEquals("foobar", System.getenv("CONFIG_TEST"));
        PropertySource<?> source = configClientTemplate.getPropertySource();
        assertNotNull(source);
        String test = (String) configClientTemplate.getPropertySource().getProperty("CONFIG_TEST");
        assertEquals(test, "foobar");
    }

}
