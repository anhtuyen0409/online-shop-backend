package com.nguyenanhtuyen.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nguyenanhtuyen.shopapp.models.Product;

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
public class ProductResponse extends BaseResponse {

	private String name;

	private Float price;

	private String thumbnail;

	private String description;

	@JsonProperty("category_id")
	private Long categoryId;

	// convert product -> ProductResponse
	public static ProductResponse fromProduct(Product product) {
		ProductResponse productResponse = ProductResponse.builder()
				.name(product.getName())
				.price(product.getPrice())
				.thumbnail(product.getThumbnail())
				.description(product.getDescription())
				.categoryId(product.getCategory().getId())
				.build();
		productResponse.setCreateAt(product.getCreateAt());
		productResponse.setUpdateAt(product.getUpdateAt());
		return productResponse;
	}
	
}
