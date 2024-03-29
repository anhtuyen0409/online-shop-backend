package com.nguyenanhtuyen.shopapp.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.javafaker.Faker;
import com.nguyenanhtuyen.shopapp.components.LocalizationUtil;
import com.nguyenanhtuyen.shopapp.dtos.ProductDTO;
import com.nguyenanhtuyen.shopapp.dtos.ProductImageDTO;
import com.nguyenanhtuyen.shopapp.models.Product;
import com.nguyenanhtuyen.shopapp.models.ProductImage;
import com.nguyenanhtuyen.shopapp.responses.ImageMessageResponse;
import com.nguyenanhtuyen.shopapp.responses.ProductListResponse;
import com.nguyenanhtuyen.shopapp.responses.ProductMessageResponse;
import com.nguyenanhtuyen.shopapp.responses.ProductResponse;
import com.nguyenanhtuyen.shopapp.services.IProductService;
import com.nguyenanhtuyen.shopapp.utils.MessageKeys;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final IProductService productService;
	
	private final LocalizationUtil localizationUtil;

	// http://localhost:8088/api/v1/products?page=1&limit=10
	@GetMapping("")
	public ResponseEntity<ProductListResponse> getProducts(
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int limit) {
		
		// tạo Pageable từ thông tin trang và giới hạn
		PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
		
		Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest);
		
		// lấy tổng số trang
		int totalPages = productPage.getTotalPages();
		List<ProductResponse> products = productPage.getContent();
		
		return ResponseEntity.ok(ProductListResponse.builder()
				.products(products)
				.totalPages(totalPages)
				.build());
	}

	// http://localhost:8088/api/v1/products/1
//	@GetMapping("/{id}")
//	public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
//		try {
//			Product existingProduct = productService.getProductById(productId);
//			return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct)); //trả về ProductResponse
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}
	
	@GetMapping("/by-ids")
	public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
		try {
			// tách chuỗi ids thành 1 mảng số nguyên
			List<Long> productIds = Arrays.stream(ids.split(","))
					.map(Long::parseLong)
					.collect(Collectors.toList());
			List<Product> products = productService.findProductsByIds(productIds);
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// POST http://localhost:8088/api/v1/products
	@PostMapping("")
	public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult result) {
		try {
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}
			Product newProduct = productService.createProduct(productDTO);
			return ResponseEntity.ok(newProduct);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,
			@ModelAttribute("files") List<MultipartFile> files) {
		
		try {
			Product existingProduct = productService.getProductById(productId);
			files = files == null ? new ArrayList<MultipartFile>() : files;
			if(files.size() > ProductImage.MAXIMUM_IMAGES_OF_PRODUCT) {
				return ResponseEntity.badRequest()
						.body(ImageMessageResponse
								.builder()
								.message(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX_5))
								.build());
			}
			List<ProductImage> productImages = new ArrayList<ProductImage>();

			for (MultipartFile file : files) {

				if (file.getSize() == 0) {
					continue;
				}

				// kiểm tra kích thước file và định dạng
				if (file.getSize() > 10 * 1024 * 1024) { // kich thuoc > 10MB
					return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
							.body(ImageMessageResponse
									.builder()
									.message(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_LARGE))
									.build());
				}

				// kiểm tra định dạng file
				String contentType = file.getContentType();
				if (contentType == null || !contentType.startsWith("image/")) {
					return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ImageMessageResponse
							.builder()
							.message(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE))
							.build());
				}

				// lưu file và cập nhật thumbnail trong DTO
				String filename = storeFile(file);
				
				// lưu vào đối tượng product trong db 
				ProductImage productImage = productService.createProductImage(
						existingProduct.getId(),
						ProductImageDTO.builder().imageUrl(filename).build());
				
				productImages.add(productImage);
			}
			
			return ResponseEntity.ok().body(productImages);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@GetMapping("/images/{imageName}")
	public ResponseEntity<?> viewImage(@PathVariable String imageName) {
		try {
			Path imagePath = Paths.get("uploads/" + imageName);
			UrlResource resource = new UrlResource(imagePath.toUri());
			
			if(resource.exists()) {
				return ResponseEntity.ok()
						.contentType(MediaType.IMAGE_JPEG)
						.body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	private String storeFile(MultipartFile file) throws IOException {
		
		if(!isImageFile(file) || file.getOriginalFilename() == null) {
			throw new IOException("Invalid image format");
		}
		// get file
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

		// thêm UUIDD vào trước tên file để chắc chắn rằng tên file là duy nhất
		String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;

		// đường dẫn thư mục lưu file
		Path uploadDir = Paths.get("uploads");

		// kiểm tra và tạo thư mục nếu không tồn tại
		if (!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}

		// đường dẫn đầy đủ đến file
		Path destination = Paths.get(uploadDir.toString(), uniqueFilename);

		// sao chép file vào thư mục đích
		Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

		return uniqueFilename;
	}
	
	private boolean isImageFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && contentType.startsWith("image/");
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestBody ProductDTO productDTO) {
		try {
			Product updateProduct = productService.updateProduct(id, productDTO);
			return ResponseEntity.ok(ProductResponse.fromProduct(updateProduct)); //trả về ResponseProduct
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable long id) {
		try {
			productService.deleteProduct(id);
			// return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", id));
			return ResponseEntity.ok(ProductMessageResponse.builder()
					.message(localizationUtil.getLocalizedMessage(MessageKeys.DELETE_PRODUCT_SUCCESSFULLY, id))
					.build());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// fake data
	//@PostMapping("/generateFakeProducts") 
	private ResponseEntity<String> generateFakeProducts() {
		Faker faker = new Faker();
		for(int i=0; i<1_000; i++) {
			String productName = faker.commerce().productName();
			if(productService.existsByName(productName)) {
				continue;
			}
			ProductDTO productDTO = ProductDTO.builder()
					.name(productName)
					.price((float)faker.number().numberBetween(10, 90_000_000))
					.description(faker.lorem().sentence())
					.thumbnail("")
					.categoryId((long)faker.number().numberBetween(2, 5))
					.build();
			try {
				productService.createProduct(productDTO);
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		return ResponseEntity.ok("Fake products created successfully");
	}
	
}
