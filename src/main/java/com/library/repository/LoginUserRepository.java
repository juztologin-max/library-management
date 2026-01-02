package com.library.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.LoginUser;

public interface LoginUserRepository extends JpaRepository<LoginUser, Long>,JpaSpecificationExecutor<LoginUser> {

	Optional<LoginUser> findByName(String name);
	
	Page<LoginUser> findBy(Pageable pageable);
	Page<LoginUser> findBy(Pageable pageable,Specification<LoginUser> spec);

}
