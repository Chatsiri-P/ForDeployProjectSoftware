package com.example.demo.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.*;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByEvent(Event event);

	List<Image> findByUser(User user);

	List<Image> findByType(Image.ImageType type);
}