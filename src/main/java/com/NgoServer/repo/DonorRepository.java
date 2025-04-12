package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.dto.DonationResponseDTO;
import com.NgoServer.dto.DonorDTO;
import com.NgoServer.dto.DonorResponseDTO;
import com.NgoServer.exceptions.UserNotFoundException;
import com.NgoServer.models.Donation;
import com.NgoServer.models.Donor;
import com.NgoServer.models.User;
import com.NgoServer.utils.Role;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

    @Query("SELECT d.id, d.lastDonation, d.totalDonation, u.id, u.username, u.email, u.phoneNumber, u.createdAt "
            + "FROM Donor d "
            + "JOIN d.user u ORDER BY d.id DESC")
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
            WHERE d.id = :donorId ORDER BY d.id DESC
            """, nativeQuery = true)
    List<Object[]> findDonorDetailsByIdObjects(Long donorId);

    /**
     * Retrieves a donor by their ID and returns a {@link DonorResponseDTO}
     * containing the donor's
     * details and associated donations.
     *
     * @param donorId the ID of the donor
     * @return a {@link DonorResponseDTO} with the donor's details and
     *         donations
     */
    default DonorResponseDTO findDonorById(long donorId) {
        return findDonorDetailsByIdObjects(donorId).stream()
                .map(this::toDonor)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Donor not found"));
    }

    private DonorResponseDTO toDonor(Object[] donorData) {
        User user = new User();
        user.setId((Long) donorData[3]);
        user.setUsername((String) donorData[4]);
        user.setEmail((String) donorData[5]);
        user.setPhoneNumber((String) donorData[6]);
        user.setCreatedAt(toLocalDateTime(donorData[7]));
        user.setRole(Role.DONOR);

        return new DonorResponseDTO(
                (Long) donorData[0],
                (Double) donorData[2],
                toLocalDateTime(donorData[1]),
                user,
                findDonationByDonorId((Long) donorData[0]));
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
               new  com.NgoServer.dto.DonationResponseDTO(
                    d.id, d.amount, d.createdAt,d.paymentId,  d.orderId, d.signature,d.status
                )
            FROM Donation d
            WHERE d.donor.id = :donorId ORDER BY d.id DESC
            """)
    List<DonationResponseDTO> findDonationByDonorId(Long donorId);

    /**
     * Retrieves a list of {@link Donation} objects associated with the donor
     * identified by the given ID.
     *
     * @param donorId the ID of the donor
     * @return a list of {@link Donation} objects associated with the donor
     */

    @Query("SELECT d.id, d.lastDonation, d.totalDonation, u.id, u.username, u.email, u.phoneNumber, u.createdAt "
            + "FROM Donor d "
            + "JOIN d.user u where u.id = :userId ORDER BY d.id DESC")
    List<Object[]> findDonorByUserIdObjects(long userId);

    default DonorResponseDTO findDonorByUserId(long userId) {
        return findDonorByUserIdObjects(userId).stream()
                .map(this::toDonor)
                .findFirst().orElseThrow(() -> new UserNotFoundException("Donor not found"));
    }

    private LocalDateTime toLocalDateTime(Object o) {
        if (o instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        } else if (o instanceof LocalDateTime ldt) {
            return ldt;
        }
        return null;
    }

}

