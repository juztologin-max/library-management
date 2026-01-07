package com.library.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.library.entity.Librarian;
import com.library.entity.User;

import tools.jackson.databind.JsonNode;

public interface UserService {
	public User saveUser(User libr) ;
	
	public Optional<User> findById(Long libr) ;

	public void deleteUser(User usr) ;


	public Page<User> listAll(JsonNode jsonNode) ;

	public Page<User> findAll(JsonNode jsonNode) ;
	
	

}
