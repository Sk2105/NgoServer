package com.NgoServer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.NgoServer.repo.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    
}
