package com.library.security;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

	@Value("${remember_me.key}")
	private String remKey;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		//@formatter:off
		http.redirectToHttps(Customizer.withDefaults())
		    .authorizeHttpRequests(auth -> 
		    				    	 auth.requestMatchers( "/login","/common/**", "/widgets/**").permitAll()
		     							.anyRequest().authenticated())
			.formLogin(loginForm -> 
						   loginForm.loginPage("/login").loginProcessingUrl("/login")
								 	.defaultSuccessUrl("/dashboard",true)
								 	.failureHandler((req,res,ex)->	
								 						res.sendRedirect("/login?message-type=error&message="
								 							+ URLEncoder.encode("Incorrect username and/or password",StandardCharsets.UTF_8))))
			.logout(logout -> logout.logoutSuccessHandler((req,res,ex)->
																res.sendRedirect("/login?message-type=info&message="
						                                           + URLEncoder.encode("Successfully logged out",StandardCharsets.UTF_8))))
			
			.exceptionHandling(ex -> ex.accessDeniedPage("/errors/access-denied"))
			.rememberMe(rem -> rem.key(remKey).tokenValiditySeconds(3600)
			.rememberMeParameter("remember-checkbox"));
		//@formatter:on
		return http.build();

	}
}
