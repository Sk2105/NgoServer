package com.NgoServer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.DonorDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.exceptions.UserAlreadyExists;
import com.NgoServer.repo.DonationRepository;

@Service
public class DonationService {


    @Autowired
    private DonationRepository donationRepository;



    
    
}
