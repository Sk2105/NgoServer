package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.services.VolunteerService;

@RestController
@RequestMapping("/volunteers")
public class VolunteerController {


    @Autowired
    private VolunteerService volunteerService;
    
}
