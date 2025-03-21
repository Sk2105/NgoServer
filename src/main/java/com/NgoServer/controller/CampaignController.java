package com.NgoServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.CampaignDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.services.CampaignService;

@RestController
@RequestMapping("/campaign")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @GetMapping
    public ResponseEntity<?> getAllCampaigns() {
        return ResponseEntity.ok().body(campaignService.getAllCampaigns());
    }

    @PostMapping
    public ResponseEntity<?> addCampaign(@RequestBody CampaignDTO campaignDTO) {
        return ResponseEntity.ok().body(campaignService.addCampaign(campaignDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok().body(campaignService.getCampaignById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCampaign(@PathVariable Long id, @RequestBody CampaignDTO campaignDTO) {
        return ResponseEntity.ok().body(campaignService.updateCampaign(id, campaignDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.ok().body(
                new ResponseDTO("Deleted Successfully", HttpStatus.OK.value()));
    }

}
