package com.example.user;

import com.example.user.entity.Role;
import com.example.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserApplication.class, args);
	}

}
