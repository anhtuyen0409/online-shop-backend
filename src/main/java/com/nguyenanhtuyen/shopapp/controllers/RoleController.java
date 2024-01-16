package com.nguyenanhtuyen.shopapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.shopapp.models.Role;
import com.nguyenanhtuyen.shopapp.services.IRoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {

	private final IRoleService roleService;
	
	@GetMapping("")
	public ResponseEntity<?> getAllRoles() {
		List<Role> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}
	
}
