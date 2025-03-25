package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.Donation;


@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

}
