package com.nguyenanhtuyen.shopapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.shopapp.components.LocalizationUtil;
import com.nguyenanhtuyen.shopapp.dtos.UserDTO;
import com.nguyenanhtuyen.shopapp.dtos.UserLoginDTO;
import com.nguyenanhtuyen.shopapp.models.User;
import com.nguyenanhtuyen.shopapp.responses.LoginResponse;
import com.nguyenanhtuyen.shopapp.responses.RegisterResponse;
import com.nguyenanhtuyen.shopapp.responses.UserResponse;
import com.nguyenanhtuyen.shopapp.services.IUserService;
import com.nguyenanhtuyen.shopapp.utils.MessageKeys;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
	
	private final IUserService userService;
	
	private final LocalizationUtil localizationUtil;

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> createUsers(@Valid @RequestBody UserDTO userDTO, BindingResult result) {

		try {
			// error
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(RegisterResponse.builder()
						.message(localizationUtil.getLocalizedMessage(MessageKeys.BINDING_ERROR, errorMessages))
						.build());
			}
			if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
				return ResponseEntity.badRequest().body(RegisterResponse.builder()
						.message(localizationUtil.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
						.build());
			}
			User user = userService.createUser(userDTO);
			return ResponseEntity.ok(RegisterResponse.builder()
					.message(localizationUtil.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
					.user(user)
					.build());
			// return ResponseEntity.ok("Register successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(RegisterResponse.builder()
					.message(localizationUtil.getLocalizedMessage(MessageKeys.REGISTER_FAILED, e.getMessage()))
					.build());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

		// kiểm tra thông tin đăng nhập và sinh token
		try {
			String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword(), userLoginDTO.getRoleId());
			// trả về token trong response
			return ResponseEntity.ok(LoginResponse.builder()
					.message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
					.token(token)
					.build());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(LoginResponse.builder()
					.message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
					.build());
		}
		
	}
	
	@PostMapping("/details")
	public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String token) {
		
		try {
			String extractedToken = token.substring(7); // loại bỏ "Bearer " từ chuỗi token
			User user = userService.getUserDetailsFromToken(extractedToken);
			return ResponseEntity.ok(UserResponse.fromUser(user));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
