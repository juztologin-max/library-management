package com.library.repository.librarian;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.library.entity.Borrowing;
import com.library.projections.librarian.LibrarianBorrowingProjection;

public interface LibrarianBorrowingRepository extends JpaRepository<Borrowing, Long>, JpaSpecificationExecutor<Borrowing> {
    Page<LibrarianBorrowingProjection> findAllProjectedBy(Specification<Borrowing> spec, Pageable pageable);
    Page<LibrarianBorrowingProjection> findAllProjectedBy( Pageable pageable);

}
