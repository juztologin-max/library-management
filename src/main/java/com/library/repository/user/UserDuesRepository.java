package com.library.repository.user;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.library.component.SearchSpecification;
import com.library.entity.Borrowing;
import com.library.projections.librarian.LibrarianBorrowingProjection;
import com.library.projections.librarian.LibrarianDuesProjection;

public interface UserDuesRepository extends JpaRepository<Borrowing, Long>, JpaSpecificationExecutor<Borrowing> {

	@Query("""
			SELECT
				bo.id AS id,
				bo.borrowDate AS borrowDate,
				TIMESTAMPDIFF(DAY, bo.borrowDate,LOCAL DATETIME) AS due,
				u.legalName AS borrower,
				b.name AS book
			FROM
				Borrowing AS bo
			JOIN
				bo.book AS b
			JOIN
				bo.user AS u
			WHERE
				TIMESTAMPDIFF(DAY, bo.borrowDate,LOCAL DATETIME) >= :dueLimit
				AND bo.status IN ('BORROW_ACCEPTED' , 'RETURN', 'BORROW')
				AND u.id = :userId
			""")
	Page<LibrarianDuesProjection> getDues(Pageable pageable, @Param("dueLimit") Long dueLimit, @Param("userId") Long userId);


}
