package com.NgoServer.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

        Optional<Campaign> findByTitle(String title);

}
