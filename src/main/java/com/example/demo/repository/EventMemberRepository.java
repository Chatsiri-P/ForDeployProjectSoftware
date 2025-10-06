package com.example.demo.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Event;
import com.example.demo.model.EventMember;
import com.example.demo.model.User;

@Repository
public interface EventMemberRepository extends JpaRepository<EventMember, Long> {
	Optional<EventMember> findByUserAndEvent(User user, Event event);

	List<EventMember> findByUser(User user);

	List<EventMember> findByEvent(Event event);

	boolean existsByUserAndEvent(User user, Event event);

	long countByEvent(Event event);
}
