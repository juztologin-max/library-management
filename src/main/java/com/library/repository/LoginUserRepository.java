package com.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.LoginUser;

public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {

	Optional<LoginUser> findByName(String name);
	
	List<LoginUser> findBy(Pageable pageable);

}
