package com.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@EnableAsync
public class PlaygroundVideoBackendApplication {

	public static void main(String[] args) {
		SSLContextHelper.setSslProperties();
		SpringApplication.run(PlaygroundVideoBackendApplication.class, args);
	}

	protected static class SSLContextHelper {
		private static final String SSL_TRUST_STORE = "javax.net.ssl.trustStore";
		private static final String SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
		private static final String SSL_TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType";
		private static final String KEY_STORE_TYPE = "JKS";
		private static final String DEFAULT_KEY_STORE_PASSWORD = "playground";
		private static final String DEFAULT_KEYSTORE = "rds-truststore.jks";

		private static void setSslProperties() {
			try {
				System.setProperty(SSL_TRUST_STORE, DEFAULT_KEYSTORE);
				System.setProperty(SSL_TRUST_STORE_TYPE, KEY_STORE_TYPE);
				System.setProperty(SSL_TRUST_STORE_PASSWORD, DEFAULT_KEY_STORE_PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
