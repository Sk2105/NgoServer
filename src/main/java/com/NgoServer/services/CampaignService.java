package com.NgoServer.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.CampaignBodyDTO;
import com.NgoServer.dto.CampaignResponseDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.exceptions.CampaignAlreadyExits;
import com.NgoServer.exceptions.CampaignNotFoundException;
import com.NgoServer.exceptions.FoundEmptyElementException;
import com.NgoServer.models.Campaign;
import com.NgoServer.repo.CampaignRepository;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    public List<CampaignResponseDTO> getAllCampaigns() {
        return campaignRepository.findAllCampaign();
    }

    public ResponseDTO addCampaign(CampaignBodyDTO campaignDTO) throws CampaignAlreadyExits {

        Campaign campaign = toCampaign(campaignDTO);

        // check if campaign with same title already exists
        if (campaignRepository.findByTitle(campaignDTO.title()).isPresent()) {
            throw new CampaignAlreadyExits("Campaign with same title already exists");
        }
        campaignRepository.save(campaign);

        return new ResponseDTO(
                "Campaign added successfully",
                HttpStatus.OK.value());

    }

    private Campaign toCampaign(CampaignBodyDTO campaignDTO) {
        Campaign campaign = new Campaign();
        if (campaignDTO.title().equals("") || campaignDTO.title() == null) {
            throw new FoundEmptyElementException("Title is found Empty or null");
        }
        campaign.setTitle(campaignDTO.title());
        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setDescription(campaignDTO.description());
        campaign.setGoalAmount(campaignDTO.goalAmount());
        campaign.setStatus(campaignDTO.status());

        return campaign;

    }

    public CampaignResponseDTO getCampaignById(Long id) {
        Optional<CampaignResponseDTO> optionalCampaign = campaignRepository.findCampaignById(id);
        if (optionalCampaign.isEmpty()) {
            throw new CampaignNotFoundException("Campaign not found");
        }

        return optionalCampaign.get();
    }

    public ResponseDTO updateCampaign(Long id, CampaignBodyDTO campaignDTO) throws CampaignNotFoundException {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(id);
        if (optionalCampaign.isEmpty()) {
            throw new CampaignNotFoundException("Campaign not found");
        }
        if (campaignRepository.findByTitle(campaignDTO.title()).isPresent()) {
            throw new CampaignAlreadyExits("Campaign with same title already exists");
        }
        Campaign campaign = optionalCampaign.get();
        campaign.setId(id);
        campaign.setTitle(campaignDTO.title());
        campaign.setDescription(campaignDTO.description());
        campaign.setGoalAmount(campaignDTO.goalAmount());
        campaignRepository.save(campaign);
        return new ResponseDTO(
                "Campaign updated successfully",
                HttpStatus.OK.value());
    }

    public void deleteCampaign(Long id) throws CampaignNotFoundException {
        getCampaignById(id);
        campaignRepository.deleteById(id);
        
    }
}
