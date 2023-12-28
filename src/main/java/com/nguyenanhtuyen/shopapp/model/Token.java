package com.nguyenanhtuyen.shopapp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tokens")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "token", nullable = false, length = 255)
	private String token;
	
	@Column(name = "token_type", nullable = false, length = 50)
	private String tokenType;
	
	@Column(name = "expiration_date")
	private LocalDateTime expirationDate; //thời gian hết hạn 
	
	private Boolean revoked; //đã huỷ?
	
	private Boolean expired; //đã hết hạn?
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
}
