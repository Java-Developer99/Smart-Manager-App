package com.smartContactManager.App.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="contact_Details")
public class ContactDetails {
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	private String firstName;
	private String lastName;
	private String work;
	private String email;
	private String phone;
//	private String imageFile;
	
	@Column(length = 50000)
	private String description;
	
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

//	public String getImageFile() {
//	    return imageFile;
//	}
//
//	public void setImageFile(String imageFile) {
//	    this.imageFile = imageFile;
//	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	

//	@Override
//	public String toString() {
//		return "ContactDetails [user=" + user + ", cId=" + cId + ", firstName=" + firstName + ", lastName=" + lastName
//				+ ", work=" + work + ", email=" + email + ", phone=" + phone + ", image=" + imageFile + ", description="
//				+ description + "]";
//	}
//	
	
	
	
	

}
