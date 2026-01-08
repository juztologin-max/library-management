package com.library.repository.user;

import com.library.entity.Book;
import com.library.entityviews.BookBorrowingView;
import org.springframework.stereotype.Repository;


import com.blazebit.persistence.spring.data.repository.EntityViewRepository;
import com.blazebit.persistence.spring.data.repository.EntityViewSpecificationExecutor;

@Repository
public interface UserBorrowingRepository extends 
    EntityViewRepository<BookBorrowingView, Long>, 
    EntityViewSpecificationExecutor<BookBorrowingView,Book> {}