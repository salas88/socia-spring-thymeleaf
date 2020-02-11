package com.webProject.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
		
		sendMessage(user);
		
		return true;
	}

	private void sendMessage(User user) {
		if(!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
					"Hello %s! \n"
					+ "Welcome to App. Please visit next link: http://localhost:8080/activate/%s",
							user.getUsername(),
							user.getActivationCode());
			
			theMailSender.send(user.getEmail(), "Activation Code", message);
		}
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

	public List<User> findAll() {
		
		return userDao.findAll();
	}

	public void saveUser(User user, String username, Map<String, String> form) {
		user.setUsername(username);
		
		Set<String> roles = Arrays.stream(Role.values())
										.map(Role::name)
										.collect(Collectors.toSet());
		
		user.getRoles().clear();
		
		for(String key  : form.keySet()) {
			if(roles.contains(key)) 
				user.getRoles().add(Role.valueOf(key));
		}
		
		userDao.save(user);
		
	}

	public void updateprofile(User user, String email, String password) {
		
		String userEmail = user.getEmail();
		
		boolean isEmailChange = (email != null && !email.equals(userEmail) 
				|| (userEmail != null && !userEmail.equals(email)));
		
		if(isEmailChange) {
			user.setEmail(email);
			
			if(!StringUtils.isEmpty(email)) {
				user.setActivationCode(UUID.randomUUID().toString());
			}
		}
		if(!StringUtils.isEmpty(password)) {
			user.setPassword(password);
	}
		userDao.save(user);
		
		if(isEmailChange) {
			sendMessage(user);
		}
	}
}
