package checkoutpaymentapi.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import checkoutpaymentapi.CheckoutPaymentApiApplication;
import checkoutpaymentapi.model.OrderData;
import checkoutpaymentapi.model.OrderEvent;
import checkoutpaymentapi.model.OrderState;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev") // -Dspring.profiles.active=dev
@SpringBootTest(classes = CheckoutPaymentApiApplication.class)
class OrderStateTransitionsManagerTest {
	@MockBean
	private OrderProcessor orderProcessor;

	private OrderDbService dbService = new OrderDbService();

	@MockBean
	PaymentProcessor paymentProcessor;

	@Autowired
	private OrderStateTransitionsManager orderStateTransitionsManager;

	@Test
	public void givenOrderSubmit_whenOrderCreated_thenAssertPaymentPendingState() throws Exception {
		OrderData data = OrderData.builder().event(OrderEvent.submit).build();

		when(orderProcessor.process(any())).thenReturn(MockData.SubmitSuccessData());
		data = (OrderData) orderStateTransitionsManager.processPreEvent(data);

		assertThat(dbService.getStates().get(data.getOrderId())).isEqualTo(OrderState.PaymentPending);
	}

	@Test
	public void givenOrderPay_whenPaymentEror_thenAssertPaymentPendingState() throws Exception {
		OrderData data = OrderData.builder().orderId(MockData.orderId).event(OrderEvent.pay).payment(0.00).build();
		dbService.getStates().put(MockData.orderId, OrderState.PaymentPending);
		when(paymentProcessor.process(any())).thenReturn(MockData.paymentErrorData());
		data = (OrderData) orderStateTransitionsManager.processPreEvent(data);

		assertThat(dbService.getStates().get(data.getOrderId())).isEqualTo(OrderState.PaymentPending);
	}

	@Test
	public void givenOrderPay_whenPaymentSuccess_thenAssertCreatedState() throws Exception {
		OrderData data = OrderData.builder().orderId(MockData.orderId).event(OrderEvent.pay).payment(1.00).build();

		dbService.getStates().put(MockData.orderId, OrderState.PaymentPending);

		when(paymentProcessor.process(any())).thenReturn(MockData.paymentSuccessData());
		data = (OrderData) orderStateTransitionsManager.processPreEvent(data);

		assertThat(dbService.getStates().get(data.getOrderId())).isEqualTo(OrderState.Completed);
	}

	@Test
	public void givenOrderPayWithUnknownOrderId_thenAssertOrderExceptionIsThrown() throws Exception {
		OrderData data = OrderData.builder().event(OrderEvent.pay).orderId(MockData.unknownOrderId).payment(0.00)
				.build();

		data = (OrderData) orderStateTransitionsManager.processPreEvent(data);
	}
}
