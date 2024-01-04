package com.nguyenanhtuyen.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nguyenanhtuyen.shopapp.models.OrderDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {

	private Long id;
	
	@JsonProperty("order_id")
	private Long orderId;
	
	@JsonProperty("product_id")
	private Long productId;
	
	private Float price;
	
	@JsonProperty("number_of_products")
	private int numberOfProducts;
	
	@JsonProperty("total_money")
	private Float totalMoney;
	
	private String color;
	
	// convert orderDetail -> orderDetailResponse
	public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
		return OrderDetailResponse.builder()
				.id(orderDetail.getId())
				.orderId(orderDetail.getOrder().getId())
				.productId(orderDetail.getProduct().getId())
				.price(orderDetail.getPrice())
				.numberOfProducts(orderDetail.getNumberOfProducts())
				.totalMoney(orderDetail.getTotalMoney())
				.color(orderDetail.getColor())
				.build();
	}
	
}
