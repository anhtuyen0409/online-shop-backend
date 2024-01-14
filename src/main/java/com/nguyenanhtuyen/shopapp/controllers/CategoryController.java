package com.nguyenanhtuyen.shopapp.controllers;

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

import com.nguyenanhtuyen.shopapp.components.LocalizationUtil;
import com.nguyenanhtuyen.shopapp.dtos.CategoryDTO;
import com.nguyenanhtuyen.shopapp.models.Category;
import com.nguyenanhtuyen.shopapp.responses.CategoryMessageResponse;
import com.nguyenanhtuyen.shopapp.services.ICategoryService;
import com.nguyenanhtuyen.shopapp.utils.MessageKeys;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
@RequiredArgsConstructor
public class CategoryController {
	
	private final ICategoryService categoryService; //Dependency Injection
	
	private final LocalizationUtil localizationUtil;

	// http://localhost:8088/api/v1/categories?page=1&limit=10
	@GetMapping("")
	public ResponseEntity<List<Category>> getAllCategories(@RequestParam("page") int page, @RequestParam("limit") int limit) {
		List<Category> categories = categoryService.getAllCategories();
		return ResponseEntity.ok(categories);
	}

	@PostMapping("")
	// tham số truyền vào là 1 object => data transfer object (dto) = request object
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
		if (result.hasErrors()) {
			List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
			return ResponseEntity.badRequest().body(errorMessages);
		}
		categoryService.createCategory(categoryDTO);
		return ResponseEntity.ok(CategoryMessageResponse.builder()
				.message(localizationUtil.getLocalizedMessage(MessageKeys.CREATE_CATEGORY_SUCCESSFULLY))
				.build());
	}

	// http://localhost:8088/api/v1/categories?id=1
	@PutMapping("")
	public ResponseEntity<CategoryMessageResponse> updateCategory(@RequestParam("id") Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
		categoryService.updateCategory(id, categoryDTO);
		return ResponseEntity.ok(CategoryMessageResponse.builder()
				.message(localizationUtil.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY))
				.build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CategoryMessageResponse> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok(CategoryMessageResponse.builder()
				.message(localizationUtil.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY, id))
				.build());
	}
}
