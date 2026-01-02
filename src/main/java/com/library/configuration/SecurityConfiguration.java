package com.library.configuration;

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
	@Value("${remember_me.valididty}")
	private int validity;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		//@formatter:off
		http.redirectToHttps(Customizer.withDefaults())
		    .authorizeHttpRequests(auth -> 
		    				    	 auth.requestMatchers( "/login","/common/**", "/widgets/**").permitAll()
		    				    	     .requestMatchers("/admin/**").hasAuthority("ADMIN")
		     							 .anyRequest().authenticated())
			.formLogin(loginForm -> 
						   loginForm.loginPage("/login").loginProcessingUrl("/login")
								 	.defaultSuccessUrl("/admin/dashboard",true)
								 	.failureHandler((req,res,ex)->	
								 						res.sendRedirect("/login?message-type=error&message="
								 							+ URLEncoder.encode("Incorrect username and/or password",StandardCharsets.UTF_8))))
			.logout(logout -> logout.logoutSuccessHandler((req,res,ex)->
																res.sendRedirect("/login?message-type=info&message="
						                                           + URLEncoder.encode("Successfully logged out",StandardCharsets.UTF_8))))
			
			.exceptionHandling(ex -> ex.accessDeniedPage("/errors/access-denied"))
			.rememberMe(rem -> rem.key(remKey).tokenValiditySeconds(validity)
			.rememberMeParameter("remember-checkbox"));
			//.csrf(csrf -> csrf.disable());
		//@formatter:on
		return http.build();

	}
}
