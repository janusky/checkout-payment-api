package checkoutpaymentapi;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import checkoutpaymentapi.CheckoutPaymentApiApplication;

@SuppressWarnings({ "deprecation", "unused" })
@RunWith(JUnitPlatform.class)
@ActiveProfiles("dev") // -Dspring.profiles.active=dev
@SpringBootTest(classes = CheckoutPaymentApiApplication.class,
	// If config/appication-default.yml exists by default those values (then the indicated ones)
	properties = { 
		"spring.main.web-application-type=reactive", 
		"app.certificates-enable=",
		"server.address=localhost",
		"server.ssl.enabled=true",
		"server.ssl.key-store-type=PKCS12",
		"server.ssl.key-store=config/ssl/key-store.pfx",
		"server.ssl.key-store-password=storepass",
		"server.ssl.trust-store-type=PKCS12",
		"server.ssl.trust-store=config/ssl/trust-store.pfx",
		"server.ssl.trust-store-password=storepass",
		"server.ssl.client-auth=want",
		"logging.level.root=DEBUG"
	},
	webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ClientAuthenticationConfig.class)
public abstract class TestBaseApi {
	protected static final String CLIENT_CN = "client.localhost";

	@LocalServerPort
	protected int port;

	@Value("${server.address:local.localhost}")
	protected String address;

	@Autowired
	protected RestTemplate restTemplate;
	
	@Autowired
	protected WebClient webClient;
}
