package com.example.demo.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.*;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	Optional<Bookmark> findByUserAndEvent(User user, Event event);

	List<Bookmark> findByUser(User user);

	boolean existsByUserAndEvent(User user, Event event);

	long countByEvent(Event event);
}