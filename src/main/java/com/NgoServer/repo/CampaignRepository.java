package com.NgoServer.repo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.CampaignDTO;
import com.NgoServer.models.Campaign;
import com.NgoServer.utils.CampaignStatus;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("""
            SELECT c.id, c.title, c.description,  c.createdAt ,c.goalAmount, c.collectedAmount, c.status
            FROM Campaign c
            WHERE c.id = :id
            """)
    List<Object[]> findByIdObjects(long id);

    default Optional<CampaignDTO> findById(long id) {
        return Optional.ofNullable(findByIdObjects(id).stream()
                .map(campaignData -> {
                    String title = (String) campaignData[1];
                    String description = (String) campaignData[2];
                    LocalDateTime createdAt = campaignData[3] instanceof Timestamp 
                            ? ((Timestamp) campaignData[3]).toLocalDateTime() 
                            : null;
                    Double goalAmount = (Double) campaignData[4];
                    Double collectedAmount = (Double) campaignData[5];
                    CampaignStatus status = (CampaignStatus) campaignData[6];

                    return new CampaignDTO(title, description, createdAt, goalAmount, collectedAmount, status);
                })
                .findFirst()
                .orElse(null));
                
    }

    Optional<Campaign> findByTitle(String title);

    @Query("""
            SELECT c.title, c.description,  c.createdAt ,c.goalAmount, c.collectedAmount, c.status
            FROM Campaign c
            """)
    List<Object[]> findAllCampaignObjects();

    default List<CampaignDTO> findAllCampaign() {
        return findAllCampaignObjects().stream()
                .map(campaignData -> {
                    String title = (String) campaignData[0];
                    String description = (String) campaignData[1];
                    LocalDateTime createdAt = campaignData[2] instanceof Timestamp 
                            ? ((Timestamp) campaignData[2]).toLocalDateTime() 
                            : null;
                    Double goalAmount = (Double) campaignData[3];
                    Double collectedAmount = (Double) campaignData[4];
                    CampaignStatus status = (CampaignStatus) campaignData[5];

                    return new CampaignDTO(title, description, createdAt, goalAmount, collectedAmount, status);
                })
                .toList();
    }


}
