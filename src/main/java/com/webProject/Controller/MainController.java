package com.webProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webProject.Daojpa.MessageDao;
import com.webProject.entity.Message;

@Controller
@RequestMapping("/")
public class MainController {
	
	@Autowired
	private MessageDao messageDao;
	
	
	@GetMapping("/main")
	public String show(Model theModel) {
		
		Iterable<Message> messages = messageDao.findAll();
		
		theModel.addAttribute("messages", messages);
		
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
	
	@GetMapping("filter")
	public String filter(@RequestParam String filter, Model theModel) {
		
		Iterable<Message> message = messageDao.findByTag(filter);
		
		theModel.addAttribute("messages", message);
		
		
		return "main-page";
	}
	
	@GetMapping
	public String homePage() {
		
		return "home-page";
		
	}
		
	

}
