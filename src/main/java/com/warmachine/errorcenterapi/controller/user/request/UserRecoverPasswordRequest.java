package com.warmachine.errorcenterapi.controller.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRecoverPasswordRequest {
	private String email;
}
