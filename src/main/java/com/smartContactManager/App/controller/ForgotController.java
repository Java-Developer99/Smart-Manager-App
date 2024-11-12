package com.smartContactManager.App.controller;

import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.smartContactManager.App.services.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	
	Random random=new Random();

	@Autowired
	private EmailService emailService;
	
	//email handler
	@RequestMapping("/forgot")
	public String forgotEmailHandler() {
		
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOtpHandler(@RequestParam("email") String email,HttpSession session) {
		
		System.out.println("Email "+email);
		
		//generating 4-digit number otp.
		
		
		int otp=random.nextInt(9999);
		
		System.out.println("OTP "+otp);
		
		//code for sending otp to email
		
		String subject="OTP From SCM";
		String message=" OTP = "+otp+"";
		String to=email;
		
		boolean flag=this.emailService.sendEmail(message, subject, to);
		
		if (flag && email.equals((String) session.getAttribute("email"))) {
	        session.setAttribute("myOtp", otp);
	        session.setAttribute("email", email);
	        return "verify_otp"; // Redirect to verify_otp.html
	    }
		else {
		
			session.setAttribute("message", "Check your email id!!");
			return "forgot_email_form";
		
	}
  }
	
	//verify otp handler
	@PostMapping("/verify-otp")
	public String verifyOTp(@RequestParam("otp") Integer otp,HttpSession session) {
		
		Integer myotp=(int)session.getAttribute("myOtp");
		String email =(String)session.getAttribute("email");
		if(myotp==otp) {
			return "password_change_form";
		}
		else {
			session.setAttribute("message", "You have entered wrong otp");
			return "verify-otp";
		}
		
	}
}