package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.*;

@Component
public class EventSubject {

    private List<EventObserver> observers = new ArrayList<>();

    @Autowired
    public void setObservers(List<EventObserver> observers) {
        this.observers = observers;
    }

    public void addObserver(EventObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(EventObserver observer) {
        observers.remove(observer);
    }

    public void notifyUserJoined(User user, Event event) {
        for (EventObserver observer : observers) {
            observer.onUserJoinedEvent(user, event);
        }
    }
}