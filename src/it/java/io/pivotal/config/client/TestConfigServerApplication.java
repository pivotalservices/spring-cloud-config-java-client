package io.pivotal.config.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TestConfigServerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestConfigServerApplication.class).properties(
				"spring.cloud.config.enabled=true").run(args);
	}
}
