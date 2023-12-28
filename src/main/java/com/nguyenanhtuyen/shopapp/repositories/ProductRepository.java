package com.nguyenanhtuyen.shopapp.repositories;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenanhtuyen.shopapp.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsByName(String name); //kiểm tra sp có tồn tại?
	
	Page<Product> findAll(Pageable pageable); //phân trang
	
}
