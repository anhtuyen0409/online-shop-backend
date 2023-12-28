package com.nguyenanhtuyen.shopapp.model;

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
@Table(name = "social_accounts")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "provider", nullable = false, length = 20)
	private String provider;

	@Column(name = "provider_id", nullable = false, length = 50)
	private String providerId;

	@Column(name = "email", nullable = false, length = 150)
	private String email;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
}
