package com.nguyenanhtuyen.shopapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.shopapp.dtos.OrderDetailDTO;
import com.nguyenanhtuyen.shopapp.exceptions.DataNotFoundException;
import com.nguyenanhtuyen.shopapp.models.Order;
import com.nguyenanhtuyen.shopapp.models.OrderDetail;
import com.nguyenanhtuyen.shopapp.models.Product;
import com.nguyenanhtuyen.shopapp.repositories.OrderDetailRepository;
import com.nguyenanhtuyen.shopapp.repositories.OrderRepository;
import com.nguyenanhtuyen.shopapp.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
	
	private final OrderRepository orderRepository;
	
	private final OrderDetailRepository orderDetailRepository;
	
	private final ProductRepository productRepository;

	@Override
	public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
		
		// kiểm tra orderId có tồn tại không?
		Order order = orderRepository.findById(orderDetailDTO.getOrderId())
				.orElseThrow(() -> new DataNotFoundException("Cannot found order with id: " + orderDetailDTO.getOrderId()));
		
		// kiểm tra product
		Product product = productRepository.findById(orderDetailDTO.getProductId())
				.orElseThrow(() -> new DataNotFoundException("Cannot found product with id: " + orderDetailDTO.getProductId()));
		
		OrderDetail orderDetail = OrderDetail.builder()
				.order(order)
				.product(product)
				.price(orderDetailDTO.getPrice())
				.numberOfProducts(orderDetailDTO.getNumberOfProducts())
				.totalMoney(orderDetailDTO.getTotalMoney())
				.color(orderDetailDTO.getColor())
				.build();
		
		//lưu vào db
		return orderDetailRepository.save(orderDetail);
		
	}

	@Override
	public OrderDetail getOrderDetail(Long id) throws Exception {
		return orderDetailRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Cannot found order detail with id: " + id));
	}

	@Override
	public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
		
		// kiểm tra orderdetail có tồn tại không?
		OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Cannot found order detail with id: " + id));
		
		// kiểm tra order
		Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
				.orElseThrow(() -> new DataNotFoundException("Cannot found order with id: " + orderDetailDTO.getOrderId()));
		
		// kiểm tra product
		Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
				.orElseThrow(() -> new DataNotFoundException("Cannot found product with id: " + orderDetailDTO.getProductId()));
		
		// tiến hành update
		existingOrderDetail.setPrice(orderDetailDTO.getPrice());
		existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
		existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
		existingOrderDetail.setColor(orderDetailDTO.getColor());
		existingOrderDetail.setOrder(existingOrder); // trường hợp đổi đơn hàng khác
		existingOrderDetail.setProduct(existingProduct); //trường hợp đổi sản phẩm khác
		
		return orderDetailRepository.save(existingOrderDetail);
		
	}

	@Override
	public void deleteOrderDetail(Long id) {
		orderDetailRepository.deleteById(id);
	}

	@Override
	public List<OrderDetail> findByOrderId(Long orderId) {
		return orderDetailRepository.findByOrderId(orderId);
	}

}
