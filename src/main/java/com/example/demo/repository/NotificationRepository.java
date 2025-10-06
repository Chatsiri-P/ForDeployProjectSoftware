package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Notification;
import com.example.demo.model.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUserOrderByCreatedAtDesc(User user);

	List<Notification> findByUserAndReadOrderByCreatedAtDesc(User user, boolean read);

	long countByUserAndRead(User user, boolean read);
}