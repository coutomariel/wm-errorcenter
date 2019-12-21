package com.warmachine.errorcenterapi.dto;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.warmachine.errorcenterapi.entity.User;

public class UserDetailDto implements UserDetails{
	
	private String login;
	private String senha;

	public UserDetailDto(Optional<User> user) {
    	this.login = user.get().getEmail();
    	this.senha = user.get().getPassword();
    }

	   @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return null;
	    }

	    @Override
	    public String getPassword() {
	        return senha;
	    }

	    @Override
	    public String getUsername() {
	        return login;
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }
}
