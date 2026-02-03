package com.library.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.library.entity.LoginUserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginUserService loginUserService;

    // @Autowired
    public UserDetailsServiceImpl(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserDetails userDetails = new LoginUserDetails();
        userDetails.setUser(loginUserService.findByNameEnabled(username).get());
        return userDetails;
    }
}
