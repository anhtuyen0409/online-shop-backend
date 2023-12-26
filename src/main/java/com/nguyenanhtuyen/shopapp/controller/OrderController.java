package com.nguyenanhtuyen.shopapp.controller;

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

import com.nguyenanhtuyen.shopapp.dto.OrderDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

	@PostMapping("")
	public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult result) {
		try {
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			return ResponseEntity.ok("create order success!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{user_id}") // thêm biên đường dẫn "user_id"
	// GET http://localhost:8088/api/v1/orders/2
	public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId) {
		try {
			return ResponseEntity.ok("get list orders by user_id");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	// PUT http://localhost:8088/api/v1/orders/2
	public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
		return ResponseEntity.ok("update order has id = " + id + " success!");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id) {
		return ResponseEntity.ok("delete order successfully!");
	}
	
}
