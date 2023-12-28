package com.nguyenanhtuyen.shopapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenanhtuyen.shopapp.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
