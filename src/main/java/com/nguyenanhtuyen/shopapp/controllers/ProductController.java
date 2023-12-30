package com.nguyenanhtuyen.shopapp.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nguyenanhtuyen.shopapp.dtos.ProductDTO;
import com.nguyenanhtuyen.shopapp.dtos.ProductImageDTO;
import com.nguyenanhtuyen.shopapp.models.Product;
import com.nguyenanhtuyen.shopapp.models.ProductImage;
import com.nguyenanhtuyen.shopapp.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;

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
						.body("You can upload maximum " + ProductImage.MAXIMUM_IMAGES_OF_PRODUCT + " images");
			}
			List<ProductImage> productImages = new ArrayList<ProductImage>();

			for (MultipartFile file : files) {

				if (file.getSize() == 0) {
					continue;
				}

				// kiểm tra kích thước file và định dạng
				if (file.getSize() > 10 * 1024 * 1024) { // kich thuoc > 10MB
					return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
							.body("File is too large! Maximum size is 10MB.");
				}

				// kiểm tra định dạng file
				String contentType = file.getContentType();
				if (contentType == null || !contentType.startsWith("image/")) {
					return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image.");
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

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable long id) {
		return ResponseEntity.status(HttpStatus.OK).body("delete product with id = " + id + " success!");
	}
}
