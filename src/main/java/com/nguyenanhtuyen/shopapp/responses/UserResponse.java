package com.nguyenanhtuyen.shopapp.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nguyenanhtuyen.shopapp.models.Role;
import com.nguyenanhtuyen.shopapp.models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
	
	private Long id;

	@JsonProperty("fullname")
	private String fullName;

	@JsonProperty("phone_number")
	private String phoneNumber;

	@JsonProperty("address")
	private String address;

	@JsonProperty("is_active")
	private Boolean active;

	@JsonProperty("date_of_birth")
	private Date dateOfBirth;

	@JsonProperty("facebook_account_id")
	private Integer facebookAccountId;

	@JsonProperty("google_account_id")
	private Integer googleAccountId;

	private Role role;
	
	// convert user -> userResponse
	public static UserResponse fromUser(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.fullName(user.getFullName())
				.phoneNumber(user.getPhoneNumber())
				.address(user.getAddress())
				.active(user.getActive())
				.dateOfBirth(user.getDateOfBirth())
				.facebookAccountId(user.getFacebookAccountId())
				.googleAccountId(user.getGoogleAccountId())
				.role(user.getRole())
				.build();
		}

}
