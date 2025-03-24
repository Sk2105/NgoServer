package com.NgoServer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.NgoServer.repo.VolunteerRepository;

@Service
public class VolunteerService {


    @Autowired
    private VolunteerRepository volunteerRepository;
    
}
