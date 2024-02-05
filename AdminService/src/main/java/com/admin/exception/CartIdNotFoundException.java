package com.admin.exception;

public class CartIdNotFoundException extends Exception {
	
	private String message;

	public CartIdNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
