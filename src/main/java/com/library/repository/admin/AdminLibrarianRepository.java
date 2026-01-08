package com.library.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.Librarian;

public interface AdminLibrarianRepository extends JpaRepository<Librarian,Long>,JpaSpecificationExecutor<Librarian> {

}
