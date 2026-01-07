package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.Book;
import com.library.entity.Librarian;

public interface BookRepository extends JpaRepository<Book,Long>,JpaSpecificationExecutor<Book> {

}
