package com.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Integer addressId;

	@Column(name = "street_name")
	private String streetName;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "pin_code")
	private Long pinCode;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "status")
	private String status;

	@Override
	public String toString() {
		return "AddressEntity [addressId=" + addressId + ", streetName=" + streetName + ", city=" + city + ", state="
				+ state + ", pinCode=" + pinCode + ", userId=" + userId + ", status=" + status + "]";
	}

	public Address() {
		super();
	}

	public Address(Integer addressId, String streetName, String city, String state, Long pinCode, Integer userId,
			String status) {
		super();
		this.addressId = addressId;
		this.streetName = streetName;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.userId = userId;
		this.status = status;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getPinCode() {
		return pinCode;
	}

	public void setPinCode(Long pinCode) {
		this.pinCode = pinCode;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
