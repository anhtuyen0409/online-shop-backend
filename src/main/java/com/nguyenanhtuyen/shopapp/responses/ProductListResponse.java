package com.nguyenanhtuyen.shopapp.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductListResponse {

	private List<ProductResponse> products;
	
	private int totalPages;
	
}
