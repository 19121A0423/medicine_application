package com.user.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.user.bean.UserBean;
import com.user.controller.UserController;
import com.user.entity.User;
import com.user.exception.UserNotFoundByIdException;
import com.user.repository.UserRepo;
import com.user.service.UserService;
import com.user.structure.ResponseStructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class UserServiceImpl implements UserService {
	
	private static Logger log = LoggerFactory
			.getLogger(UserServiceImpl.class.getSimpleName());
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public ResponseEntity<ResponseStructure<UserBean>> save(UserBean user) {
		
		log.info("UserServiceImpl save method start {} "+user);
		
		if(user.getUserEmail()==null || user.getUserPassword()==null) {
			throw new IllegalArgumentException("User Values cannot be null");
		}
		
		User userEntity = new User();
		userEntity=	beanToEntity(userEntity, user);	
		userEntity = userRepo.save(userEntity);	
		user=entityToBean(userEntity, user);
		sendMail(user);
				
		ResponseStructure<UserBean> structure = new ResponseStructure<>();
		structure.setData(user);
		structure.setMessage("Data Saved Successfully !!!");
		structure.setStatusCode(HttpStatus.OK.value());
		
		log.info("UserServiceImpl save method end {} "+user);	
		
		return new ResponseEntity<ResponseStructure<UserBean>>(structure,HttpStatus.OK) ;
		
	}
	
	
	public void sendMail(UserBean user) {
		
		log.info("User  service implementation send mail method start {} "+user);
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getUserEmail());
		mail.setSubject(" Registration ");
		mail.setText("Hello " + user.getUserName() + " your account created succesfully as a "
				+ user.getUserRole());
		mail.setSentDate(new Date());
		javaMailSender.send(mail);
		log.info("User  service implementation send mail method end {} "+user);

	}

	@Override
	public ResponseEntity<ResponseStructure<UserBean>> update(UserBean user) throws UserNotFoundByIdException {
		
		log.info("User  service implementation update method start {} "+user);
		
		if(user.getUserId()==0) {
			throw new IllegalArgumentException("User Id cannot be null");
		}
		
		Optional<User> optional = userRepo.findById(user.getUserId());
		
		if(optional.isPresent()) {	
			User userEntity = optional.get();
			userEntity=beanToEntity(userEntity, user);
			userEntity= userRepo.save(userEntity);
			user=entityToBean(userEntity, user);
					
			ResponseStructure<UserBean> structure = new ResponseStructure<>();
			structure.setData(user);
			structure.setMessage("Data Saved Successfully !!!");
			structure.setStatusCode(HttpStatus.OK.value());
			log.info("User  service implementation update method end {} "+user);
			return new ResponseEntity<ResponseStructure<UserBean>>(structure,HttpStatus.OK) ;
			
		}else {
			throw new UserNotFoundByIdException("User does not exist by this "+user.getUserId());
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserBean>> getById(int userId) throws UserNotFoundByIdException {
		log.info("User  service implementation getById method start {} "+userId);
		if(userId==0) {
			throw new IllegalArgumentException("User Id cannot be null");
		}
		Optional<User> optional = userRepo.findById(userId);
		if(optional.isPresent()){
			
			User userEntity = optional.get();
			UserBean bean = new UserBean();
			bean=entityToBean(userEntity, bean);
		
			ResponseStructure<UserBean> structure = new ResponseStructure<>();
			structure.setData(bean);
			structure.setMessage("Data fetched Successfully !!!");
			structure.setStatusCode(HttpStatus.FOUND.value());
			
			log.info("User  service implementation getById method end {} "+userId);
			return new ResponseEntity<ResponseStructure<UserBean>>(structure, HttpStatus.FOUND);
			
		}else {
			 throw new UserNotFoundByIdException("User Not Found By This Id "+userId) ;
		}		
	}
	
	@Override
	public ResponseEntity<ResponseStructure<UserBean>> delete(int userId) throws UserNotFoundByIdException {
		log.info("User  service implementation delete method start {} "+userId);
		if(userId==0) {
			throw new IllegalArgumentException("User Id cannot be null");
		}
		Optional<User> optional = userRepo.findById(userId);
		
		if(optional.isPresent()){	
			User userEntity=optional.get();
			userRepo.deleteById(userId);			
			UserBean bean = new UserBean();		
			bean =  entityToBean(userEntity, bean);
		    
			ResponseStructure<UserBean> structure = new ResponseStructure<>();
			structure.setData(bean);
			structure.setMessage("User data deleted successfully  !!!");
			structure.setStatusCode(HttpStatus.OK.value());	
			log.info("User  service implementation delete method end {} "+userId);
			return new ResponseEntity<ResponseStructure<UserBean>>(structure, HttpStatus.OK);
		}else {
			throw new UserNotFoundByIdException("User Not Present By This Id "+userId);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<List<UserBean>>> getAll() {
		
		log.info("User  service implementation get all method start ");

		List<User> users = userRepo.findAll(); 
		List<UserBean> usersList = new ArrayList<>();
		for(User user :users) {
			
			UserBean bean = new UserBean();			
			bean=entityToBean(user, bean);
		    usersList.add(bean);	    
		}
		
		ResponseStructure<List<UserBean>> structure = new ResponseStructure<>();
		structure.setData(usersList);
		structure.setMessage("User List Found  successfully  !!!");
		structure.setStatusCode(HttpStatus.FOUND.value());	
		
		log.info("User  service implementation get all method end {} "+usersList);
		
		return new ResponseEntity<ResponseStructure<List<UserBean>>>(structure, HttpStatus.FOUND);
	}

		public User beanToEntity(User userEntity, UserBean bean) {
			log.info("User  service implementation beanToEntity method start {}"+userEntity);

			userEntity.setName(bean.getUserName());
			userEntity.setEmail(bean.getUserEmail());
			userEntity.setGender(bean.getUserGender());
			userEntity.setMobile_number(bean.getUserMobileNumber());
			userEntity.setPassword(bean.getUserPassword());
			userEntity.setUser_role(bean.getUserRole());
			userEntity.setStatus(bean.getUserStatus());
			
			log.info("User  service implementation beanToEntity method end "+bean);
			return userEntity;
			
		}
		
		public UserBean entityToBean(User userEntity, UserBean bean) {
			
			
			log.info("User  service implementation beanToEntity method start {}"+bean);

			bean.setUserId(userEntity.getUser_id());
			bean.setUserEmail(userEntity.getEmail());
			bean.setUserPassword(userEntity.getPassword());
			bean.setUserGender(userEntity.getGender());
			bean.setUserMobileNumber(userEntity.getMobile_number());
			bean.setUserName(userEntity.getName());
			bean.setUserRole(userEntity.getUser_role());
			bean.setUserStatus(userEntity.getStatus());
			
			log.info("User  service implementation beanToEntity method start {}"+userEntity);

			return bean;
		}

	
}
