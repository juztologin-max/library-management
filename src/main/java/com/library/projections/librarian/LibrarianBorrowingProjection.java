package com.library.projections.librarian;

import java.time.LocalDateTime;

import com.library.other.BorrowStatus;

public interface LibrarianBorrowingProjection {
	Long getId();

	BookView getBook();
	interface BookView {
		String getName();

	}

	LocalDateTime getBorrowDate();

	BorrowStatus getStatus();

	UserView getUser();
	interface UserView {
		String getLegalName();
	}
}
