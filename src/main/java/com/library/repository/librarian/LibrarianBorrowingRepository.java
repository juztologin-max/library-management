package com.library.repository.librarian;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.library.entity.Borrowing;
import com.library.projections.librarian.LibrarianBorrowingProjection;

public interface LibrarianBorrowingRepository
		extends JpaRepository<Borrowing, Long>, JpaSpecificationExecutor<Borrowing> {
	/*@Query("""
			SELECT bo FROM Borrowing AS bo WHERE bo.status IN ('RETURN','BORROW')
			""")*/
	Page<LibrarianBorrowingProjection> findAllBorrowingsBy(Specification<Borrowing> spec, Pageable pageable);

	/*@Query("""
			SELECT bo FROM Borrowing AS bo WHERE bo.status IN ('RETURN','BORROW')
			""")*/
	Page<LibrarianBorrowingProjection> findAllBorrowingsBy(Pageable pageable);

}
