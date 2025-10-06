package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.dto.CommentDto;
import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventService eventService;

    @Autowired
    public CommentService(CommentRepository commentRepository, EventService eventService) {
        this.commentRepository = commentRepository;
        this.eventService = eventService;
    }

    public Comment addComment(CommentDto commentDto, User user) {
        Event event = eventService.findEventById(commentDto.getEventId());
        Comment comment = new Comment(commentDto.getText(), user, event);
        return commentRepository.save(comment);
    }

    public List<Comment> getEventComments(Event event) {
        return commentRepository.findByEventOrderByCreatedAtDesc(event);
    }
    
    public Page<Comment> getEventCommentsPaginated(Event event, Pageable pageable) {
        return commentRepository.findByEventOrderByCreatedAtDesc(event, pageable);
    }

    public long getCommentCount(Event event) {
        return commentRepository.countByEvent(event);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        // Check if user owns the comment
        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("You can only delete your own comments");
        }
        
        // Soft delete - mark as deleted instead of removing
        comment.setText("This comment has been deleted");
        comment.setDeleted(true);
        commentRepository.save(comment);
    }

}