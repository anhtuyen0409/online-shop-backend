package com.nguyenanhtuyen.shopapp.services;

import java.util.List;

import com.nguyenanhtuyen.shopapp.dtos.OrderDTO;
import com.nguyenanhtuyen.shopapp.exceptions.DataNotFoundException;
import com.nguyenanhtuyen.shopapp.models.Order;

public interface IOrderService {

	Order createOrder(OrderDTO orderDTO) throws Exception;
	
	Order getOrder(Long id);
	
	Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
	
	void deleteOrder(Long id);
	
	List<Order> findByUserId(Long userId);
	
}
