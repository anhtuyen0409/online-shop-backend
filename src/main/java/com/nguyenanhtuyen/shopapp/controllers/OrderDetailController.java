package com.nguyenanhtuyen.shopapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.shopapp.dtos.OrderDetailDTO;
import com.nguyenanhtuyen.shopapp.models.OrderDetail;
import com.nguyenanhtuyen.shopapp.responses.OrderDetailResponse;
import com.nguyenanhtuyen.shopapp.services.IOrderDetailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

	private final IOrderDetailService orderDetailService;
	
	@PostMapping
	public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
		try {
			OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
			return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail)); // trả về orderDetailResponse
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
		try {
			OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
			return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail)); // trả về orderDetailResponse
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
		try {
			List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
			List<OrderDetailResponse> orderDetailResponses = orderDetails.stream().map(OrderDetailResponse::fromOrderDetail)
					.toList();
			return ResponseEntity.ok(orderDetailResponses); // trả về list orderDetailResponse
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
			@RequestBody OrderDetailDTO orderDetailDTO) {
		try {
			OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
			return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail)); // trả về orderDetailResponse
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
		try {
			orderDetailService.deleteOrderDetail(id);
			return ResponseEntity.ok(String.format("Order detail with id = %d deleted successfully", id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
