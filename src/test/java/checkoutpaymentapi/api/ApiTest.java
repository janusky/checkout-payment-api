package checkoutpaymentapi.api;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import checkoutpaymentapi.TestBaseApi;
import lombok.extern.slf4j.Slf4j;

/*
 * 1-Run Postgres (cd checkout-payment-api)
 * docker-compose -f src/test/resources/docker/payment_database.yaml up -d
 */
@Slf4j
public class ApiTest extends TestBaseApi {

	@Value("${app.route.api}")
	private String apiHome;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void callHomeApi() {
		// Invoke -> PublicApi.currentUser()
		String urlApi = "https://" + address + ":" + port + apiHome;
		log.debug("url=>{}", urlApi);
		Map<?, ?> response = restTemplate.getForObject(urlApi, Map.class);

		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.get(Api.KEY_USER_SESION));
		Assertions.assertEquals(response.get(Api.KEY_USER_SESION), CLIENT_CN);
	}
}
