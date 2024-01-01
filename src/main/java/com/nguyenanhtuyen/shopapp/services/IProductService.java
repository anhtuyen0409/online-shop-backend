package com.nguyenanhtuyen.shopapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.nguyenanhtuyen.shopapp.dtos.ProductDTO;
import com.nguyenanhtuyen.shopapp.dtos.ProductImageDTO;
import com.nguyenanhtuyen.shopapp.models.Product;
import com.nguyenanhtuyen.shopapp.models.ProductImage;
import com.nguyenanhtuyen.shopapp.responses.ProductResponse;

public interface IProductService {

	Product createProduct(ProductDTO productDTO) throws Exception;
	
	Product getProductById(long id) throws Exception;
	
	Page<ProductResponse> getAllProducts(PageRequest pageRequest);
	
	Product updateProduct(long id, ProductDTO productDTO) throws Exception;
	
	void deleteProduct(long id);
	
	boolean existsByName(String name);
	
	ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
	
}
