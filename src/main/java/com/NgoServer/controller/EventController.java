package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.services.EventService;

@RequestMapping("/events")
@RestController
public class EventController {


    @Autowired
    private EventService eventService;
    
}
