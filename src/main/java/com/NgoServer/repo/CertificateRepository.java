package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.Certificate;


@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {
    
}
