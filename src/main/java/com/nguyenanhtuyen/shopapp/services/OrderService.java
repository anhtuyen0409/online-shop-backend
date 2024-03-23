package com.nguyenanhtuyen.shopapp.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenanhtuyen.shopapp.dtos.OrderDTO;
import com.nguyenanhtuyen.shopapp.exceptions.DataNotFoundException;
import com.nguyenanhtuyen.shopapp.models.Order;
import com.nguyenanhtuyen.shopapp.models.OrderStatus;
import com.nguyenanhtuyen.shopapp.models.User;
import com.nguyenanhtuyen.shopapp.repositories.OrderRepository;
import com.nguyenanhtuyen.shopapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
	
	private final OrderRepository orderRepository;
	
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;

	@Override
	@Transactional
	public Order createOrder(OrderDTO orderDTO) throws Exception {
		
		// kiểm tra userId có tồn tại không?
		User user = userRepository.findById(orderDTO.getUserId())
				.orElseThrow(() -> new DataNotFoundException("Cannot found user with id: " + orderDTO.getUserId()));
		
		// convert orderDTO -> order
		// sử dụng thư viện ModelMapper
		modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId)); // ánh xạ orderDTO -> order bỏ qua trường id
		
		// cập nhật các trường của đơn hàng từ orderDTO
		Order order = new Order();
		modelMapper.map(orderDTO, order);
		order.setUser(user);
		order.setOrderDate(new Date()); // lấy thời điểm hiện tại
		order.setStatus(OrderStatus.PENDING);
		
		// kiểm tra shipping date phải >= ngày đật hàng
		LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
		if(shippingDate.isBefore(LocalDate.now())) {
			throw new DataNotFoundException("Date must be at least today!");
		}
		order.setShippingDate(shippingDate);
		order.setActive(true);
		orderRepository.save(order);
		
		return order; // trả về orderResponse
		
	}

	@Override
	public Order getOrder(Long id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
		
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Cannot found order with id: " + id));
		User existingUser = userRepository.findById(orderDTO.getUserId())
				.orElseThrow(() -> new DataNotFoundException("Cannot found user with id: " + orderDTO.getUserId()));
		
		// tạo 1 luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
		modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
		
		// cập nhật các trường của đơn hàng từ orderDTO
		modelMapper.map(orderDTO, order);
		order.setUser(existingUser);
		
		return orderRepository.save(order);
		
	}

	@Override
	@Transactional
	public void deleteOrder(Long id) {
		Order order = orderRepository.findById(id).orElse(null);
		if(order != null) {
			order.setActive(false);
			orderRepository.save(order);
		}
	}

	@Override
	public List<Order> findByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}

}
