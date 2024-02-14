package checkoutpaymentapi;

import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

@Configuration
public class ClientAuthenticationConfig {
	private char[] allPassword = "storepass".toCharArray();
    private String keyStore = "classpath:ssl/client.localhost.pfx";
    
    @Bean
    public RestTemplate restTemplate() throws Exception {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        return restTemplate;
    }
    
    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() throws Exception {    	
    	SSLContext sslContext = SSLContextBuilder
    			.create()
    			.loadKeyMaterial(ResourceUtils.getFile(keyStore), allPassword, allPassword)
    			.loadTrustMaterial(null, new TrustSelfSignedStrategy())
    			.build();
    	
    	//FIXME Remove when you can add the appropriate domain in your hosts file
    	// hosts file	-> c:/Window/System32/etc/diver/hosts or /etc/hosts
    	// 				-> 127.0.0.1	local.localhost
    	// IMPORTANT: To disable Host Name validation, when run as localhost.
    	// allowAllHostnameVerifier() or .setHostnameVerifier(AllowAllHostnameVerifier.INSTANCE) 
    	// or new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
				 sslContext, NoopHostnameVerifier.INSTANCE);
		HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
				.setSSLSocketFactory(sslSocketFactory).build();
		
		HttpClient httpClient = HttpClients.custom()
				.setConnectionManager(cm).evictExpiredConnections().build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }

	@Bean
	public WebClient webClient() throws Exception {
		SslContext sslContext = sslContext();
		reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
				.secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
		ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
		return WebClient.builder().clientConnector(connector)
				// .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE,
				// MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	private SslContext sslContext() throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(this.getClass().getClassLoader().getResourceAsStream(keyStore), allPassword);

		// KeyManagerFactory keyManagerFactory =
		// KeyManagerFactory.getInstance("SunX509");
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(ks, "password".toCharArray());

		return SslContextBuilder.forClient().keyManager(keyManagerFactory)
				.trustManager(InsecureTrustManagerFactory.INSTANCE).clientAuth(ClientAuth.REQUIRE)
				.sslProvider(SslProvider.JDK).build();
	}

	protected void allowAllHostnameVerifier() {
		final Properties props = System.getProperties();
		props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
	}
}
