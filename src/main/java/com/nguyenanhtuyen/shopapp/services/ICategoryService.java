package com.nguyenanhtuyen.shopapp.services;

import java.util.List;

import com.nguyenanhtuyen.shopapp.dtos.CategoryDTO;
import com.nguyenanhtuyen.shopapp.models.Category;

public interface ICategoryService {

	Category createCategory(CategoryDTO categoryDTO);

	Category getCategoryById(long id);

	List<Category> getAllCategories();

	Category updateCategory(long categoryId, CategoryDTO categoryDTO);

	void deleteCategory(long id);

}
