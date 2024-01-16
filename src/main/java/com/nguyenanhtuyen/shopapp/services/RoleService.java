package com.nguyenanhtuyen.shopapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.shopapp.models.Role;
import com.nguyenanhtuyen.shopapp.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
	
	private final RoleRepository roleRepository;

	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

}
