package io.pivotal.config.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TestConfigClientApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestConfigClientApplication.class).properties(
				"spring.cloud.config.enabled=true").run(args);
	}
}
