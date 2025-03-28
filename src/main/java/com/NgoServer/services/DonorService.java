package com.NgoServer.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.DonorDTO;
import com.NgoServer.dto.DonorResponseDTO;
import com.NgoServer.exceptions.UserNotFoundException;
import com.NgoServer.models.Donor;
import com.NgoServer.models.User;
import com.NgoServer.repo.DonorRepository;

@Service
public class DonorService {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private AuthServices authServices;

    public DonorResponseDTO getDonorById(long id) {
        DonorResponseDTO donor = donorRepository.findDonorById(id);
        if (donor == null) {
            throw new UserNotFoundException("Donor not found");
        }
        return donor;
    }

    public List<DonorDTO> getAllDonors() {
        return donorRepository.findAllDonors();
    }

    public DonorResponseDTO getCurrentDonor() {
        User user = authServices.getCurrentUserDetails();
        DonorResponseDTO donor = donorRepository.findDonorByUserId(user.getId());
        if (donor == null) {
            throw new UserNotFoundException("Donor not found");
        }
        return donor;
    }

    public void save(Donor donor) { donorRepository.save(donor); }

  

}
