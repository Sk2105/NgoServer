package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.DonorDTO;
import com.NgoServer.exceptions.UserNotFoundException;
import com.NgoServer.models.Campaign;
import com.NgoServer.models.Donation;
import com.NgoServer.models.Donor;
import com.NgoServer.models.User;
import com.NgoServer.utils.CampaignStatus;
import com.NgoServer.utils.PaymentStatus;
import com.NgoServer.utils.Role;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
        user.setCreatedAt(toLocalDateTime(donorData[7]));

        return new DonorDTO(
                (Long) donorData[0],
                user,
                (Double) donorData[2],
                toLocalDateTime(donorData[1]));
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
    default Donor findDonorById(long donorId) {
        return findDonorDetailsByIdObjects(donorId).stream()
                .map(
                        this::toDonor)
                .findFirst().orElseThrow(() -> new UserNotFoundException("Donor not found"));

    }

    private Donor toDonor(Object[] donorData) {
        Donor donor = new Donor();
        donor.setId((Long) donorData[0]);
        donor.setLastDonation(toLocalDateTime(donorData[1]));
        donor.setTotalDonation((Double) donorData[2]);

        User user = new User();
        user.setId((Long) donorData[3]);
        user.setUsername((String) donorData[4]);
        user.setEmail((String) donorData[5]);
        user.setPhoneNumber((String) donorData[6]);
        user.setCreatedAt(toLocalDateTime(donorData[7]));
        user.setRole(Role.DONOR);

        donor.setUser(user);

        List<Donation> donations = findDonationsByDonorId(donor.getId());
        donor.setDonations(donations);

        return donor;
    }

    /**
     * Retrieves a list of {@link DonationResponseDTO} objects associated with the
     * donor
     * identified by the given ID.
     *
     * @param donorId the ID of the donor
     * @return a list of {@link DonationResponseDTO} objects associated with the
     *         donor
     */
    @Query("""
            SELECT
                d.id, d.amount, d.createdAt, d.paymentId, d.status,
                c.id, c.title, c.description, c.createdAt, c.status
            FROM Donation d
            JOIN d.campaign c
            WHERE d.donor.id = :donorId
            """)
    List<Object[]> findDonationsByDonorIdObjects(Long donorId);

    /**
     * Retrieves a list of {@link Donation} objects associated with the donor
     * identified by the given ID.
     *
     * @param donorId the ID of the donor
     * @return a list of {@link Donation} objects associated with the donor
     */
    default List<Donation> findDonationsByDonorId(Long donorId) {
        return findDonationsByDonorIdObjects(donorId).stream()
                .map(this::toDonation)
                .toList();
    }

    private Donation toDonation(Object[] donationData) {
        Donation donation = new Donation();
        donation.setId((Long) donationData[0]);
        donation.setAmount((Double) donationData[1]);
        donation.setCreatedAt(toLocalDateTime(donationData[2]));
        donation.setPaymentId((String) donationData[3]);
        donation.setStatus((PaymentStatus) donationData[4]);

        Campaign campaign = new Campaign();
        campaign.setId((Long) donationData[5]);
        campaign.setTitle((String) donationData[6]);
        campaign.setDescription((String) donationData[7]);
        campaign.setCreatedAt(toLocalDateTime(donationData[8]));
        campaign.setStatus((CampaignStatus) donationData[9]);

        donation.setCampaign(campaign);

        return donation;
    }

    private LocalDateTime toLocalDateTime(Object obj) {
        if (obj instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        } else if (obj instanceof LocalDateTime ldt) {
            return ldt;
        }
        return null;
    }

}
