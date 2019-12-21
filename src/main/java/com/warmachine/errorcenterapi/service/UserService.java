package com.warmachine.errorcenterapi.service;

import java.util.Optional;

import com.warmachine.errorcenterapi.controller.user.request.UserLoginRequest;
import com.warmachine.errorcenterapi.controller.user.request.UserRecoverPasswordRequest;
import com.warmachine.errorcenterapi.controller.user.request.UserRegisterRequest;
import com.warmachine.errorcenterapi.entity.User;

import lombok.NonNull;
import org.springframework.stereotype.Service;

public interface UserService {
	
	User save(User user);

	Optional<User> findByEmail(String email);

	//Métodos já existentes
	String login(@NonNull UserLoginRequest userLoginRequest);
	String register(@NonNull UserRegisterRequest userRegisterRequest);
	void recoverPassword(@NonNull UserRecoverPasswordRequest userRecoverPasswordRequest);
	
		
}
