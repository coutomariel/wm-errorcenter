package com.warmachine.errorcenterapi.controller.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warmachine.errorcenterapi.controller.user.request.UserLoginRequest;
import com.warmachine.errorcenterapi.dto.UserDto;
import com.warmachine.errorcenterapi.entity.User;
import com.warmachine.errorcenterapi.response.Response;
import com.warmachine.errorcenterapi.service.UserService;
import com.warmachine.errorcenterapi.util.Bcrypt;

import io.swagger.annotations.ApiOperation;
import lombok.NonNull;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	
	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Operacao que realiza a criacao de um novo usuario.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<UserDto>> create(@Valid @RequestBody UserDto dto, BindingResult result){
		Response<UserDto> response = new Response<>();
		if(result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		User user = userService.save(this.convertDtoToEntity(dto));
		response.setData(this.convertEntityToDto(user));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	
	@PostMapping(value = "/login")
	@ApiOperation(value = "Operacao que realiza o login e retorna o token do usuario.")
	public ResponseEntity<Object> login(@RequestBody @NonNull UserLoginRequest userLoginRequest) {
		String token = userService.login(userLoginRequest);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
		headers.setBearerAuth(token);

		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
	
	private User convertDtoToEntity(UserDto dto) {
		User user = new User();
		user.setId(dto.getId());
		user.setEmail(dto.getEmail());
		user.setPassword(Bcrypt.getHash(dto.getPassword()));
		return user;
	}
	

	private UserDto convertEntityToDto(User user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		return dto;
	}
	

}
