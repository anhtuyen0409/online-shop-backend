package com.nguyenanhtuyen.shopapp.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.shopapp.dtos.ProductDTO;
import com.nguyenanhtuyen.shopapp.dtos.ProductImageDTO;
import com.nguyenanhtuyen.shopapp.exceptions.DataNotFoundException;
import com.nguyenanhtuyen.shopapp.exceptions.InvalidParamException;
import com.nguyenanhtuyen.shopapp.models.Category;
import com.nguyenanhtuyen.shopapp.models.Product;
import com.nguyenanhtuyen.shopapp.models.ProductImage;
import com.nguyenanhtuyen.shopapp.repositories.CategoryRepository;
import com.nguyenanhtuyen.shopapp.repositories.ProductImageRepository;
import com.nguyenanhtuyen.shopapp.repositories.ProductRepository;
import com.nguyenanhtuyen.shopapp.responses.ProductResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

	private final ProductRepository productRepository;

	private final CategoryRepository categoryRepository;
	
	private final ProductImageRepository productImageRepository;

	@Override
	public Product createProduct(ProductDTO productDTO) throws Exception {

		Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
				() -> new DataNotFoundException("Cannot found category with id: " + productDTO.getCategoryId()));

		// convert productDTO -> product
		Product newProduct = Product.builder()
				.name(productDTO.getName())
				.price(productDTO.getPrice())
				.thumbnail(productDTO.getThumbnail())
				.description(productDTO.getDescription())
				.category(existingCategory)
				.build();

		return productRepository.save(newProduct);
	}

	@Override
	public Product getProductById(long id) throws Exception {
		return productRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Cannot found product with id: " + id));
	}

	@Override
	public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
		// lấy danh sách sản phẩm theo trang (page) và giới hạn (limit)
		return productRepository.findAll(pageRequest).map(product -> {
			ProductResponse productResponse = ProductResponse.builder()
					.name(product.getName())
					.price(product.getPrice())
					.thumbnail(product.getThumbnail())
					.description(product.getDescription())
					.categoryId(product.getCategory().getId())
					.build();
			productResponse.setCreateAt(product.getCreateAt());
			productResponse.setUpdateAt(product.getUpdateAt());
			return productResponse;
		});
	}

	@Override
	public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
		
		Product existingProduct = getProductById(id);
		
		if(existingProduct != null) {
			//copy các thuộc tính từ DTO -> product
			Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
					() -> new DataNotFoundException("Cannot found category with id: " + productDTO.getCategoryId()));
			existingProduct.setName(productDTO.getName());
			existingProduct.setCategory(existingCategory);
			existingProduct.setPrice(productDTO.getPrice());
			existingProduct.setThumbnail(productDTO.getThumbnail());
			existingProduct.setDescription(productDTO.getDescription());
			return productRepository.save(existingProduct);
		}
		
		return null;
	}

	@Override
	public void deleteProduct(long id) {
		Optional<Product> optionalProduct = productRepository.findById(id);
		optionalProduct.ifPresent(productRepository::delete);
	}

	@Override
	public boolean existsByName(String name) {
		return productRepository.existsByName(name);
	}
	
	@Override
	public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
		
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new DataNotFoundException("Cannot found product with id: " + productImageDTO.getProductId()));
		
		//convert DTO -> ProductImage
		ProductImage newProductImage = ProductImage.builder()
				.product(existingProduct)
				.imageUrl(productImageDTO.getImageUrl())
				.build();
		
		//không cho insert quá 5 ảnh trong 1 sản phẩm
		int count = productImageRepository.findByProductId(productId).size();
		if(count > ProductImage.MAXIMUM_IMAGES_OF_PRODUCT) {
			throw new InvalidParamException("Number of images must be <= " + ProductImage.MAXIMUM_IMAGES_OF_PRODUCT);
		}
		
		return productImageRepository.save(newProductImage);
	}

}
