package com.webProject.Daojpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webProject.entity.User;


public interface UserDao extends JpaRepository<User, Integer>{
	
	User findByUsername(String username);

	User findByActivationCode(String code);

}
