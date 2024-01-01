package com.nguyenanhtuyen.shopapp.models;

import java.time.LocalDateTime;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "fullname", length = 100)
	private String fullName;
	
	@Column(name = "email", length = 100)
	private String email;
	
	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;
	
	@Column(name = "address", nullable = false, length = 200)
	private String address;
	
	@Column(name = "note", length = 100)
	private String note;
	
	@Column(name = "order_date")
	private LocalDateTime orderDate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "total_money")
	private Float totalMoney;
	
	@Column(name = "shipping_method", length = 100)
	private String shippingMethod;
	
	@Column(name = "shipping_address", length = 200)
	private String shippingAddress;
	
	@Column(name = "shipping_date")
	private Date shippingDate;
	
	@Column(name = "tracking_number")
	private String trackingNumber;
	
	@Column(name = "payment_method")
	private String paymentMethod;
	
	private Boolean active; //dành cho admin
	
}
