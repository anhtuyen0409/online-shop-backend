package com.nguyenanhtuyen.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

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

}
