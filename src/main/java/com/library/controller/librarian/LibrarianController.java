package com.library.controller.librarian;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.dto.UserDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

	@GetMapping("/dashboard")
	@Valid
	public String getAdminDashboard(Model m, @AuthenticationPrincipal UserDetails usr) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(usr.getUsername());
		m.addAttribute("usr", userDTO);
		m.addAttribute("title", "Dashboard");
		m.addAttribute("mainMenuItem", "Dashboard");
		m.addAttribute("content", "librarian/dashboard :: content");
		return "librarian/librarian-layout";
	}

	@GetMapping("/manage-borrowing")
	@Valid
	public String getManageAdmin(Model m, @AuthenticationPrincipal UserDetails usr) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(usr.getUsername());
		m.addAttribute("usr", userDTO);
		m.addAttribute("title", "Settings :: Manage Borrowing");
		m.addAttribute("mainMenuItem", "Manage Borrowing");
		m.addAttribute("content", "librarian/manage-borrowing :: content");
		return "librarian/librarian-layout";
	}

	@GetMapping("/manage-book")
	@Valid
	public String getManageBook(Model m, @AuthenticationPrincipal UserDetails usr) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(usr.getUsername());
		m.addAttribute("usr", userDTO);
		m.addAttribute("title", "Manage Book");
		m.addAttribute("mainMenuItem", "Manage Book");
		m.addAttribute("content", "admin/manage-book :: content");
		return "librarian/librarian-layout";
	}

}
