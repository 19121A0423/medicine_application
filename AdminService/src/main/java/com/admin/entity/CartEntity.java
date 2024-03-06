package com.admin.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import com.admin.bean.Product;

@Entity
@Table(name="cart")
public class CartEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id")
	private Integer cartId;
	
	@NotNull
	@Column(name = "user_id")
	private Integer userId;
	
	
	@OneToMany
	private List<ProductEntity> products;
	
	@NotNull
	@Column(name="quantity")
	private Integer quantity;

	@Column(name="amount")
	private Double amount; 
	
	private String status;


	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public List<ProductEntity> getProducts() {
		return products;
	} 

	public void setProducts(List<ProductEntity> products) {
		this.products = products;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CartEntity [cartId=" + cartId + ", userId=" + userId + ", products=" + products + ", quantity="
				+ quantity + ", amount=" + amount + ", status=" + status + "]";
	}

}