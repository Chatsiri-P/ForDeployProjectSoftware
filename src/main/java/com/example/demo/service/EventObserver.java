package com.example.demo.service;

import com.example.demo.model.Event;
import com.example.demo.model.User;

public interface EventObserver {
    void onUserJoinedEvent(User user, Event event);

}
