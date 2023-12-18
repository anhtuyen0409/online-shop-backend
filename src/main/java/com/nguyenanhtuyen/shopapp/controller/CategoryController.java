package com.nguyenanhtuyen.shopapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.shopapp.dto.CategoryDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/categories")
//@Validated
public class CategoryController {

	// http://localhost:8088/api/v1/categories?page=1&limit=10
	@GetMapping("")
	public ResponseEntity<String> getAllCategories(@RequestParam("page") int page, @RequestParam("limit") int limit) {
		return ResponseEntity.ok(String.format("get all categories, page = %d, limit = %d", page, limit));
	}

	@PostMapping("")
	// tham so truyen vao la 1 object => data transfer object (dto) = request object
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
		if (result.hasErrors()) {
			List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
			return ResponseEntity.badRequest().body(errorMessages);
		}
		return ResponseEntity.ok("create success " + categoryDTO);
	}

	// http://localhost:8088/api/v1/categories?id=1
	@PutMapping("")
	public ResponseEntity<String> updateCategory(@RequestParam("id") Long id) {
		return ResponseEntity.ok("update category with id = " + id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		return ResponseEntity.ok("delete category with id = " + id);
	}
}
