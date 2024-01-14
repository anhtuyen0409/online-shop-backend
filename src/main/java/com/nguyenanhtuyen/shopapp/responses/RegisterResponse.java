package com.nguyenanhtuyen.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nguyenanhtuyen.shopapp.models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

	@JsonProperty("message")
	private String message;
	
	@JsonProperty("user")
	private User user;
	
}
