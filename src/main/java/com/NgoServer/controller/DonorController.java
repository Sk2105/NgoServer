package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.services.DonorService;

@RestController
@RequestMapping("/api/donors")
public class DonorController {



    @Autowired
    private DonorService donorService;


    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAllDonors() {
        return ResponseEntity.ok().body(donorService.getAllDonors());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getDonorById(@PathVariable Long id) {
        return ResponseEntity.ok().body(donorService.getDonorById(id));
    }
    
}
