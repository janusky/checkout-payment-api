package checkoutpaymentapi.order;

import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import checkoutpaymentapi.service.PaymentProcessorHelper;
import checkoutpaymentapi.state.AbstractProcessor;
import checkoutpaymentapi.state.ProcessData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentProcessor extends AbstractProcessor {
	
	private final PaymentProcessorHelper helper;
    
    @Override
    public void processAsync(ProcessData data, Consumer<ProcessData> consumerFn) {
		helper.process(data, consumerFn);
	}

	@Override
	public boolean isAsync() {
		return true;
	}
}
