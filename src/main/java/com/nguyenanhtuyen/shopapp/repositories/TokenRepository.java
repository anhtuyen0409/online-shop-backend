package com.nguyenanhtuyen.shopapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenanhtuyen.shopapp.models.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

}
