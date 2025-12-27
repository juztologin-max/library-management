package com.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.LoginRoles;

public interface LoginRolesRepository extends JpaRepository<LoginRoles, Long> {
	Optional<LoginRoles> findByRoleName(String role);
}
