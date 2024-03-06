package com.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.bean.PasswordUpdateRequest;
import com.user.bean.UserBean;
import com.user.exception.DuplicateMobileNumberException;
import com.user.exception.DuplicateEmailIdException;
import com.user.exception.UserNotFoundByIdException;
import com.user.service.UserService;

@RestController
public class UserController {
	
	private static Logger log = LoggerFactory
			.getLogger(UserController.class.getSimpleName());

	@Autowired
	private UserService service;

	@PostMapping("/users/save")
	public ResponseEntity<UserBean> save(@RequestBody UserBean user) throws DuplicateEmailIdException, DuplicateMobileNumberException {
		log.info("UserController save method start {}"+user);	
		UserBean userBean=null;
		try {
			 userBean = service.save(user);
		}catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("UserController save method end{}"+user);	
		 return ResponseEntity.status(HttpStatus.OK).body(userBean);
	}

	@PutMapping("/users/update")
	public ResponseEntity<UserBean> update(@RequestBody UserBean user) throws UserNotFoundByIdException {
		log.info("UserController update method start {}"+user);	
		UserBean userBean=null;
		try {
			 userBean = service.update(user);
		}catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("UserController update method 	end {}"+user);	
		 return ResponseEntity.status(HttpStatus.OK).body(userBean);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<UserBean> getById(@PathVariable int userId) throws UserNotFoundByIdException {
		
		log.info("UserController getById method start {}"+userId);	
		UserBean userBean=null;
		try {
			 userBean = service.getById(userId);
		}catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("UserController getById method 	end {}"+userId);	
		
		return ResponseEntity.status(HttpStatus.OK).body(userBean);
	}

	@DeleteMapping("/users/delete/{userId}")
	public ResponseEntity<UserBean> delete(@PathVariable int userId) throws UserNotFoundByIdException {
		log.info("UserController delete method start {}"+userId);	
		UserBean userBean=null;
		try {
			  userBean = service.delete(userId);
		}catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("UserController delete method 	end {}"+userId);			
		return ResponseEntity.status(HttpStatus.OK).body(userBean);
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserBean>> getAll() {
		log.info("UserController getAll method start");	
		 List<UserBean> usersList = service.getAll();
		log.info("UserController getAll method end");	
		return ResponseEntity.status(HttpStatus.OK).body(usersList);
	}

	
	@GetMapping("/users/validate/{email}/{password}")
	public ResponseEntity<UserBean> userValiadtion(@PathVariable(value="email") String email, @PathVariable(value="password") String password) {
		log.info("UserController userValiadtion method start");	
		UserBean user =null;
		try {
			user = service.validateUser(email,password);
			log.info("UserController userValiadtion method end");	
			return new ResponseEntity<UserBean>(user,HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/users/updatepassword")
	public ResponseEntity<UserBean> updatePassword(@RequestBody PasswordUpdateRequest request){
		log.info("UserController updatePassword method start");	
		log.info("Update password : "+request);
		UserBean user =null;
		try {
			user = service.updatePassword(request.getEmail(),request.getNewPassword());
			log.info("UserController updatePassword method end");	
			return new ResponseEntity<UserBean>(user,HttpStatus.OK);
		}
		catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
