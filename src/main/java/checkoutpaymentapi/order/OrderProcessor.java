package checkoutpaymentapi.order;

import org.springframework.stereotype.Service;

import checkoutpaymentapi.model.OrderData;
import checkoutpaymentapi.model.OrderEvent;
import checkoutpaymentapi.state.AbstractProcessor;
import checkoutpaymentapi.state.ProcessData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProcessor extends AbstractProcessor {

    @Override
    public ProcessData process(ProcessData data) {
        // TODO 2024/02/13 janusky@gmail.com - perform tasks necessary for creating order
        ((OrderData) data).setEvent(OrderEvent.orderCreated);
        return data;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

}
