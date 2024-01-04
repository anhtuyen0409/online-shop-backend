package com.nguyenanhtuyen.shopapp.services;

import java.util.List;

import com.nguyenanhtuyen.shopapp.dtos.OrderDetailDTO;
import com.nguyenanhtuyen.shopapp.models.OrderDetail;

public interface IOrderDetailService {

	OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
	
	OrderDetail getOrderDetail(Long id) throws Exception;
	
	OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;
	
	void deleteOrderDetail(Long id);
	
	List<OrderDetail> findByOrderId(Long orderId);
	
}
