package com.NgoServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.NgoServer.models.Volunteer;


@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer,Long>{
    
}
