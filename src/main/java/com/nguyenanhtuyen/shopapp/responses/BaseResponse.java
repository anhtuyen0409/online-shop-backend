package com.nguyenanhtuyen.shopapp.responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.MappedSuperclass;
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
public class BaseResponse {

	@JsonProperty("create_at")
	private LocalDateTime createAt;

	@JsonProperty("update_at")
	private LocalDateTime updateAt;

}
