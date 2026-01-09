package com.library.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.Librarian;
import com.library.entity.LoginUser;
import com.library.entity.User;

public interface AdminUserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

	Optional<User> findByLoginUser(LoginUser lusr);

}
