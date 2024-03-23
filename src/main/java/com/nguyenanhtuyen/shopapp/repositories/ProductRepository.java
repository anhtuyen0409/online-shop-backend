package com.nguyenanhtuyen.shopapp.repositories;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nguyenanhtuyen.shopapp.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsByName(String name); //kiểm tra sp có tồn tại?
	
	Page<Product> findAll(Pageable pageable); //phân trang
	
	@Query("SELECT p FROM Product p WHERE " + 
	       "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId)" +
	       "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
	Page<Product> searchProducts(
			@Param("keyword") String keyword, 
			@Param("categoryId") Long categoryId,
			Pageable pageable);
	
}
