package com.nguyenanhtuyen.shopapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenanhtuyen.shopapp.models.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}
