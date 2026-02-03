package com.library.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.Book;

public interface AdminBookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

}
