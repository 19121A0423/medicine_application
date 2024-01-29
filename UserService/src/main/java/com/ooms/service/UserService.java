package com.ooms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ooms.entity.bean.Cart;
import com.ooms.entity.bean.UserBean;
import com.ooms.util.ResponseStructure;

public interface UserService {
	
	
	public ResponseEntity<ResponseStructure<UserBean>> save(UserBean user);
	public ResponseEntity<ResponseStructure<UserBean>> update(UserBean user);
	public ResponseEntity<ResponseStructure<UserBean>> delete(int userId);
	public ResponseEntity<ResponseStructure<UserBean>> getById(int userId);
	public ResponseEntity<ResponseStructure<List<UserBean>>> getAll();
	
	
	public ResponseEntity<ResponseStructure<Cart>> save(Cart cart);
	public ResponseEntity<ResponseStructure<Cart>> update(Cart cart);
	public ResponseEntity<ResponseStructure<Cart>> deleteCart(int cartId);
	public ResponseEntity<ResponseStructure<Cart>> getCartById(int cartId);
	public ResponseEntity<ResponseStructure<List<Cart>>> getAllCarts();
	
	

}