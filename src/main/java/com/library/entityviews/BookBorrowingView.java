package com.library.entityviews;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingSubquery;
import com.library.entity.Book;

@EntityView(Book.class)
public interface BookBorrowingView {
	@IdMapping
	Long getId();

	String getName();

	String getAuthor();

	String getDescription();

	Integer getTotal();

//	@Mapping("(SELECT COUNT(bo.id) FROM Borrowing bo WHERE bo.book.id = VIEW(id) "
	//		+ "AND bo.status NOT IN ('RETURN', 'RETURN_ACCEPTED')) < total")
	//boolean isBorrowable();

	// Subquery for returnable using a dynamic parameter :userId
	//@MappingSubquery(ReturnableSubqueryProvider.class)
	//@Mapping("this > 0") // 'this' refers to the result of the subquery provider
	//boolean isReturnable();
}
