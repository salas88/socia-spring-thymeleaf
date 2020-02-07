package com.webProject.service;

import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.webProject.Daojpa.UserDao;
import com.webProject.entity.Role;
import com.webProject.entity.User;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MailSender theMailSender;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		return user;
	}
	
	public boolean addUser(User user) {
		User userFromDb = userDao.findByUsername(user.getUsername());
		
		if(userFromDb != null) {
			return false;}
		
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		user.setActivationCode(UUID.randomUUID().toString());
		userDao.save(user); 
		
		if(!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
					"Hello %s! \n"
					+ "Welcome to App. Please visit next link: http://localhost:8080/activate/%s",
							user.getUsername(),
							user.getActivationCode());
			
			theMailSender.send(user.getEmail(), "Activation Code", message);
		}
		
		return true;
	}

	public boolean activateUsr(String code) {
		User user = userDao.findByActivationCode(code);
		
		if(user == null) {
			return false;
		}
		
		user.setActivationCode(null);
		userDao.save(user);
		return true;
	}

}
