 package com.webProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.webProject.entity.User;
import com.webProject.service.UserService;

@Controller
public class SecurityController {
	@Autowired
	private UserService theUserService;
	
	@GetMapping("/registration")
	public String getPageRegistation() {
		return "registration";
	}
	
	@PostMapping("/registration")
	public String registration(User user, Model theModel) {
	
		if(!theUserService.addUser(user)) {
			return "registration";
		}	
		return "redirect:/user";
	}
	
	@GetMapping("/activate/{code}")
	public String activatePage(Model theModel, @PathVariable String code) {
		
		boolean isActivate = theUserService.activateUsr(code);
		
		if(isActivate) 
			theModel.addAttribute("message", "User successfully activated");
		else
			theModel.addAttribute("message", "Activation code is not found");
			
		
		return "/login";
	}
}
