package com.nguyenanhtuyen.shopapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenanhtuyen.shopapp.models.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

	List<ProductImage> findByProductId(Long productId);
	
}
