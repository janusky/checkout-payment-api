package checkoutpaymentapi.service;

import java.util.UUID;

import checkoutpaymentapi.model.OrderData;

public interface OrderService {
	 public OrderData save(OrderData entity);
	 
	 public OrderData getById(UUID id);
}
