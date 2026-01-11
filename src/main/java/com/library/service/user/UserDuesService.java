package com.library.service.user;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.library.projections.librarian.LibrarianDuesProjection;

import tools.jackson.databind.JsonNode;

public interface UserDuesService {

	public Page<LibrarianDuesProjection> listAll(JsonNode jsonNode,@AuthenticationPrincipal Long usrId) ;

	
	

}
