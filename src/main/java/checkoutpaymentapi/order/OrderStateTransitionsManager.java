package checkoutpaymentapi.order;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import checkoutpaymentapi.exception.OrderException;
import checkoutpaymentapi.exception.ProcessException;
import checkoutpaymentapi.model.OrderData;
import checkoutpaymentapi.model.OrderEvent;
import checkoutpaymentapi.model.OrderState;
import checkoutpaymentapi.service.OrderService;
import checkoutpaymentapi.state.AbstractProcessor;
import checkoutpaymentapi.state.AbstractStateTransitionsManager;
import checkoutpaymentapi.state.ProcessData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class manages various state transitions based on the event The
 * superclass AbstractStateTransitionsManager calls the two methods
 * initializeState and processStateTransition in that order
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class OrderStateTransitionsManager extends AbstractStateTransitionsManager {
	private final ApplicationContext context;

	@Autowired
	private OrderService orderService;

	@Override
	protected void processPostEvent(ProcessData data) {
		log.info("Post-event: {}", data.getEvent().toString());

		orderService.save((OrderData) data);

		// if the post-event is either paymentSuccess or paymentError
		// then initiate the email process
		log.info("Final state: " + ((OrderData) data).getOrderId());
		log.info("??*************************************");

		if ((OrderEvent) data.getEvent() == OrderEvent.paymentSuccess) {
			((OrderData) data).setEvent(OrderEvent.successEmail);
			processPreEvent(data);
		} else if ((OrderEvent) data.getEvent() == OrderEvent.paymentError) {
			((OrderData) data).setEvent(OrderEvent.errorEmail);
			processPreEvent(data);
		}
	}

	@Override
	protected ProcessData processStateTransition(ProcessData sdata) throws ProcessException {
		OrderData data = (OrderData) sdata;
		log.info("Pre-event: {}", data.getEvent().toString());

		AbstractProcessor processor = this.context.getBean(data.getEvent().nextStepProcessor());
		if (processor.isAsync()) {
			processor.processAsync(data, this::processPostEvent);
		} else {
			data = (OrderData) processor.process(data);
			processPostEvent(data);
			;
		}
		return data;
	}

	@Override
	protected void initializeState(ProcessData sdata) throws OrderException {
		OrderData data = (OrderData) sdata;

		// validate state
		checkStateForReturningCustomers(data);

		if ((OrderEvent) data.getEvent() == OrderEvent.submit) {
			UUID orderId = UUID.randomUUID();
			data.setOrderId(orderId);
		}

		OrderData dbModel = orderService.save((OrderData) data);

		log.info("Initial state: {}", dbModel.getOrderId().toString());
	}

	private void checkStateForReturningCustomers(OrderData data) throws OrderException {
		// returning customers must have a state
		if (data.getOrderId() != null) {
			OrderData byId = orderService.getById(data.getOrderId());
			if (byId == null) {
				throw new OrderException("No state exists for orderId=" + data.getOrderId());
			} else if (((OrderEvent) byId.getEvent()).nextState().equals(OrderState.Completed)) {
				throw new OrderException("Order is completed for orderId=" + data.getOrderId());
			}
		}
	}
}
