package com.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.library.entity.LoginUserDetails;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	LoginUserService loginUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginUserDetails userDetails=new LoginUserDetails();
		userDetails.setUser(loginUserService.findByName(username).get());
		return userDetails;
	}

}
