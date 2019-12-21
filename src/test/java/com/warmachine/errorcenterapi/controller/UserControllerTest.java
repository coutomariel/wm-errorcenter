package com.warmachine.errorcenterapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.warmachine.errorcenterapi.ContextsLoads;
import com.warmachine.errorcenterapi.controller.error.ErrorController;
import com.warmachine.errorcenterapi.controller.user.UserController;
import com.warmachine.errorcenterapi.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmachine.errorcenterapi.dto.UserDto;
import com.warmachine.errorcenterapi.entity.User;
import com.warmachine.errorcenterapi.service.UserService;

@RunWith(SpringRunner.class)
//@WebMvcTest(ErrorController.class)
@ActiveProfiles("test")
@Import(ContextsLoads.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	private static final Long ID = 1L;
	private static final String EMAIL = "test@gmail.com";
	private static final String PASSWORD = "123456";

	
	private static final String URL = "/v1/users";

	@MockBean
	private UserServiceImpl userService;

	@Autowired
	private MockMvc mvc;
	
	@Test
	public void testSave() throws Exception {
		
		BDDMockito.given(userService.save(Mockito.any(User.class))).willReturn(getMockUser());
		
		mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayLoad(ID, EMAIL, PASSWORD))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.data.id").value(ID))
		.andExpect(jsonPath("$.data.email").value(EMAIL))
		.andExpect(jsonPath("$.data.password").doesNotExist());
	}


	public User getMockUser() {
		User user = new User();
		user.setId(ID);
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		return user;
	}

	public String getJsonPayLoad(Long id, String email, String password) 
			throws JsonProcessingException {
		UserDto userDTO = new UserDto();
		userDTO.setId(id);
		userDTO.setEmail(email);
		userDTO.setPassword(password);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(userDTO);
	}
	
}
