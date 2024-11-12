package com.smartContactManager.App.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartContactManager.App.entities.ContactDetails;
import com.smartContactManager.App.entities.User;
import com.smartContactManager.App.repositpries.ContactRepository;
import com.smartContactManager.App.repositpries.UserRepository;

@RestController
public class SearchController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//Search Method
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal ){
		
		
		User user=this.userRepository.getUserByUsername(principal.getName());
		
	List<ContactDetails> contactDetails=this.contactRepository.findByFirstNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contactDetails);
		
	}
}
