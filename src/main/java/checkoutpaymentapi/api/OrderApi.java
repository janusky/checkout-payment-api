package checkoutpaymentapi.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import checkoutpaymentapi.exception.AppException;
import checkoutpaymentapi.exception.ProcessException;
import checkoutpaymentapi.model.OrderData;
import checkoutpaymentapi.model.OrderEvent;
import checkoutpaymentapi.order.OrderStateTransitionsManager;
import checkoutpaymentapi.payload.PaymentResponse;
import checkoutpaymentapi.payload.StatusResponse;
import checkoutpaymentapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class OrderApi {
	private final OrderStateTransitionsManager stateTrasitionsManager;

	@Autowired
	AppException appException;

	@Autowired
	private OrderService orderService;

	/**
	 * Quick API to test the payment event
	 * 
	 * @param amount
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@PostMapping("${app.route.api}/orders/{id}/payment/{amount}")
	public Mono<PaymentResponse> payment(@PathVariable double amount, @PathVariable UUID id) throws Exception {
		// TODO 2024/02/12 janusky@gmail.com - Use @Valid or Validate here

		OrderData data = new OrderData();
		data.setPayment(amount);
		data.setOrderId(id);
		data.setEvent(OrderEvent.pay);
		data = (OrderData) stateTrasitionsManager.processPreEvent(data);

		PaymentResponse pResponse = PaymentResponse.builder().build();
		StatusResponse statusResp = StatusResponse.builder()
				.message(((OrderEvent) data.getEvent()).getMessage() + ", orderId = " + data.getOrderId()).build();
		pResponse.setTransaction(data.getOrderId().toString());
		pResponse.setStatus(statusResp);
		return Mono.just(pResponse);
	}

	/**
	 * API to test the order submit event
	 * 
	 * @return
	 * @throws ProcessException
	 */
	@PostMapping("${app.route.api}/orders/items")
	public Mono<PaymentResponse> submit() throws ProcessException {
		// TODO 2024/02/13 janusky@gmail.com - Recibir referencia de configuración de
		// sesión checkout para la empresa/company.

		PaymentResponse pResponse = PaymentResponse.builder().build();

		OrderData data = new OrderData();
		data.setEvent(OrderEvent.submit);
		data = (OrderData) stateTrasitionsManager.processPreEvent(data);

		StatusResponse statusResp = StatusResponse.builder().httpStatus(HttpStatus.CREATED)
				.message(((OrderEvent) data.getEvent()).getMessage() + ", orderId = " + data.getOrderId()).build();
		pResponse.setTransaction(data.getOrderId().toString());
		pResponse.setStatus(statusResp);
		return Mono.just(pResponse);
	}

	@GetMapping("${app.route.api}/orders/{id}")
	public Mono<ResponseEntity<OrderData>> getUserById(@PathVariable UUID id) {
		// TODO 2024/02/13 janusky@gmail.com - TEST
		return Mono.fromCallable(() -> {
			OrderData order = orderService.getById(id);
			if (order == null) {
				throw appException.of("error.notfound", id);
			}
			return new ResponseEntity<>(order, HttpStatus.OK);
		});
	}
}
