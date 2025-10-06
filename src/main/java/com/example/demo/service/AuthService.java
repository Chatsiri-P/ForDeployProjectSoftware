package com.example.demo.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.model.SystemRole;
import com.example.demo.model.User;
import com.example.demo.repository.*;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final SystemRoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AuthService(UserRepository userRepository, SystemRoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User register(UserRegistrationDto registrationDto) {
		validateRegistration(registrationDto);

		User user = createUserFromDto(registrationDto);
		assignDefaultRole(user);

		return userRepository.save(user);
	}

	private void validateRegistration(UserRegistrationDto dto) {
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		if (userRepository.existsByUsername(dto.getUsername())) {
			throw new RuntimeException("Username already exists");
		}
	}

	private User createUserFromDto(UserRegistrationDto dto) {
		User user = new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		return user;
	}

	private void assignDefaultRole(User user) {
		SystemRole userRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new RuntimeException("Default role not found"));
		user.setRoles(Set.of(userRole));
	}

	public boolean isEmailAvailable(String email) {
		return !userRepository.existsByEmail(email);
	}

	public boolean isUsernameAvailable(String username) {
		return !userRepository.existsByUsername(username);
	}
}