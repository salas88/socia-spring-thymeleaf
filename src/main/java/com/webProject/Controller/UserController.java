package com.webProject.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webProject.Daojpa.UserDao;
import com.webProject.entity.Role;
import com.webProject.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserDao userRepo;
	
	@GetMapping
	public String showUser(Model theModel) {
		
		theModel.addAttribute("users", userRepo.findAll());
		
		return "users/list-user";
	}
	
	@GetMapping("/edit")
	public String editPageForUser(@RequestParam("userId") int userId, Model theModel) {
		
		User user = userRepo.findById(userId).get();
		
		
		theModel.addAttribute("user", user);
		theModel.addAttribute("roles", Role.values());
		
		return "users/edit-page";
	}

}
