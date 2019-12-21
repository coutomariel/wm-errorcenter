package com.warmachine.errorcenterapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warmachine.errorcenterapi.controller.user.request.UserLoginRequest;
import com.warmachine.errorcenterapi.controller.user.request.UserRecoverPasswordRequest;
import com.warmachine.errorcenterapi.controller.user.request.UserRegisterRequest;
import com.warmachine.errorcenterapi.entity.User;
import com.warmachine.errorcenterapi.repository.UserRepository;
import com.warmachine.errorcenterapi.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmailEquals(email);
	}


	public String login(UserLoginRequest userLoginRequest) {
		return "1234";
	}

	public String register(UserRegisterRequest userRegisterRequest) {
		return "1234";
	}

	public void recoverPassword(UserRecoverPasswordRequest userRecoverPasswordRequest) {}



}
