package com.nguyenanhtuyen.shopapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.shopapp.dtos.OrderDTO;
import com.nguyenanhtuyen.shopapp.models.Order;
import com.nguyenanhtuyen.shopapp.services.IOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

	private final IOrderService orderService;
	
	@PostMapping("")
	public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult result) {
		try {
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			Order order = orderService.createOrder(orderDTO);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/user/{user_id}") // thêm biên đường dẫn "user_id"
	// GET http://localhost:8088/api/v1/orders/user/2
	public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId) {
		try {
			List<Order> orders = orderService.findByUserId(userId);
			return ResponseEntity.ok(orders);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}") 
	// GET http://localhost:8088/api/v1/orders/2
	public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId) {
		try {
			Order existingOrder = orderService.getOrder(orderId);
			return ResponseEntity.ok(existingOrder);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	// PUT http://localhost:8088/api/v1/orders/2
	public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
		try {
			Order order = orderService.updateOrder(id, orderDTO);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id) {
		orderService.deleteOrder(id);
		return ResponseEntity.ok("delete order successfully!");
	}
	
}
