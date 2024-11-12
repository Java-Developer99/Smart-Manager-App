package com.smartContactManager.App.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContactManager.App.entities.User;
import com.smartContactManager.App.helper.Message;
import com.smartContactManager.App.repositpries.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home | Smart Contact Manager");
		return "home";
	}
	

	@RequestMapping("/about/")
	public String about(Model model) {
		model.addAttribute("title","About | Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup/")
	public String signup(Model model) {
		model.addAttribute("title","Signup | Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	//handler for registering user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value = "agreement",defaultValue = "false")boolean agreement,
	 	Model model, HttpSession session) {
		
		try {
			
			if (!agreement) {
				System.out.println("Please agree the terms and conditions");
				throw new Exception("Please agree the terms and conditions");
			}
			
			if(result1.hasErrors()) {
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement :" +agreement);
			System.out.println("user"+user);
			User result=this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully Registered","alert-success"));
			return "signup";
			
		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went worng!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
	
	
}
	
	//Login Handler
	@GetMapping("/signin")
	public String customLogin(Model model,HttpSession session) {		
		model.addAttribute("title","Login | Smart Contact Manager ");
		session.setAttribute("Message", new Message("Invalid Username and password", "alert-danger"));
		return "login";
	}
}
