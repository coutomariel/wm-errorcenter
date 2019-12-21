package com.warmachine.errorcenterapi.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/v1/users").permitAll()
        		.antMatchers(HttpMethod.GET, "/v1/errors").authenticated()
				.antMatchers(HttpMethod.POST, "/v1/errors").authenticated()
				.antMatchers(HttpMethod.PUT, "/v1/errors").authenticated()
				.antMatchers(HttpMethod.DELETE, "/v1/errors").authenticated();

		http.headers().frameOptions().disable();
	}

}
