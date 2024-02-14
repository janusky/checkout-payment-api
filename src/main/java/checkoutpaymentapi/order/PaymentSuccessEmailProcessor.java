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
public class PaymentSuccessEmailProcessor extends AbstractProcessor {
    
    @Override
    public ProcessData process(ProcessData data) {
		//TODO 2024/02/13 janusky@gmail.com - generate order confirmation number and call the email service
		log.info("Sent payment success email");
		((OrderData)data).setEvent(OrderEvent.paymentSuccessEmailSent);
		return data;
	}

	@Override
	public boolean isAsync() {
		return false;
	}
}
