package com.library.service.user;

import org.springframework.data.domain.Page;


import com.library.entityviews.BookBorrowingView;

import tools.jackson.databind.JsonNode;

public interface UserBorrowingService {
	

	public Page<BookBorrowingView> listAll(JsonNode jsonNode) ;

	
	

}
