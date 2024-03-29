package com.nguyenanhtuyen.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data //toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

	@JsonProperty("product_id")
	private Long productId;
	
	@JsonProperty("quantity")
	private Integer quantity;
}
