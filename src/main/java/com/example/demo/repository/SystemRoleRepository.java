package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.SystemRole;

@Repository
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {
	Optional<SystemRole> findByName(String name);

	boolean existsByName(String name);
}