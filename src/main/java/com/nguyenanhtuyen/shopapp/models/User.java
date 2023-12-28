package com.nguyenanhtuyen.shopapp.models;

import java.util.Date;

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
@Table(name = "users")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fullname", length = 100)
	private String fullName;

	@Column(name = "phone_number", nullable = false, length = 10)
	private String phoneNumber;

	@Column(name = "address", length = 200)
	private String address;

	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@Column(name = "is_active")
	private Boolean active;

	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "facebook_account_id")
	private Integer facebookAccountId;

	@Column(name = "google_account_id")
	private Integer googleAccountId;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
}
