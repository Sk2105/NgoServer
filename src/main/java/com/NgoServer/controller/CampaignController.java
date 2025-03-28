package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.CampaignBodyDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.services.CampaignService;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getAllCampaigns() {
        return ResponseEntity.ok().body(campaignService.getAllCampaigns());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCampaign(@RequestBody CampaignBodyDTO campaignDTO) {
        return ResponseEntity.ok().body(campaignService.addCampaign(campaignDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok().body(campaignService.getCampaignById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCampaign(@PathVariable Long id, @RequestBody CampaignBodyDTO campaignDTO) {
        return ResponseEntity.ok().body(campaignService.updateCampaign(id, campaignDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.ok().body(
                new ResponseDTO("Deleted Successfully", HttpStatus.OK.value()));
    }


}
