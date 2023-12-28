package com.nguyenanhtuyen.shopapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenanhtuyen.shopapp.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId); //tìm danh sách đơn hàng của 1 user
	
}
