package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.Image;


@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    // Custom query methods can be defined here if needed
    
}
