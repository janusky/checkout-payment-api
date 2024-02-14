package checkoutpaymentapi.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import checkoutpaymentapi.TestBaseApi;
import checkoutpaymentapi.payload.PaymentResponse;
import lombok.extern.slf4j.Slf4j;

/*
 * 1-Run Postgres (cd checkout-payment-api)
 * docker-compose -f src/test/resources/docker/payment_database.yaml up -d
 */
@Slf4j
public class OrderApiTest extends TestBaseApi {
	@Value("${app.route.api}/orders")
	private String orderApi;
	
	@Test
	public void orderSubmitTest() throws Exception {
		// Valid.sync()
		String urlApi = "https://" + address + ":" + port + orderApi + "/items";
		//urlApi = "https://local.localhost/api/valids/sync";
		log.debug("url=>{}", urlApi);
		
		String transaction = "2";

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("transaction", transaction);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(bodyMap,
				headers);

		ResponseEntity<PaymentResponse> response = restTemplate.exchange(urlApi, HttpMethod.POST, requestEntity,
				PaymentResponse.class);

		PaymentResponse postResponse = response.getBody();
		Assertions.assertNotNull(postResponse);
		Assertions.assertEquals(HttpStatus.CREATED, postResponse.getStatus().getHttpStatus());
		Assertions.assertNotNull(postResponse.getTransaction());
	}
	
	@Test
	public void paymentTest() throws Exception {
		String transaction = "c35918b7-0071-4ffe-b251-0d4116d62ce0";
		String amount = "1";
		String urlApi = "https://" + address + ":" + port + orderApi + "/" + transaction + "/payment" + "/" + amount;
		log.debug("url=>{}", urlApi);

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("transaction", transaction);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(bodyMap,
				headers);
		ResponseEntity<PaymentResponse> response = restTemplate.exchange(
				urlApi, HttpMethod.POST, requestEntity,
				PaymentResponse.class);

		PaymentResponse postResponse = response.getBody();
		Assertions.assertNotNull(postResponse);
		Assertions.assertEquals(HttpStatus.OK, postResponse.getStatus().getHttpStatus());
		Assertions.assertNotNull(postResponse.getTransaction());
	}
	
//	@Test
//	public void paymentTest() throws Exception {
//		String urlApi = "https://" + address + ":" + port + orderApi + "/{id}/payment/{amount}";
//		log.debug("url=>{}", urlApi);
//
//		String transaction = "2";
//		Integer amount = 1;
//		
//		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(urlApi)
//			    .queryParam("id", transaction)
//			    .queryParam("amount", amount);		
//		Mono<PostResponse> result = webClient.post()
//	      .uri(uriBuilder.build().toUri())
//	      .retrieve()
//	      .toEntity(PostResponse.class)
//	      .doOnError(Exception.class, ex -> {
//	          System.err.println(ex);
//	       })
//	      .flatMap(responseEntity -> Mono.justOrEmpty(responseEntity.getBody()));
//        result.subscribe(System.out::println);
//        
//        webClient.post()
//        	.uri(urlApi, transaction, amount)
//	        .retrieve()
//	        .toEntity(PostResponse.class)
//	        .doOnError(Exception.class, ex -> {
//	          System.err.println(ex);
//	        })
//	        .subscribe(response -> {
//	        	System.out.println("Created: " + response.getBody());
//	            System.out.println("Status: " + response.getStatusCode());
//	            System.out.println("Location URI: " + response.getHeaders().getLocation().toString());
//	        });
//	}
}
