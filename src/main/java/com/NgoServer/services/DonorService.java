package com.NgoServer.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.DonorDTO;
import com.NgoServer.dto.DonorWithCampaignsDTO;
import com.NgoServer.exceptions.UserNotFoundException;
import com.NgoServer.repo.DonorRepository;

@Service
public class DonorService {


    @Autowired
    private DonorRepository donorRepository;



    public DonorWithCampaignsDTO getDonorById(long id) {
        DonorWithCampaignsDTO donor = donorRepository.findDonorById(id);
        if(donor == null){
            throw new UserNotFoundException("Donor not found");
        }
        return donor;
    }

    public List<DonorDTO> getAllDonors() {
        return donorRepository.findAllDonors();
    }





    
}
