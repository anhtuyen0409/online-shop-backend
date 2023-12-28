package com.nguyenanhtuyen.shopapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenanhtuyen.shopapp.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}
