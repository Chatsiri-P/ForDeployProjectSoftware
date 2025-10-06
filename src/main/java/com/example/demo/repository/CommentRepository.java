package com.example.demo.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByEventOrderByCreatedAtDesc(Event event);
    Page<Comment> findByEventOrderByCreatedAtDesc(Event event, Pageable pageable);

	long countByEvent(Event event);
}
