package com.nguyenanhtuyen.shopapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductDTO productDTO,
//			@RequestPart("file") MultipartFile file, 
			BindingResult result) {
		try {
			if (result.hasErrors()) {
				List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
						.toList();
				return ResponseEntity.badRequest().body(errorMessages);
			}

			List<MultipartFile> files = productDTO.getFiles();
			files = files == null ? new ArrayList<MultipartFile>() : files;

			for (MultipartFile file : files) {

				if (file.getSize() == 0) {
					continue;
				}

				// kiem tra kich thuoc file va dinh dang
				if (file.getSize() > 10 * 1024 * 1024) { // kich thuoc > 10MB
					return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
							.body("File is too large! Maximum size is 10MB.");
				}

				// kiem tra dinh dang file
				String contentType = file.getContentType();
				if (contentType == null || !contentType.startsWith("image/")) {
					return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image.");
				}

				// luu file va cap nhat thubnail trong DTO
				String filename = storeFile(file);

			}

			return ResponseEntity.ok("create success!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private String storeFile(MultipartFile file) throws IOException {
		// get file
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		// them UUIDD vao truoc ten file de chac chan rang ten file la duy nhat
		String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;

		// duong dan thu muc luu file
		Path uploadDir = Paths.get("uploads");

		// kiem tra va tao thu muc neu khong ton tai
		if (!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}

		// duong dan day du den file
		Path destination = Paths.get(uploadDir.toString(), uniqueFilename);

		// sao chep file vao thu muc dich
		Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

		return uniqueFilename;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable long id) {
		return ResponseEntity.status(HttpStatus.OK).body("delete product with id = " + id + " success!");
	}
}
