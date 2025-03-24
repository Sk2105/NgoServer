package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.CampaignDTO;
import com.NgoServer.dto.DonorDTO;
import com.NgoServer.dto.DonorWithCampaignsDTO;
import com.NgoServer.exceptions.UserNotFoundException;
import com.NgoServer.models.Donor;
import com.NgoServer.models.User;
import com.NgoServer.utils.CampaignStatus;
import com.NgoServer.utils.Role;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

    @Query("SELECT d.id, d.lastDonation, d.totalDonation, u.id, u.username, u.email, u.phoneNumber, u.createdAt "
            + "FROM Donor d "
            + "JOIN d.user u")
    List<Object[]> findAllDonorsObjects();

    default List<DonorDTO> findAllDonors() {
        return findAllDonorsObjects().stream()
                .map(this::toDonorDTO)
                .toList();
    }

    private DonorDTO toDonorDTO(Object[] donorData) {
        User user = new User();
        user.setId((Long) donorData[3]);
        user.setUsername((String) donorData[4]);
        user.setEmail((String) donorData[5]);
        user.setPhoneNumber((String) donorData[6]);
        user.setRole(Role.DONOR);
        user.setCreatedAt((LocalDateTime) donorData[7]);

        return new DonorDTO(
                (Long) donorData[0],
                user,
                (Double) donorData[2],
                (LocalDateTime) donorData[1]);
    }

    /**
     * Retrieves a donor by their ID and returns an array of objects containing the
     * donor's details.
     *
     * @param donorId the ID of the donor
     * @return an array of objects with the donor's details:
     *         { id: Long, lastDonation: LocalDateTime, totalDonation: Double,
     *         userId: Long, username: String, email: String, phoneNumber: String,
     *         createdAt: LocalDateTime }
     */
    @Query(value = """
            SELECT d.id, d.last_donation, d.total_donation,
            u.id AS user_id, u.username, u.email, u.phone_number, u.created_at
            FROM donors d
            JOIN users u ON u.id = d.user_id
            WHERE d.id = :donorId
            """, nativeQuery = true)
    List<Object[]> findDonorDetailsByIdObjects(Long donorId);

    /**
     * Retrieves a donor by their ID and returns a {@link DonorWithCampaignsDTO}
     * containing the donor's
     * details and associated campaigns.
     *
     * @param donorId the ID of the donor
     * @return a {@link DonorWithCampaignsDTO} with the donor's details and
     *         campaigns
     */
    default DonorWithCampaignsDTO findDonorById(long donorId) {
        List<Object[]> donorDataList = findDonorDetailsByIdObjects(donorId);
        Object[] donorData = donorDataList.isEmpty() ? null : donorDataList.get(0);
        if (donorData == null) {
            throw new UserNotFoundException("Donor not found");
        }

        User user = new User();
        user.setId((Long) donorData[3]);
        user.setUsername((String) donorData[4]);
        user.setEmail((String) donorData[5]);
        user.setPhoneNumber((String) donorData[6]);
        user.setCreatedAt(((Timestamp) donorData[7]).toLocalDateTime());
        user.setRole(Role.DONOR);

        return new DonorWithCampaignsDTO(
                (Long) donorData[0],
                user,
                (Double) donorData[2],
                ((Timestamp) donorData[1]).toLocalDateTime(),
                findCampaignsByDonorId(donorId));
    }

    /**
     * Retrieves a list of campaigns associated with a specific donor ID.
     *
     * @param donorId the ID of the donor
     * @return a list of campaign DTOs
     */
    @Query(value = """
            SELECT c.id, c.title, c.description, c.created_at, c.goal_amount, c.collected_amount, c.status
            FROM donor_campaign dc
            JOIN campaigns c ON dc.campaign_id = c.id
            WHERE dc.donor_id = :donorId;
            """, nativeQuery = true)
    List<Object[]> findCampaignsByDonorIdObjects(@Param("donorId") long donorId);

    default List<CampaignDTO> findCampaignsByDonorId(long donorId) {
        return findCampaignsByDonorIdObjects(donorId)
                .stream()
                .map(
                        campaignData -> new CampaignDTO(
                                (String) campaignData[1], // Title
                                (String) campaignData[2], // Description
                                ((Timestamp) campaignData[3]).toLocalDateTime(), // CreatedAt
                                (Double) campaignData[3], // GoalAmount
                                (Double) campaignData[4], // CollectedAmount
                                getCampaignStatus((String) campaignData[5]))) // CampaignStatus
                .toList();

    }

    private CampaignStatus getCampaignStatus(String status) {
        return Arrays.stream(CampaignStatus.values())
                .filter(campaignStatus -> campaignStatus.name().equals(status))
                .findFirst()
                .orElseThrow();
    }

}
