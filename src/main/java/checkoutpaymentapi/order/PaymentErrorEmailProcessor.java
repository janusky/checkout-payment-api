package checkoutpaymentapi.order;

import org.springframework.stereotype.Service;

import checkoutpaymentapi.model.OrderData;
import checkoutpaymentapi.model.OrderEvent;
import checkoutpaymentapi.state.AbstractProcessor;
import checkoutpaymentapi.state.ProcessData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentErrorEmailProcessor extends AbstractProcessor {

	@Override
	public ProcessData process(ProcessData data) {
		// TODO 2024/02/13 janusky@gmail.com - call the email service
		log.info("Sent payment error email");
		((OrderData) data).setEvent(OrderEvent.paymentErrorEmailSent);
		return data;
	}

	@Override
	public boolean isAsync() {
		return false;
	}
}
