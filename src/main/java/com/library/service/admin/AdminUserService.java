package com.library.service.admin;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.library.entity.LoginUser;
import com.library.entity.User;

import tools.jackson.databind.JsonNode;

public interface AdminUserService {
	public User saveUser(User libr) ;
	
	public Optional<User> findById(Long libr) ;
	public Optional<User> findByLoginUser(LoginUser lusr) ;

	public void deleteUser(User usr) ;


	public Page<User> listAll(JsonNode jsonNode) ;

	public Page<User> findAll(JsonNode jsonNode) ;
	
	

}
