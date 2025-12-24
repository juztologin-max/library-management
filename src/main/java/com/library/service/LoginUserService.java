package com.library.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.entity.LoginUser;
import com.library.repository.LoginUserRepository;

@Service
public class LoginUserService {
	@Autowired
	private  LoginUserRepository repo;

	public LoginUser saveLoginUser(LoginUser usr) {
		return repo.save(usr);
	}
	
	public Optional<LoginUser> findById(long id) {
		return repo.findById(id);
	}
	
	public Optional<LoginUser> findByName(String name) {
		return repo.findByName( name);
	}
	
	
}
