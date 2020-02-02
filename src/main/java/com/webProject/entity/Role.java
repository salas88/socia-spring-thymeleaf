package com.webProject.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
	
	USER;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return name();
	}

}
