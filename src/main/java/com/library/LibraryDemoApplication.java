package com.library;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.library.entity.LoginRoles;
import com.library.repository.LoginRolesRepository;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableDiscoveryClient
public class LibraryDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryDemoApplication.class, args);
	}

	@Bean
	public ApplicationRunner fillRolesTable(LoginRolesRepository repo,
			@Value("${setup.initial.fill-roles-table}") String fillRolesTable) {
		return args -> {
			if (Boolean.parseBoolean(fillRolesTable)) {
				repo.save(new LoginRoles("ADMIN"));
				repo.save(new LoginRoles("LIBRARIAN"));
				repo.save(new LoginRoles("USER"));
			}
		};
	}

	@Bean
	public RestTemplate createRestTemplate() {
		return new RestTemplate();
	}

}
