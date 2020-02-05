package com.webProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webProject.Daojpa.MessageDao;
import com.webProject.Daojpa.UserDao;
import com.webProject.entity.Message;
import com.webProject.entity.User;

@Controller

public class MainController {
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private UserDao userDao;
	
	
	@GetMapping("/main")
	public String show(@RequestParam(required = false, defaultValue = "") String filter,
						Model theModel) {
		
		Iterable<Message> messages = messageDao.findAll();
		
		if( !filter.isEmpty() && filter != null ) 
			messages = messageDao.findByTag(filter);	
		else 
			messages = messageDao.findAll();		
		
		theModel.addAttribute("messages", messages);
		theModel.addAttribute("filter", filter);
		
		return "main-page";
	}
	
	@PostMapping("/main")
	public String addNewMessage(@RequestParam String title, @RequestParam String tag,
								 Model theModel) {
		
		Message message = new Message(title, tag);
		messageDao.save(message);
		
		Iterable<Message> messages = messageDao.findAll();
		
		theModel.addAttribute("messages", messages);
	
		return "main-page";
	}
	
	
	
	@GetMapping("/login")
	public String homePage() {
		
		return "login";
		
	}
	
	@PostMapping("/login")
	public String mainPage() {
		
		return "redirect:/main";
		
	}
	
	@GetMapping()
	public String showPage() {
		
		
		return "home-page";
	}
		
	

}
