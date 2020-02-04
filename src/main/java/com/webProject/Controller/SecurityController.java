package com.webProject.Controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.webProject.Daojpa.UserDao;
import com.webProject.entity.Role;
import com.webProject.entity.User;

@Controller
public class SecurityController {
	@Autowired
	private UserDao userDao;
	
	@GetMapping("/registration")
	public String getPageRegistation() {
		return "registration";
	}
	
	@PostMapping("/registration")
	public String registration(User user, Model theModel) {
		
		User userFromDb = userDao.findByUsername(user.getUsername());
		
		if(userFromDb != null) {
			return "registration";
		}
		
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		userDao.save(user);
		
		return "redirect:/user";
	}
	
}
