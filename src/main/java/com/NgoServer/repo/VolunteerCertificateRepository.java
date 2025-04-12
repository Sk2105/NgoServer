package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.NgoServer.models.VolunteerCertificate;

public interface VolunteerCertificateRepository extends JpaRepository<VolunteerCertificate, Long> {
    
}
