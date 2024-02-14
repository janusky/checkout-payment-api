package checkoutpaymentapi.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import checkoutpaymentapi.exception.ResourceNotFoundException;
import checkoutpaymentapi.model.OrderData;
import checkoutpaymentapi.model.OrderEvent;
import checkoutpaymentapi.repository.OrderDataRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDataRepository orderDataRepository;

	@Transactional
	public OrderData save(OrderData entity) {
		if (entity != null) {			
			log.debug("Save OrderData id{} and event {}", entity.getOrderId(), ((OrderEvent) entity.getEvent()).name());
		}
		return orderDataRepository.save(entity);
	}

	@Transactional
	public OrderData getById(UUID id) {
		OrderData orderData = orderDataRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
		return orderData;
	}
}