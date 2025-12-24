package com.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.LoginUser;

public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {

	Optional<LoginUser> findByName(String name);

}
