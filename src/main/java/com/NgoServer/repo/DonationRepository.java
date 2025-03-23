package com.NgoServer.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.User;


@Repository
public interface DonationRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
}
