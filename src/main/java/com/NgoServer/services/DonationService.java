package com.NgoServer.services;

import java.time.LocalDateTime;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.NgoServer.dto.DonorResponseDTO;
import com.NgoServer.dto.InitiateBodyDTO;
import com.NgoServer.dto.ResponseDTO;
import com.NgoServer.dto.VerifyPaymentBodyDTO;
import com.NgoServer.models.Donation;
import com.NgoServer.models.Donor;
import com.NgoServer.repo.*;
import com.NgoServer.utils.PaymentStatus;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonorService donorService;

    @Value("${key.secret}")
    private String keySecret;

    @Value("${key.id}")
    private String keyId;

    public Order createOrder(InitiateBodyDTO initiate) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);

        JSONObject orderDetails = new JSONObject();
        orderDetails.put("amount", initiate.amount() * 100); // Amount in paise (100 INR = 10000 paise)
        orderDetails.put("currency", "INR");
        orderDetails.put("receipt", initiate.receipt());
        orderDetails.put("payment_capture", 1); // Auto capture payment

        Order order = razorpayClient.orders.create(orderDetails);

        Donation donation = new Donation();
        donation.setAmount(initiate.amount());
        donation.setOrderId(order.get("id"));
        donation.setCreatedAt(LocalDateTime.now());
        donation.setStatus(PaymentStatus.PENDING);

        Donor donor = toDonor(donorService.getCurrentDonor());
        donation.setDonor(donor);
        donationRepository.save(donation);
        return order;

    }

    public ResponseDTO verifyPayment(VerifyPaymentBodyDTO body) throws RazorpayException {
        String generatedSignature = Utils.getHash(body.orderId() + "|" + body.paymentId(), keySecret);
        Donation donation = donationRepository.findDonationByOrderId(body.orderId());

        if (donation == null) {
            throw new RuntimeException("Invalid Order Id");
        }
        if (donation.getStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment Already Verified");
        }

        if (donation.getStatus() == PaymentStatus.FAILED) {
            throw new RuntimeException("Payment Already Failed");
        }
        donation.setPaymentId(body.paymentId());
        donation.setSignature(body.signature());
        if (generatedSignature.equals(body.signature())) {
            donation.setStatus(PaymentStatus.SUCCESS);

            Double totalDonation = donation.getAmount() + donation.getDonor().getTotalDonation();
            Donor donor = donation.getDonor();
            donor.setTotalDonation(totalDonation);
            donor.setLastDonation(LocalDateTime.now());

            donorService.save(donor);
            donationRepository.save(donation);

            return new ResponseDTO("Payment Verified Successfully", 200);
        } else {
            donation.setStatus(PaymentStatus.FAILED);
            donationRepository.save(donation);
            throw new RuntimeException("Payment Verification Failed");

        }

    }

    private Donor toDonor(DonorResponseDTO currentDonor) {
        Donor donor = new Donor();
        donor.setId(currentDonor.id());
        donor.setTotalDonation(currentDonor.totalDonation());
        donor.setLastDonation(currentDonor.lastDonation());
        donor.setUser(currentDonor.user());
        return donor;
    }

    public List<Donation> findAllDonation() {
        return donationRepository.findAllDonations();
    }

    public Donation findDonationById(Long id) {
        return donationRepository.findDonationById(id).orElse(null);
    }
}
