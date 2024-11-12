package com.smartContactManager.App.repositpries;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartContactManager.App.entities.ContactDetails;
import com.smartContactManager.App.entities.User;


public interface ContactRepository extends JpaRepository<ContactDetails, Integer> {
	
	//trying to implement pagination
	@Query("from ContactDetails as c where c.user.id =:userId")
	public Page<ContactDetails> findConatctByUser(@Param("userId") int userId,Pageable pageable);

	
	public List<ContactDetails> findByFirstNameContainingAndUser(String firstName,User user);
}
