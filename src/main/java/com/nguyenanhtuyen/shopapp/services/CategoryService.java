package com.nguyenanhtuyen.shopapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.shopapp.components.LocalizationUtil;
import com.nguyenanhtuyen.shopapp.dtos.CategoryDTO;
import com.nguyenanhtuyen.shopapp.models.Category;
import com.nguyenanhtuyen.shopapp.repositories.CategoryRepository;
import com.nguyenanhtuyen.shopapp.utils.MessageKeys;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
	
	private final CategoryRepository categoryRepository; //Dependency Injection
	
	private final LocalizationUtil localizationUtil;

	@Override
	public Category createCategory(CategoryDTO categoryDTO) {
		Category newCategory = Category.builder().name(categoryDTO.getName()).build();
		return categoryRepository.save(newCategory);
	}

	@Override
	public Category getCategoryById(long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException(localizationUtil.getLocalizedMessage(MessageKeys.CATEGORY_NOT_FOUND)));
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category updateCategory(long categoryId, CategoryDTO categoryDTO) {
		Category existingCategory = getCategoryById(categoryId);
		existingCategory.setName(categoryDTO.getName());
		categoryRepository.save(existingCategory);
		return existingCategory;
	}

	@Override
	public void deleteCategory(long id) {
		categoryRepository.deleteById(id);
	}

}
