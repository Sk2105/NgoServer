package com.NgoServer.repo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.Donation;
import com.NgoServer.models.Donor;
import com.NgoServer.models.User;
import com.NgoServer.utils.PaymentStatus;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query(value = """
            SELECT d.id, d.amount, d.created_at, d.payment_id, d.status, d.order_id, d.signature, d.donor_id
            FROM donations d
            WHERE d.order_id = :orderId ORDER BY d.id DESC
            """, nativeQuery = true)
    List<Object[]> findDonationByOrderIdObjects(String orderId);

    default Donation findDonationByOrderId(String orderId) {
        return findDonationByOrderIdObjects(orderId).stream()
                .map(this::toDonation)
                .findFirst()
                .orElse(null);
    }

    private Donation toDonation(Object[] objects) {
        Donation donation = new Donation();
        donation.setId((Long) objects[0]);
        donation.setAmount((Double) objects[1]);
        donation.setCreatedAt(toLocalDateTime(objects[2]));
        donation.setPaymentId((String) objects[3]);
        donation.setStatus(PaymentStatus.valueOf((String) objects[4]));
        donation.setOrderId((String) objects[5]);
        donation.setSignature((String) objects[6]);
        donation.setDonor(findDonorByDonorId((Long) objects[7]));
        return donation;
    }

    private LocalDateTime toLocalDateTime(Object object) {
        if (object instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        } else if (object instanceof LocalDateTime ldt) {
            return ldt;
        }
        return null;
    }

    @Query(value = """
            SELECT d.id, d.total_donation, d.last_donation, u.id, u.username, u.email, u.phone_number, u.created_at
            FROM donors d
            JOIN users u ON u.id = d.user_id
            WHERE d.id = :donorId ORDER BY d.id DESC
            """, nativeQuery = true)
    List<Object[]> findDonorByDonorIdObjects(Long donorId);

    default Donor findDonorByDonorId(Long donorId) {
        return findDonorByDonorIdObjects(donorId).stream()
                .map(this::toDonor)
                .findFirst()
                .orElse(null);
    }

    private Donor toDonor(Object[] objects) {
        Donor donor = new Donor();
        donor.setId((Long) objects[0]);
        donor.setTotalDonation((Double) objects[1]);
        donor.setLastDonation(toLocalDateTime(objects[2]));

        User user = new User();
        user.setId((Long) objects[3]);
        user.setUsername((String) objects[4]);
        user.setEmail((String) objects[5]);
        user.setPhoneNumber((String) objects[6]);
        user.setCreatedAt(toLocalDateTime(objects[7]));

        donor.setUser(user);
        return donor;
    }

    @Query("""
             SELECT new com.NgoServer.models.Donation(d.id, d.amount, d.createdAt,
             new com.NgoServer.models.Donor(d.donor.id,
             new com.NgoServer.models.User(u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role), d.donor.totalDonation, d.donor.lastDonation)
            , d.paymentId, d.orderId, d.signature,d.status)
             FROM Donation d
             JOIN d.donor
             JOIN d.donor.user u ORDER BY d.id DESC
             """)
    List<Donation> findAllDonations();

    @Query("""
             SELECT new com.NgoServer.models.Donation(d.id, d.amount, d.createdAt,
             new com.NgoServer.models.Donor(d.donor.id,
             new com.NgoServer.models.User(u.id, u.username, u.email, u.phoneNumber, u.createdAt, u.role), d.donor.totalDonation, d.donor.lastDonation)
            , d.paymentId, d.orderId, d.signature,d.status)
             FROM Donation d
             JOIN d.donor
             JOIN d.donor.user u
             WHERE d.id = :id ORDER BY d.id DESC
             """)
    Optional<Donation> findDonationById(Long id);

}