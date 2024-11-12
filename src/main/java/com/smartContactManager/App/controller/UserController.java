package com.smartContactManager.App.controller;





import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartContactManager.App.entities.ContactDetails;
import com.smartContactManager.App.entities.User;
import com.smartContactManager.App.helper.Message;
import com.smartContactManager.App.repositpries.ContactRepository;
import com.smartContactManager.App.repositpries.UserRepository;

import jakarta.persistence.criteria.Path;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		//get users by using userName(Email)
		String userName=principal.getName();
				
		User user= userRepository.getUserByUsername(userName);
				
		model.addAttribute("user", user);
	}
	
	//dashboard home
	@RequestMapping("/index")
	public String dashBoard(Model model, Principal principal) {
		
		model.addAttribute("title","User Dashboard | Smart Contact Manager");
		return "normal/user_dashBoard";
	}
	
	//Adding contact
	@GetMapping("/add-contacts")
	public String addContactsForm(Model model) {
		
		model.addAttribute("title","Add Contacts | Smart Contact Manager");
		model.addAttribute("contact",new ContactDetails());
		
		
		return "normal/add_contact_form";
	}
	
	//processing add contact form
  @RequestMapping(value = "/process-contact",method = {RequestMethod.POST,RequestMethod.GET},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PostMapping(value="/process-contact")
	public String processContact(@ModelAttribute ContactDetails contact,
			Principal principal, HttpSession session){
	  
	  try {
	  String name=principal.getName();
	  User user=this.userRepository.getUserByUsername(name);
	  contact.setUser(user);
	  
	  //file uploading 
	  
//	  if(file.isEmpty()) {
//		  
//		  
//		  
//	  }else {
//		  contact.setImageFile(file.getOriginalFilename());
//		  
//		  File saveFile=new ClassPathResource("static/img").getFile();
//		 
//		 Files.copy(file.getInputStream(),
//				 Paths.get(saveFile.getAbsolutePath()+File.separator+file.getContentType()+file.getOriginalFilename())
//				 , StandardCopyOption.REPLACE_EXISTING);
//	  }
	  
	  user.getContacts().add(contact);
	  this.userRepository.save(user);
	  System.out.println("Added to database");
	  
	  //message of success
	  session.setAttribute("message", new Message("Your contact is added!! Add more...","success"));
	  
	  }catch (Exception e) {
         System.out.println("ERROR"+e.getMessage());
         e.printStackTrace();
         session.setAttribute("message", new Message("Something went wrong!! Try again...","danger"));
	  }
    

    return "normal/add_contact_form";
}
	
	
  
	//creating show contacts handler
	//per page 5 contact details
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page ,Model model,Principal principal) {
		
		model.addAttribute("title","Show Contacts | Smart Contact Manager");
		
		//retrieving contacts
		
		String userName= principal.getName();
		User user=this.userRepository.getUserByUsername(userName);
		
		//current-page= page
		//contact per page=5
		Pageable pageable=PageRequest.of(page, 5);
		
		Page<ContactDetails> contactDetails=this.contactRepository.findConatctByUser(user.getId(),pageable);
		model.addAttribute("contactDetails",contactDetails);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contactDetails.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	//showing details of particular contact
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId,Model model,Principal principal ) {
		
		Optional<ContactDetails> contactOptional=this.contactRepository.findById(cId);
		ContactDetails contactDetail=contactOptional.get();
		
		model.addAttribute("contactDetail",contactDetail);
		
		String userName =principal.getName();
		User user=this.userRepository.getUserByUsername(userName);
		
		if(user.getId()==contactDetail.getUser().getId()) {
			model.addAttribute("contactDetail",contactDetail);
			model.addAttribute("title", contactDetail.getFirstName());
		}
		
		return "normal/contact_detail";
	}
	
	//handler for deleting contacts
	
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId,
			Model model,
			HttpSession session) {
		
		Optional<ContactDetails> optionalContact= this.contactRepository.findById(cId);
		ContactDetails contactDetails=optionalContact.get();
		
		//unlinking contact with user because then an only we can perform delete operation
		contactDetails.setUser(null);
		
		this.contactRepository.delete(contactDetails);
		session.setAttribute("message",new Message("Contact Deleted successfully","success"));
		
		return "redirect:/user/show-contacts/0";
	}
	
	//handler for updating contact
	
	@PostMapping("/update-contact/{cId}")
	public String updateContact(@PathVariable("cId") Integer cId, Model model) {
		
		model.addAttribute("title","Update Contact | Smart Contact Manager");
		
		ContactDetails contactDetails=this.contactRepository.findById(cId).get();
		
		model.addAttribute("contactDetails", contactDetails);
		
		return "normal/update_form";
	}
	
	
	//handler for process update contact
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute ContactDetails contactDetails,
			Model model,HttpSession session,Principal principal) {
		
		try {
			
			User user=this.userRepository.getUserByUsername(principal.getName());
			contactDetails.setUser(user);
			
			this.contactRepository.save(contactDetails);
			
			session.setAttribute("message", new Message("Your contact is updated","success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Contact Name"+contactDetails.getFirstName());
		System.out.println("Contact ID"+ contactDetails.getcId());
		
		return "redirect:/user/"+contactDetails.getcId()+"/contact";
	}
	
	//User profile handler
	@GetMapping("/profile")
	public String userProfile(Model model) {
		
		model.addAttribute("title", "User Profile | Smart Contact Manager");
		return "normal/profile";
	}
	
	//Settings handler
	
	@GetMapping("/settings")
	public String settingsHandler(Model model) {
		
		model.addAttribute("title", "Settings | Smart Contact Manager");
		return "normal/settings";
	}
	
	//Change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal,HttpSession session ) {
		
		System.out.println("Old Password "+oldPassword);
		System.out.println("New Password "+newPassword);
		String userName =principal.getName();
		User user=this.userRepository.getUserByUsername(userName);
		System.out.println(user.getPassword());
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			
			//change the password
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(user);
			session.setAttribute("message", new Message("Your password is successfully changed !! ","success"));
		}
		else {
			
			//if password does'nt match print error
			session.setAttribute("message", new Message("Please enter your correct Old Password !! ","danger"));
			return "redirect:/user/settings";
		}
		
	
		
		return "redirect:/user/index";
	}
	
}
