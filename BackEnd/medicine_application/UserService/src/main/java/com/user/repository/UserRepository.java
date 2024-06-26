package com.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.user.entity.User;


public interface UserRepository extends JpaRepository<User, Integer>  {

	User findByUserEmailAndUserPassword(String userEmail, String userPassword);
	
	Optional<User> findByUserEmail(String userEmail);
	
	public List<User> findByUserEmailOrUserMobileNumber(String userEmail,Long userMobileNumber);

	Optional<User> findByUserName(String userName);
	
}
