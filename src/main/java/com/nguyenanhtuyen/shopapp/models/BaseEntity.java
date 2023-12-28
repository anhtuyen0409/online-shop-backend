package com.nguyenanhtuyen.shopapp.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

	@Column(name = "create_at")
	private LocalDateTime createAt;
	
	@Column(name = "update_at")
	private LocalDateTime updateAt;
	
	@PrePersist
	protected void onCreate() {
		createAt = LocalDateTime.now();
		updateAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updateAt = LocalDateTime.now();
	}
	
}
