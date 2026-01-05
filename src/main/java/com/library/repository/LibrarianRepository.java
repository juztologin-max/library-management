package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.Librarian;

public interface LibrarianRepository extends JpaRepository<Librarian,Long>,JpaSpecificationExecutor<Librarian> {

}
