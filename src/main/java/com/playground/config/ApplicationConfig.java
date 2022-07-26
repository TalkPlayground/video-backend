package com.playground.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.playground.domain.OtpSessions;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
public class ApplicationConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.playground"))
				.build();
	}
	
//	@Bean
//	public RestTemplate restTemplate() {
//	    RestTemplate restTemplate = new RestTemplate();
//	    HttpClient httpClient = HttpClientBuilder.create().build();
//	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//	    restTemplate.setRequestFactory(requestFactory);
//	    return restTemplate;
//	}
	
	  @Bean
	    public RestTemplate restTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyManagementException {
	        TrustManager[] trustAllCerts = new TrustManager[] {
	                (TrustManager) new X509TrustManager() {
	                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                        return new X509Certificate[0];
	                    }
	                    public void checkClientTrusted(
	                            java.security.cert.X509Certificate[] certs, String authType) {
	                    }
	                    public void checkServerTrusted(
	                            java.security.cert.X509Certificate[] certs, String authType) {
	                    }
	                }
	        };  
	        SSLContext sslContext = SSLContext.getInstance("SSL");
	        sslContext.init(null, trustAllCerts, new java.security.SecureRandom()); 
	        CloseableHttpClient httpClient = HttpClients.custom()
	                .setSSLContext(sslContext)
	                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	                .build();   
	        HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
	        customRequestFactory.setHttpClient(httpClient);
	        return builder.requestFactory(() -> customRequestFactory).build();  
	    }
	
//	@Bean
//	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
//		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
//		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
//		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
//		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//		requestFactory.setHttpClient(httpClient);
//		RestTemplate restTemplate = new RestTemplate(requestFactory);
//		return restTemplate;
//	}
	
	@Bean
	public OtpSessions otpSessions() {
		return new OtpSessions();
	}
}
