package com.admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.admin.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	public Cart getCartByUserId(Integer userId);

}
