package com.NgoServer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NgoServer.dto.InitiateBodyDTO;
import com.NgoServer.dto.VerifyPaymentBodyDTO;
import com.NgoServer.services.DonationService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Value("${key.secret}")
    private String keySecret;

    @PostMapping("/verify")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<?> verifyPayment(@RequestBody VerifyPaymentBodyDTO body) throws RazorpayException {
        return ResponseEntity.ok().body(donationService.verifyPayment(body));
    }

    @PostMapping("/initiate-donation")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<?> initiateDonation(@RequestBody InitiateBodyDTO initiateBody) {
        try {
            return ResponseEntity.ok(donationService.createOrder(initiateBody).toString());
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDonations() {
        return ResponseEntity.ok().body(donationService.findAllDonation());
    }

    @GetMapping("/{donorId}")
    public ResponseEntity<?> getDonationByDonorId(@PathVariable Long donorId) {
        return ResponseEntity.ok().body(donationService.findDonationById(donorId));
    }

}
