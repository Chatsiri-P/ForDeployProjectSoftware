package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.EventRole;

@Repository
public interface EventRoleRepository extends JpaRepository<EventRole, Long> {
	Optional<EventRole> findByName(String name);

	boolean existsByName(String name);
}