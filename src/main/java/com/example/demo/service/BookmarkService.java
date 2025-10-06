package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.*;
import com.example.demo.repository.BookmarkRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public void addBookmark(User user, Event event) {
        if (!bookmarkRepository.existsByUserAndEvent(user, event)) {
            Bookmark bookmark = new Bookmark(user, event);
            bookmarkRepository.save(bookmark);
        }
    }

    public void removeBookmark(User user, Event event) {
        bookmarkRepository.findByUserAndEvent(user, event)
                .ifPresent(bookmarkRepository::delete);
    }

    public boolean isBookmarked(User user, Event event) {
        return bookmarkRepository.existsByUserAndEvent(user, event);
    }

    public List<Event> getUserBookmarkedEvents(User user) {
        return bookmarkRepository.findByUser(user).stream()
                .map(Bookmark::getEvent)
                .collect(Collectors.toList());
    }
}
