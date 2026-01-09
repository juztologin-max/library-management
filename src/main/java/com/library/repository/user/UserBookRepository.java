package com.library.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.library.entity.Book;
import com.library.projections.BookBorrowableReturnableStatus;

public interface UserBookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

	@Query("""
			SELECT
			 	((COUNT(CASE WHEN bo.status NOT IN ( 'RETURN_ACCEPTED') THEN 1 END) < b.total) AND
			    	(COUNT(CASE WHEN bo.status NOT IN ('RETURN', 'RETURN_ACCEPTED') AND bo.user.id = :userId THEN 1 END) < 1)) AS borrowbleByUser,
			     (COUNT(CASE WHEN bo.status NOT IN ('RETURN', 'RETURN_ACCEPTED','BORROW') AND bo.user.id = :userId THEN 1 END) >= 1) AS returnableByUser
			 FROM Book b
			 	LEFT JOIN b.borrowings bo
			 WHERE b.id = :bookId
			 GROUP BY b.id
			         """)
	BookBorrowableReturnableStatus getbookBorrowableReturnableStatus(@Param("userId") Long userId,
			@Param("bookId") Long bookId);
}
