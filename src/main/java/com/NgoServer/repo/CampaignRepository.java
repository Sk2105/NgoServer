package com.NgoServer.repo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.CampaignResponseDTO;
import com.NgoServer.models.Campaign;
import com.NgoServer.utils.CampaignStatus;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

        @Query("""
                         SELECT c.id, c.title, c.description, c.createdAt, c.goalAmount, c.collectedAmount, c.status
                         FROM Campaign c
                         WHERE c.id = :id
                        """)
        List<Object[]> findCampaignByIdObjects(long id);

        default Optional<CampaignResponseDTO> findCampaignById(long id) {
                return findCampaignByIdObjects(id).stream()
                                .map(campaignData -> {
                                        long campaignId = (long) campaignData[0];
                                        String campaignTitle = (String) campaignData[1];
                                        String campaignDescription = (String) campaignData[2];
                                        LocalDateTime campaignCreatedAt = toLocalDateTime(campaignData[3]);
                                        double campaignGoalAmount = (double) campaignData[4];
                                        double campaignCollectedAmount = (double) campaignData[5];
                                        CampaignStatus campaignStatus = (CampaignStatus) campaignData[6];

                                        return new CampaignResponseDTO(campaignId, campaignTitle, campaignDescription,
                                                        campaignCreatedAt, campaignGoalAmount, campaignCollectedAmount,
                                                        campaignStatus);
                                })
                                .findFirst();
        }

        private LocalDateTime toLocalDateTime(Object obj) {
                if (obj instanceof Timestamp timestamp) {
                        return timestamp.toLocalDateTime();
                } else if (obj instanceof LocalDateTime ldt) {
                        return ldt;
                }
                return null;
        }

        Optional<Campaign> findByTitle(String title);

        @Query("""
                        SELECT c.id, c.title, c.description,  c.createdAt ,c.goalAmount, c.collectedAmount, c.status
                        FROM Campaign c
                        """)
        List<Object[]> findAllCampaignObjects();

        default List<CampaignResponseDTO> findAllCampaign() {
                return findAllCampaignObjects().stream()
                                .map(campaignData -> {
                                        Long id = (Long) campaignData[0];
                                        String title = (String) campaignData[1];
                                        String description = (String) campaignData[2];
                                        LocalDateTime createdAt = toLocalDateTime(campaignData[3]);
                                        Double goalAmount = (Double) campaignData[4];
                                        Double collectedAmount = (Double) campaignData[5];
                                        CampaignStatus status = (CampaignStatus) campaignData[6];

                                        return new CampaignResponseDTO(
                                                        id,
                                                        title,
                                                        description,
                                                        createdAt,
                                                        goalAmount,
                                                        collectedAmount,
                                                        status);
                                })
                                .toList();
        }

}
