package com.webProject.Controller;


import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webProject.entity.Role;
import com.webProject.entity.User;
import com.webProject.service.UserService;

@Controller
@RequestMapping("/user")

public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping
	public String showUser(Model theModel) {
		
		theModel.addAttribute("users", userService.findAll());
		
		return "users/list-user";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/edit")
	public String editPageForUser(@RequestParam("userId") User user, Model theModel) {
		
		theModel.addAttribute("user", user);
		theModel.addAttribute("roles", Role.values());
		
		return "users/edit-page";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/edit")
	public String saveUser(@RequestParam("id") User user, 
						   @RequestParam String username,
						   @RequestParam Map<String,String> form) {
		
		userService.saveUser(user, username, form);
		
		return "redirect:/user";
	}
	
	@GetMapping("/profile")
	public String showProfile(Model theModel, @AuthenticationPrincipal User user) {
		
		theModel.addAttribute("username", user.getUsername());
		theModel.addAttribute("email", user.getEmail());
		
		return "users/profile";
	}
	
	@PostMapping("/profile/save")
	public String updateProfile(@AuthenticationPrincipal User user,
								@RequestParam String password,
								@RequestParam String email) {
		
		userService.updateprofile(user, email, password);
		
		
		return "redirect:/user/profile";
	}

}
