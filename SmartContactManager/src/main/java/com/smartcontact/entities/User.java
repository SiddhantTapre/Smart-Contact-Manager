package com.smartcontact.entities;

import java.util.ArrayList;
import java.util.List;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.CascadeType;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int u_id;
	@NotBlank(message="Required")
	@Size(min=2,max=10,message="Name should have charecters between 2 - 10")
	private String name;
	@Column(unique = true)
	@NotBlank(message="Required")
	@Email
	private String email;
	@NotBlank(message="Required")
	@Size
	private String password;
	private String role;
	private String image;
	@Column(length = 500)
	private String about;
	private boolean enabled;
	
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy="user",orphanRemoval = true)
	private List<Contact> list=new ArrayList<>();
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(int u_id, String name, String email, String password, String role, String image, String about,
			boolean enabled, List<Contact> list) {
		super();
		this.u_id = u_id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.image = image;
		this.about = about;
		this.enabled = enabled;
		this.list = list;
	}

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Contact> getList() {
		return list;
	}

	public void setList(List<Contact> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "User [u_id=" + u_id + ", name=" + name + ", email=" + email + ", password=" + password + ", role="
				+ role + ", image=" + image + ", about=" + about + ", enabled=" + enabled + ", list=" + list + "]";
	}
	
	
	
	

}
