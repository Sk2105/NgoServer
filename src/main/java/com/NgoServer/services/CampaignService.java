package com.NgoServer.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.CampaignDTO;
import com.NgoServer.exceptions.CampaignAlreadyExits;
import com.NgoServer.exceptions.CampaignNotFoundException;
import com.NgoServer.models.Campaign;
import com.NgoServer.repo.CampaignRepository;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Campaign addCampaign(CampaignDTO campaignDTO) throws CampaignAlreadyExits {

        Campaign campaign = toCampaign(campaignDTO);

        // check if campaign with same title already exists
        if (campaignRepository.findByTitle(campaignDTO.title()).isPresent()) {
            throw new CampaignAlreadyExits("Campaign with same title already exists");
        }
        return campaignRepository.save(campaign);

    }

    private Campaign toCampaign(CampaignDTO campaignDTO) {
        Campaign campaign = new Campaign();
        campaign.setTitle(campaignDTO.title());
        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setDescription(campaignDTO.description());
        campaign.setGoalAmount(campaignDTO.goalAmount());
        campaign.setCollectedAmount(campaignDTO.collectedAmount());
        campaign.setStatus(campaignDTO.status());

        return campaign;

    }

    public Campaign getCampaignById(Long id) {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(id);
        if (optionalCampaign.isEmpty()) {
            throw new CampaignNotFoundException("Campaign not found");
        }

        return optionalCampaign.get();
    }

    public Campaign updateCampaign(Long id, CampaignDTO campaignDTO) throws CampaignNotFoundException {
        if (campaignRepository.findByTitle(campaignDTO.title()).isPresent()) {
            throw new CampaignAlreadyExits("Campaign with same title already exists");
        }
        Campaign campaign = toCampaign(campaignDTO);
        campaign.setId(id);
        return campaignRepository.save(campaign);
    }

    public void deleteCampaign(Long id) throws CampaignNotFoundException {
        Campaign campaign = getCampaignById(id);
        campaignRepository.delete(campaign);
    }
}
