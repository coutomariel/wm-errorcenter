package com.warmachine.errorcenterapi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

	private Long id;
	@NotNull(message = "Informe email")
	@Email(message = "Informe um email válido")
	private String email;
	@NotNull(message = "Informe password")
	private String password;
}
