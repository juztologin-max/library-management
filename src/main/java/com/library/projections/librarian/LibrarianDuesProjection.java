package com.library.projections.librarian;

import java.time.LocalDateTime;

public interface LibrarianDuesProjection {
	Long getId();

	LocalDateTime getBorrowDate();

	Long getDue();

	String getBorrower();

	String getBook();
}
