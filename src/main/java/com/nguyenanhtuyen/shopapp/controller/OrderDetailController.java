package com.nguyenanhtuyen.shopapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.shopapp.dto.OrderDetailDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

	@PostMapping
	public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
		return ResponseEntity.ok("create order detail success!");
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
		return ResponseEntity.ok("Get order detail with id = " + id + " success!");
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
		return ResponseEntity.ok("get list order detail by order's id = " + orderId);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
			@RequestBody OrderDetailDTO newOrderDetailData) {
		return ResponseEntity
				.ok("update order detail with id = " + id + ", new order detail data: " + newOrderDetailData);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
		return ResponseEntity.noContent().build();
	}
}
