package com.smartcontact.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int c_id;
	private String name;
	private String secondName;
	private String phoneNumber;
	@Column(unique = true)
	private String email;
	private String work;
	private String image;
	@Column(length=1000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public Contact() {
		// TODO Auto-generated constructor stub
	}

	public Contact(int c_id, String name, String secondName, String phoneNumber, String email, String work,
			String image, String description, User user) {
		super();
		this.c_id = c_id;
		this.name = name;
		this.secondName = secondName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.work = work;
		this.image = image;
		this.description = description;
		this.user = user;
	}

	public int getC_id() {
		return c_id;
	}

	public void setC_id(int c_id) {
		this.c_id = c_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	@Override
//	public String toString() {
//		return "Contact [c_id=" + c_id + ", name=" + name + ", secondName=" + secondName + ", phoneNumber="
//				+ phoneNumber + ", email=" + email + ", work=" + work + ", image=" + image + ", description="
//				+ description + ", user=" + user + "]";
//	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.c_id==((Contact)obj).getC_id();
	}
	
	
}
