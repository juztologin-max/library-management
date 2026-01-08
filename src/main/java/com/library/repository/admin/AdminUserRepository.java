package com.library.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.Librarian;
import com.library.entity.User;

public interface AdminUserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

}
