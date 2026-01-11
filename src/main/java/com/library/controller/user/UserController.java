package com.library.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.dto.UserDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/dashboard")
    @Valid
    public String getAdminDashboard(Model m, @AuthenticationPrincipal UserDetails usr) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(usr.getUsername());
        m.addAttribute("usr", userDTO);
        m.addAttribute("title", "Dashboard");
        m.addAttribute("mainMenuItem", "Dashboard");
        m.addAttribute("content", "user/dashboard :: content");
        return "user/user-layout";
    }
    
    @GetMapping("/manage-borrowing")
    @Valid
    public String getUserBorrowing(Model m, @AuthenticationPrincipal UserDetails usr) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(usr.getUsername());
        m.addAttribute("usr", userDTO);
        m.addAttribute("title", "Borrowing");
        m.addAttribute("mainMenuItem", "Borrowing");
        m.addAttribute("content", "user/manage-borrowing :: content");
        return "user/user-layout";
    }
    @GetMapping("/dues")
    @Valid
    public String getUserDues(Model m, @AuthenticationPrincipal UserDetails usr) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(usr.getUsername());
        m.addAttribute("usr", userDTO);
        m.addAttribute("title", "Dues");
        m.addAttribute("mainMenuItem", "Dues");
        m.addAttribute("content", "user/dues :: content");
        return "user/user-layout";
    }

    }
