package com.smartcontact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//fetching user from database:
		
		User user = userRepository.getUserByUserName(username);
		
		if(user==null)
		{
			throw new UsernameNotFoundException("could not find user !!");
		}
		
		CustomUserDetails customUserDetail=new CustomUserDetails(user);
		
		return customUserDetail;
	}

}
