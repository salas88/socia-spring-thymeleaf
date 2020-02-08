package com.webProject.Controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Value("${upload.path}")
	private String uploadPath;
	
	
	@GetMapping("/main")
	public String show(@RequestParam(required = false, defaultValue = "") String filter,
						Model theModel, @AuthenticationPrincipal User user) {
		
		Iterable<Message> messages = messageDao.findAll();
		
		if( !filter.isEmpty() && filter != null ) 
			messages = messageDao.findByTag(filter);	
		else 
			messages = messageDao.findAll();		
		
		theModel.addAttribute("messages", messages);
		theModel.addAttribute("filter", filter);
		theModel.addAttribute("user", user);
		
		
		return "main-page";
	}
	
	@PostMapping("/main")
	public String addNewMessage(@RequestParam String title,
								@RequestParam String tag,
								Model theModel,
								@RequestParam("file") MultipartFile file,
								@AuthenticationPrincipal User user) 
	throws IllegalStateException, IOException {
		
		Message message = new Message(title, tag, user);
		
		
		if(file != null && !file.getOriginalFilename().isEmpty()) {
			File fileDir = new File(uploadPath);
			
			if(!fileDir.exists()) {
				fileDir.mkdir();
			}
			
			String uuidFile = UUID.randomUUID().toString();
			
			String resultFileName = uuidFile + "." + file.getOriginalFilename();
			
			file.transferTo(new File(uploadPath + "/" + resultFileName));
			
			message.setFilename(resultFileName);
		}
		
		messageDao.save(message);
		
		Iterable<Message> messages = messageDao.findAll();
		
		theModel.addAttribute("messages", messages);
			
		return "main-page";
	}
	
	@GetMapping("/delete")
	public String deleteMessage(@RequestParam("userId") int theId) {
		
		messageDao.deleteById(theId);
		
		return "redirect:/main";
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
