package com.nguyenanhtuyen.shopapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.shopapp.dto.UserDTO;
import com.nguyenanhtuyen.shopapp.dto.UserLoginDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

	@PostMapping("/register")
	public ResponseEntity<?> createUsers(@Valid @RequestBody UserDTO userDTO, BindingResult result) {

		try {
			// error
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
				return ResponseEntity.badRequest().body("Password does not match!");
			}
			return ResponseEntity.ok("Register successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

		// kiểm tra thông tin đăng nhập và sinh token
		// trả về token trong response
		return ResponseEntity.ok("some token");
	}
}
