package com.nguyenanhtuyen.shopapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.shopapp.dto.ProductDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

	// http://localhost:8088/api/v1/products?page=1&limit=10
	@GetMapping("")
	public ResponseEntity<String> getProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
		return ResponseEntity.ok("success");
	}
	
	// http://localhost:8088/api/v1/products/1
	@GetMapping("/{id}")
	public ResponseEntity<String> getProductById(@PathVariable("id") String productId) {
		return ResponseEntity.ok("product with id: " + productId);
	}
	
	@PostMapping("")
	public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult result){
		try {
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			return ResponseEntity.ok("create success!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable long id){
		return ResponseEntity.status(HttpStatus.OK).body("delete product with id = "+id+" success!");
	}
}
