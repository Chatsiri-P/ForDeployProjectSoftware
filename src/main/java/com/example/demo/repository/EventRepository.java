package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Event;
import com.example.demo.model.User;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCreator(User creator);
    List<Event> findByDateAfterOrderByDateAsc(LocalDateTime date);
    
    @Query("SELECT e FROM Event e WHERE e.date > :currentDate ORDER BY e.date ASC")
    List<Event> findUpcomingEvents(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT e FROM Event e JOIN e.members m WHERE m.user = :user")
    List<Event> findEventsByMember(@Param("user") User user);
    
    @Query("SELECT e FROM Event e WHERE e.category.id = :categoryId AND e.date > :currentDate ORDER BY e.date ASC")
    List<Event> findByCategoryAndUpcoming(@Param("categoryId") Long categoryId, @Param("currentDate") LocalDateTime currentDate);
    
    // Search by title or creator name
    @Query("SELECT e FROM Event e WHERE " +
           "(LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.creator.username) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND e.date > :currentDate ORDER BY e.date ASC")
    List<Event> searchEvents(@Param("keyword") String keyword, @Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT e FROM Event e WHERE e.category = :category AND e.date > :currentDate ORDER BY e.date ASC")
    List<Event> findByCategoryAndUpcoming(@Param("category") com.example.demo.model.Category category, @Param("currentDate") LocalDateTime currentDate);
    
 // Paginated
    Page<Event> findByCreator(User creator, Pageable pageable);
    
    @Query("SELECT e FROM Event e WHERE e.date > :currentDate ORDER BY e.date ASC")
    Page<Event> findUpcomingEvents(@Param("currentDate") LocalDateTime currentDate, Pageable pageable);
    
    @Query("SELECT e FROM Event e JOIN e.members m WHERE m.user = :user")
    Page<Event> findEventsByMember(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT e FROM Event e WHERE e.category.id = :categoryId AND e.date > :currentDate ORDER BY e.date ASC")
    Page<Event> findByCategoryAndUpcoming(@Param("categoryId") Long categoryId, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);
    
    @Query("SELECT e FROM Event e WHERE " +
           "(LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.creator.username) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND e.date > :currentDate ORDER BY e.date ASC")
    Page<Event> searchEvents(@Param("keyword") String keyword, @Param("currentDate") LocalDateTime currentDate, Pageable pageable);
}