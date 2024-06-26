package com.playground;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
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
        private static final Logger logger = LoggerFactory.getLogger(PlaygroundVideoBackendApplication.class);
        private static final String SSL_TRUST_STORE = "javax.net.ssl.trustStore";
        private static final String SSL_TRUST_STORE_PASWORD = "javax.net.ssl.trustStorePassword";
        private static final String SSL_TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType";
        private static final String KEY_STORE_TYPE = "JKS";
        private static final String DEFAULT_KEY_STORE_PASWORD = "playground";
        private static final String DEFAULT_KEYSTORE = "rds-truststore.jks";
        private static final String SSL_KEYSTORE = "sslKeyStore";

        private SSLContextHelper() {
        }

        private static void setSslProperties() {
            try {
                String sslKeyStore = System.getProperty(SSL_KEYSTORE);
                logger.info("-DsslKeyStore={}", sslKeyStore);
                if (StringUtils.isEmpty(sslKeyStore)) {
                    sslKeyStore = DEFAULT_KEYSTORE;
                }
                logger.info("ssl keystore path: {}", sslKeyStore);
                System.setProperty(SSL_TRUST_STORE, sslKeyStore);
                System.setProperty(SSL_TRUST_STORE_TYPE, KEY_STORE_TYPE);
                System.setProperty(SSL_TRUST_STORE_PASWORD, DEFAULT_KEY_STORE_PASWORD);
            } catch (Exception e) {
                logger.error("Exection failed",e);
            }
        }
    }

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
}
