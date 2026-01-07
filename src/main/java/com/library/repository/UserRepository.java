package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.Librarian;
import com.library.entity.User;

public interface UserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

}
